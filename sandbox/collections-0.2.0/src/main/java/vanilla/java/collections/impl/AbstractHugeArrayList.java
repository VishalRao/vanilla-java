package vanilla.java.collections.impl;

import vanilla.java.collections.api.*;
import vanilla.java.collections.api.impl.ByteBufferAllocator;
import vanilla.java.collections.api.impl.Copyable;
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
  private final List<E> implPool = new ArrayList<E>();

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
  }

  @Override
  public void clear() {
    super.clear();
    for (HugePartition partition : partitions) {
      partition.clear();
    }
  }

  @Override
  public void compact() {
    for (HugePartition partition : partitions) {
      partition.compact();
    }
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
    return listIterator();
  }

  @Override
  public int lastIndexOf(Object o) {
    long n = longLastIndexOf(o);
    if (n >= Integer.MAX_VALUE)
      throw new IllegalStateException("The last location of " + o + " was at " + n + " use longLastIndexOf()");
    return (int) n;
  }

  @Override
  public HugeListIterator<E> listIterator() {
    return listIterator(0L);
  }

  @Override
  public HugeListIterator<E> listIterator(int index) {
    final int size = iteratorPool.size();
    return size > 0 ? iteratorPool.remove(iteratorPool.size() - 1) : new VanillaHugeListIterator<E>(this, get(0));
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
          return iter.index();
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
          return iter.index();
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
    return remove((long) index);
  }

  @Override
  public E remove(long index) {
    if (index + 1 != size()) throw new IllegalStateException("Can only remove the last entry");
    return ((Copyable<E>) get(--size)).copyOf();
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
    return set((long) index, element);
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
    if (a.length != size())
      a = (T[]) Array.newInstance(a.getClass().getComponentType(), size());

    HugeIterator<E> iter = null;
    try {
      iter = iterator();
      while (iter.hasNext() && iter.index() < a.length) {
        a[((int) iter.index())] = ((Copyable<T>) iter.next()).copyOf();
      }
      return a;
    } finally {
      recycle(iter);
    }
  }

  @Override
  public boolean update(long index, Updater<E> updater) {
    E e = get(index);
    try {
      return updater.update(e);
    } finally {
      recycle(e);
    }
  }

  @Override
  public long update(Predicate<E> predicate, Updater<E> updater) {
    HugeIterator<E> iter = null;
    long count = 0;
    try {
      iter = iterator();
      while (iter.hasNext()) {
        E e = iter.next();
        if (predicate.test(e) && updater.update(e))
          count++;
      }
      return count;
    } finally {
      recycle(iter);
    }
  }

  @Override
  public long update(Updater<E> updater) {
    HugeIterator<E> iter = null;
    long count = 0;
    try {
      iter = iterator();
      while (iter.hasNext()) {
        E e = iter.next();
        if (updater.update(e))
          count++;
      }
      return count;
    } finally {
      recycle(iter);
    }
  }

  public int partitionSize() {
    return partitionSize;
  }

  @Override
  protected void growCapacity(long capacity) {
    long partitions = (capacity + partitionSize - 1) / partitionSize + 1;
    while (this.partitions.size() < partitions)
      this.partitions.add(createPartition());
  }

  public HugePartition partitionFor(long index) {
    final int n = (int) (index / partitionSize);
    if (n >= partitions.size())
      growCapacity(index);
    return partitions.get(n);
  }

  protected abstract HugePartition createPartition();
}
