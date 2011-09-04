package vanilla.java.collections.impl;

import vanilla.java.collections.api.HugeList;
import vanilla.java.collections.api.HugeListIterator;
import vanilla.java.collections.api.Recycleable;
import vanilla.java.collections.api.impl.ByteBufferAllocator;
import vanilla.java.collections.api.impl.Copyable;
import vanilla.java.collections.api.impl.HugeElement;
import vanilla.java.collections.api.impl.HugePartition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;

public abstract class AbstractHugeArrayList<E> extends AbstractHugeCollection<E> implements HugeList<E>, RandomAccess {
  protected final List<HugePartition> partitions = new ArrayList<HugePartition>();
  protected final int partitionSize;
  protected final ByteBufferAllocator allocator;
  private final Class<E> elementType;
  protected final List<E> pointerPool = new ArrayList<E>();
  private final List<HugeListIterator<E>> iteratorPool = new ArrayList<HugeListIterator<E>>();
  protected final List<E> implPool = new ArrayList<E>();
  private final List<SubList<E>> subListPool = new ArrayList<SubList<E>>();

  protected AbstractHugeArrayList(int partitionSize, Class<E> elementType, ByteBufferAllocator allocator) {
    super(elementType);
    this.partitionSize = partitionSize;
    this.elementType = elementType;
    this.allocator = allocator;
  }

  @Override
  public E get(long index) {
    final int size = pointerPool.size();
    E e = size > 0 ? pointerPool.remove(size - 1) : createPointer();
    ((HugeElement) e).index(index);
    return e;
  }

  protected abstract E createPointer();

  @Override
  public HugeListIterator<E> listIterator(long start, long end) {
    final int size = iteratorPool.size();
    HugeListIterator<E> e = size > 0 ? iteratorPool.remove(size - 1) : createIterator();
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
    if (index + 1 != size) {
      E from = get(index + 1);
      E to = get(index);
      for (long i = index; i < size - 1; i++) {
        ((HugeElement) from).index(index + 1);
        ((HugeElement) to).index(index);
        ((Copyable<E>) to).copyFrom(from);
      }
    }
    final Copyable<E> e = (Copyable<E>) get(--size);
    final E e2 = e.copyOf();
    ((Recycleable) e).recycle();
    return e2;
  }

  @Override
  public boolean remove(Object o) {
    throw new Error("Not implemented");
  }

  @Override
  public E set(long index, E element) {
    E e = get(index);
    E i = ((Copyable<E>) e).copyOf();
    ((Copyable<E>) e).copyFrom(element);
    return i;
  }

  public E acquireImpl() {
    final int size = implPool.size();
    return size > 0 ? implPool.remove(size - 1) : createImpl();
  }

  protected abstract E createImpl();

  @Override
  public HugeList<E> subList(long fromIndex, long toIndex) {
    final int size = subListPool.size();
    return size > 0 ? subListPool.remove(size - 1) : new SubList<E>(this, fromIndex, toIndex);
  }

  public int partitionSize() {
    return partitionSize;
  }

  @Override
  protected void growCapacity(long capacity) {
    long partitions = (capacity + partitionSize - 1) / partitionSize + 1;
    try {
      while (this.partitions.size() < partitions)
        this.partitions.add(createPartition(this.partitions.size()));
    } catch (IOException e) {
      throw new IllegalStateException("Unable to grow collection", e);
    }
  }

  public HugePartition partitionFor(long index) {
    final int n = (int) (index / partitionSize);
    if (n >= partitions.size())
      growCapacity(index);
    return partitions.get(n);
  }

  protected abstract HugePartition createPartition(int partitionNumber) throws IOException;

  public void subListPoolAdd(SubList<E> es) {
    subListPool.add(es);
  }
}
