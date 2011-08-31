package vanilla.java.collections.impl;

import vanilla.java.collections.api.*;
import vanilla.java.collections.api.impl.ByteBufferAllocator;
import vanilla.java.collections.api.impl.HugeElement;
import vanilla.java.collections.api.impl.HugePartition;
import vanilla.java.collections.util.NullReadWriteLock;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;

public abstract class AbstractHugeArrayList<E> extends AbstractHugeContainer implements HugeList<E>, RandomAccess {
  protected final List<HugePartition> partitions = new ArrayList<HugePartition>();
  protected final int partitionSize;
  protected final ByteBufferAllocator allocator;
  private final Class<E> elementType;
  private final List<E> pointerPool = new ArrayList<E>();
  private final List<HugeListIterator<E>> iteratorPool = new ArrayList<HugeListIterator<E>>();

  protected AbstractHugeArrayList(int partitionSize, Class<E> elementType, ByteBufferAllocator allocator) {
    this.partitionSize = partitionSize;
    this.elementType = elementType;
    this.allocator = allocator;
  }

  @Override
  public boolean add(E e) {
    final long size = longSize();
    setSize(size + 1);
    set(size, e);
    return true;
  }

  @Override
  public void add(int index, E element) {
    add((long) index, element);
  }

  @Override
  public void add(long index, E element) {
    if (index != longSize() - 1) throw new UnsupportedOperationException("Can only add to the end.");
    add(element);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    boolean b = false;
    for (E e : c) {
      b |= add(e);
    }
    return b;
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    return addAll((long) index, c);
  }

  @Override
  public boolean addAll(long index, Collection<? extends E> c) {
    if (index != longSize() - 1) throw new UnsupportedOperationException("Can only add to the end.");
    return addAll(c);
  }

  @Override
  public void close() throws IOException {
    throw new Error("Not implemented");
  }

  @Override
  public void compact() {
    throw new Error("Not implemented");
  }

  @Override
  public boolean contains(Object o) {
    HugeIterator<E> iter = null;
    try {
      iter = iterator();
      while (iter.hasNext())
        if (iter.next().equals(o)) return true;
      return false;
    } finally {
      recycle(iter);
    }
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    Set set = new HashSet(c);
    if (set.isEmpty()) return true;
    HugeIterator<E> iter = null;
    try {
      iter = iterator();
      while (iter.hasNext()) {
        set.remove(iter.next());
        if (set.isEmpty()) return true;
      }
      return false;
    } finally {
      recycle(iter);
    }
  }

  @Override
  public Class<E> elementType() {
    return elementType;
  }

  @Override
  public HugeCollection<E> filter(Predicate<E> predicate) {
    throw new Error("Not implemented");
  }

  @Override
  public void flush() throws IOException {
    throw new Error("Not implemented");
  }

  @Override
  public <T> HugeCollection<T> forEach(Predicate<E> predicate, Procedure<E, T> etProcedure) {
    throw new Error("Not implemented");
  }

  @Override
  public <T> HugeCollection<T> forEach(Procedure<E, T> etProcedure) {
    throw new Error("Not implemented");
  }

  @Override
  public E get(int index) {
    return get((long) index);
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
  public int indexOf(Object o) {
    HugeIterator<E> iter = null;
    try {
      iter = iterator();
      while (iter.hasNext()) {
        if (iter.next().equals(o))
          return (int) iter.index();
        if (iter.index() >= Integer.MAX_VALUE)
          return -1;
      }
      return -1;
    } finally {
      recycle(iter);
    }
  }

  @Override
  public HugeIterator<E> iterator() {
    throw new Error("Not implemented");
  }

  @Override
  public int lastIndexOf(Object o) {
    throw new Error("Not implemented");
  }

  @Override
  public HugeListIterator<E> listIterator() {
    throw new Error("Not implemented");
  }

  @Override
  public HugeListIterator<E> listIterator(int index) {
    throw new Error("Not implemented");
  }

  @Override
  public HugeListIterator<E> listIterator(long index) {
    final int size = iteratorPool.size();
    HugeListIterator<E> e = size > 0 ? iteratorPool.remove(size - 1) : createIterator();
    e.index(index);
    return e;
  }

  protected abstract HugeListIterator<E> createIterator();

  @Override
  public ReadWriteLock lock() {
    return NullReadWriteLock.INSTANCE; // there is no lock by default.
  }

  @Override
  public long longHashCode() {
    HugeIterator<E> iter = null;
    long hashCode = 0;
    try {
      iter = iterator();
      while (iter.hasNext()) {
        hashCode ^= iter.nextElement().longHashCode();
      }
      return hashCode;
    } finally {
      recycle(iter);
    }
  }

  @Override
  public long longIndexOf(Object o) {
    HugeListIterator<E> iter = null;
    try {
      iter = listIterator(0);
      while (iter.hasNext()) {
        if (iter.next().equals(o))
          return iter.longIndex();
      }
      return -1;
    } finally {
      recycle(iter);
    }
  }

  @Override
  public long longLastIndexOf(Object o) {
    HugeListIterator<E> iter = null;
    try {
      iter = listIterator(longSize() - 1);
      while (iter.hasPrevious()) {
        if (iter.previous().equals(o))
          return iter.longIndex();
      }
      return -1;
    } finally {
      recycle(iter);
    }
  }

  @Override
  public void recycle() {
  }

  @Override
  public void recycle(Object recycleable) {
  }

  @Override
  public E remove(int index) {
    throw new Error("Not implemented");
  }

  @Override
  public E remove(long index) {
    throw new Error("Not implemented");
  }

  @Override
  public boolean remove(Object o) {
    throw new Error("Not implemented");
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new Error("Not implemented");
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new Error("Not implemented");
  }

  @Override
  public E set(int index, E element) {
    throw new Error("Not implemented");
  }

  @Override
  public E set(long index, E element) {
    throw new Error("Not implemented");
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    throw new Error("Not implemented");
  }

  @Override
  public HugeList<E> subList(long fromIndex, long toIndex) {
    throw new Error("Not implemented");
  }

  @Override
  public Object[] toArray() {
    return toArray((Object[]) Array.newInstance(elementType, size()));
  }

  @Override
  public <T> T[] toArray(T[] a) {
    throw new Error("Not implemented");
  }

  @Override
  public boolean update(long index, Updater<E> updater) {
    throw new Error("Not implemented");
  }

  @Override
  public long update(Predicate<E> predicate, Updater<E> updater) {
    throw new Error("Not implemented");
  }

  @Override
  public long update(Updater<E> updater) {
    throw new Error("Not implemented");
  }

  public int partitionSize() {
    return partitionSize;
  }
}
