package vanilla.java.collections.impl;

import vanilla.java.collections.api.*;
import vanilla.java.collections.util.NullReadWriteLock;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.RandomAccess;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;

public abstract class AbstractHugeCollection<E> extends AbstractHugeContainer implements HugeCollection<E>, RandomAccess {
  private final Class<E> elementType;

  protected AbstractHugeCollection(Class<E> elementType) {
    this.elementType = elementType;
  }

  public boolean add(E e) {
    final long size = longSize();
    setSize(size + 1);
    set(size, e);
    return true;
  }

  public void add(int index, E element) {
    add((long) index, element);
  }

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

  public boolean addAll(int index, Collection<? extends E> c) {
    return addAll((long) index, c);
  }

  public boolean addAll(long index, Collection<? extends E> c) {
    if (index != longSize() - 1) throw new UnsupportedOperationException("Can only add to the end.");
    return addAll(c);
  }

  @Override
  public void close() throws IOException {
  }

  @Override
  public void compact() {
  }

  @Override
  public boolean contains(Object o) {
    HugeIterator<E> iter = null;
    try {
      iter = iterator();
      while (iter.hasNext()) {
        final E next = iter.next();
        if (next.equals(o))
          return true;
      }
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
        final E next = iter.next();
        set.remove(next);
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
  public <T> HugeCollection<T> forEach(Procedure<E, T> etProcedure) {
    throw new Error("Not implemented");
  }

  @Override
  public <T> HugeCollection<T> forEach(Predicate<E> predicate, Procedure<E, T> etProcedure) {
    throw new Error("Not implemented");
  }

  public int indexOf(Object o) {
    HugeIterator<E> iter = null;
    try {
      iter = iterator();
      while (iter.hasNext()) {
        final E next = iter.next();
        if (next.equals(o))
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

  public int lastIndexOf(Object o) {
    long n = longLastIndexOf(o);
    if (n >= Integer.MAX_VALUE)
      throw new IllegalStateException("The last location of " + o + " was at " + n + " use longLastIndexOf()");
    return (int) n;
  }

  public HugeListIterator<E> listIterator() {
    return listIterator(0L);
  }

  public HugeListIterator<E> listIterator(int index) {
    return listIterator(index, longSize());
  }

  public HugeListIterator<E> listIterator(long index) {
    return listIterator(index, longSize());
  }

  public abstract HugeListIterator<E> listIterator(long start, long end);

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
  public boolean removeAll(Collection<?> c) {
    boolean b = false;
    for (Object o : c) {
      b |= remove(o);
    }
    return b;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    Set set = new HashSet(c);
    boolean b = false;
    HugeIterator<E> iter = null;
    try {
      iter = iterator();
      while (iter.hasNext()) {
        if (set.contains(iter.next())) continue;
        iter.remove();
      }
      return b;
    } finally {
      recycle(iter);
    }
  }

  @Override
  public Object[] toArray() {
    return toArray((Object[]) Array.newInstance(elementType, size()));
  }

  @Override
  public <T> T[] toArray(T[] a) {
    int size = size();
    if (a.length != size)
      a = (T[]) Array.newInstance(elementType, size);
    HugeIterator<E> iter = null;
    try {
      iter = iterator();
      while (iter.hasNext())
        a[((int) iter.index())] = (T) iter.next();
      return a;
    } finally {
      recycle(iter);
    }
  }

  public boolean update(long index, Updater<E> updater) {
    E e = get(index);
    try {
      return updater.update(e);
    } finally {
      recycle(e);
    }
  }

  public E get(int index) {
    return get((long) index);
  }

  protected abstract E get(long index);

  public E remove(int index) {
    return remove((long) index);
  }

  public abstract E remove(long index);

  public E set(int index, E element) {
    return set((long) index, element);
  }

  public abstract E set(long index, E element);

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
}
