/*
 * Copyright (c) 2011 Peter Lawrey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vanilla.java.collections.impl;

import vanilla.java.collections.api.*;
import vanilla.java.collections.api.impl.ByteBufferAllocator;
import vanilla.java.collections.api.impl.Copyable;
import vanilla.java.collections.api.impl.HugeElement;
import vanilla.java.collections.api.impl.HugePartition;
import vanilla.java.collections.util.HugeCollections;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class AbstractHugeArrayList<E> extends AbstractHugeCollection<E> implements HugeList<E>, RandomAccess {
  protected final List<HugePartition> partitions = new ArrayList<HugePartition>();
  protected final ByteBufferAllocator allocator;
  private final Class<E> elementType;
  protected final Deque<E> pointerPool = new ArrayDeque<E>();
  private final Deque<HugeListIterator<E>> iteratorPool = new ArrayDeque<HugeListIterator<E>>();
  protected final Deque<E> implPool = new ArrayDeque<E>();

  protected AbstractHugeArrayList(int partitionSize, Class<E> elementType, ByteBufferAllocator allocator) {
    super(elementType, allocator.sizeHolder());
    if (this.size.partitionSize() < 1)
      this.size.partitionSize(partitionSize);
    this.elementType = elementType;
    this.allocator = allocator;
  }

  @Override
  public E get(long index) {
    E e = pointerPool.poll();
    if (e == null) e = createPointer();
    ((HugeElement) e).index(index);
    return e;
  }

  protected abstract E createPointer();

  @Override
  public HugeListIterator<E> listIterator(long start, long end) {
    HugeListIterator<E> e = iteratorPool.poll();
    if (e == null) e = createIterator();
    e.index(start - 1);
    e.end(end);
    return e;
  }

  protected abstract HugeListIterator<E> createIterator();

  @Override
  public void recycle() {
  }

  @Override
  public void recycle(Object recycleable) {
    if (recycleable instanceof VanillaHugeListIterator)
      iteratorPool.add((HugeListIterator<E>) recycleable);
    else if (recycleable instanceof SubList)
      subListPoolAdd((SubList<E>) recycleable);
  }

  @Override
  public E remove(long index) {
    final long size1 = longSize() - 1;
    if (index != size1) {
      E from = get(index + 1);
      E to = get(index);
      for (long i = index; i < size1; i++) {
        ((HugeElement) from).index(index + 1);
        ((HugeElement) to).index(index);
        ((Copyable<E>) to).copyFrom(from);
      }
    }
    final Copyable<E> e = (Copyable<E>) get(size1);
    setSize(size1);
    final E e2 = e.copyOf();
    ((Recycleable) e).recycle();
    return e2;
  }

  public E acquireImpl() {
    E e = implPool.poll();
    return e == null ? createImpl() : e;
  }

  protected abstract E createImpl();

  @Override
  protected void growCapacity(long capacity) {
    final int partitionSize = partitionSize();
    long partitions = (capacity + partitionSize - 1) / partitionSize;
    try {
//      System.out.println("Adding partition " + partitions + " for " + capacity);
      while (this.partitions.size() < partitions)
        this.partitions.add(createPartition(this.partitions.size()));

      final long minCapacity = partitions * partitionSize;
      if (minCapacity > this.size.capacity())
        this.size.capacity(minCapacity);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to grow collection", e);
    }
  }

  public HugePartition partitionFor(long index) {
    final int n = (int) (index / partitionSize());
    if (n >= partitions.size())
      growCapacity(index + 1);
    return partitions.get(n);
  }

  protected abstract HugePartition createPartition(int partitionNumber) throws IOException;

  @Override
  public void flush() throws IOException {
    super.flush();
    for (HugePartition partition : partitions) {
      partition.flush();
    }
    allocator.flush();
  }

  @Override
  public void close() throws IOException {
    super.close();
    for (HugePartition partition : partitions) {
      partition.close();
    }
    allocator.close();
  }

  //// MULTI_THREADED SUPPORT.

  @Override
  public long update(final Predicate<E> predicate, final Updater<E> updater) {
    List<Future<Long>> tasks = new ArrayList<Future<Long>>();
    final int partitionSize = partitionSize();
    for (long start = 0, size = longSize(); start < size; start += partitionSize) {
      final long partitionEnd = Math.min(start + partitionSize, size);
      if (partitionEnd <= start) break;

      final HugeElement he = (HugeElement) get(start);
      final long finalStart = start;
      tasks.add(HugeCollections.EXECUTOR_SERVICE.submit(new Callable<Long>() {
        @Override
        public Long call() throws Exception {
          long count = 0;
          for (long i = finalStart; i < partitionEnd; i++) {
            he.index(i);
            if (predicate.test((E) he) && updater.update((E) he))
              count++;
          }
          return count;
        }
      }));
    }
    long count = 0;
    try {
      for (Future<Long> task : tasks) {
        count += task.get();
      }
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    } catch (ExecutionException e) {
      Thread.currentThread().stop(e.getCause());
    }
    return count;
  }

  @Override
  public long update(Updater<E> updater) {
    return update((Predicate<E>) Predicates.ALL, updater);
  }
}
