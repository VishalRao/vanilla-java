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

import vanilla.java.collections.api.HugeIterator;
import vanilla.java.collections.api.HugeListIterator;
import vanilla.java.collections.api.impl.HugeElement;
import vanilla.java.collections.util.HugeCollections;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

public class SelectedList<E> extends AbstractHugeCollection<E> {
  private final AbstractHugeCollection<E> list;
  private final List<LongBuffer> indecies = new ArrayList<LongBuffer>();
  private int size = 0;

  public SelectedList(Class<E> elementType, AbstractHugeCollection<E> list) {
    super(elementType, null);
    ((AbstractHugeCollection) this).size = new SelectedListSizeHolder();
    this.list = list;
    indecies.add(HugeCollections.acquireLongBuffer());
  }

  @Override
  public boolean add(E e) {
    int bufferNum = (size / HugeCollections.LONG_BUFFER_SIZE);
    int offset = (size & (HugeCollections.LONG_BUFFER_SIZE - 1));
    if (bufferNum >= indecies.size())
      indecies.add(HugeCollections.acquireLongBuffer());
    final LongBuffer buffer = indecies.get(bufferNum);
    final long index = ((HugeElement) e).index();
    buffer.put(offset, index);
    size++;
    return true;
  }

  @Override
  protected E get(long index) {
    long num = mapIndex(index);
    return list.get(num);
  }

  private long mapIndex(long index) {
    if (index >= size) throw new IndexOutOfBoundsException();
    int bufferNum = (int) (index / HugeCollections.LONG_BUFFER_SIZE);
    int offset = (int) (index & (HugeCollections.LONG_BUFFER_SIZE - 1));
    return indecies.get(bufferNum).get(offset);
  }

  @Override
  public HugeIterator<E> iterator() {
    return new HugeIterator<E>() {
      final HugeElement e = (HugeElement) list.get(0);
      long index = -1;

      @Override
      public HugeElement nextElement() {
        return e;
      }

      @Override
      public void index(long index) {
        this.index = index;
      }

      @Override
      public long index() {
        return index;
      }

      @Override
      public boolean hasNext() {
        return index + 1 < size;
      }

      @Override
      public E next() {
        e.index(mapIndex(++index));
        return (E) e;
      }

      @Override
      public void remove() {
        list.remove(e.index());
      }

      @Override
      public void recycle() {
      }
    };
  }

  @Override
  public HugeListIterator<E> listIterator(long start, long end) {
    throw new Error("Not implemented");
  }

  @Override
  public E remove(long index) {
    long num = mapIndex(index);
    return list.remove(num);
  }

  @Override
  protected void growCapacity(long capacity) {
    throw new Error("Not implemented");
  }

  @Override
  public int partitionSize() {
    return list.partitionSize();
  }

  @Override
  public void recycle(Object recycleable) {
    list.recycle(recycleable);
  }

  @Override
  public void recycle() {
    this.size = 0;
    // leave one.
    while (indecies.size() > 1)
      HugeCollections.recycle(indecies.remove(indecies.size() - 1));
    list.recycle(this);
  }

  class SelectedListSizeHolder extends SimpleSizeHolder {
    @Override
    public void size(long size) {
      if (size > size()) throw new IllegalArgumentException();
      SelectedList.this.size = (int) size;
    }

    @Override
    public long partitionSize() {
      return SelectedList.this.partitionSize();
    }

    @Override
    public long size() {
      return SelectedList.this.size;
    }
  }

}
