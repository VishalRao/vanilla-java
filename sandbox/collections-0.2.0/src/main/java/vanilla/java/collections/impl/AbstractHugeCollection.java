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

  @Override
  public abstract boolean add(E e);

  @Override
  public boolean addAll(Collection<? extends E> c) {
    boolean b = false;
    for (E e : c) {
      b |= add(e);
    }
    return b;
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
  public <T> HugeCollection<T> forEach(Procedure<E, T> etProcedure) {
    throw new Error("Not implemented");
  }

  @Override
  public <T> HugeCollection<T> forEach(Predicate<E> predicate, Procedure<E, T> etProcedure) {
    throw new Error("Not implemented");
  }

  @Override
  public abstract HugeIterator<E> iterator();

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

  @Override
  public long update(Predicate<E> predicate, Updater<E> updater) {
    long count = 0;
    HugeIterator<E> iter = null;
    try {
      iter = iterator();
      while (iter.hasNext()) {
        final E e = iter.next();
        if (predicate.test(e) && updater.update(e)) count++;
      }
      return count;
    } finally {
      recycle(iter);
    }
  }

  @Override
  public long update(Updater<E> updater) {
    long count = 0;
    HugeIterator<E> iter = null;
    try {
      iter = iterator();
      while (iter.hasNext()) {
        if (updater.update(iter.next())) count++;
      }
      return count;
    } finally {
      recycle(iter);
    }
  }
}
