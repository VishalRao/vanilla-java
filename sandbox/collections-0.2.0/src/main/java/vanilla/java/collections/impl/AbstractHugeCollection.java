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
import vanilla.java.collections.api.impl.Copyable;
import vanilla.java.collections.api.impl.SizeHolder;
import vanilla.java.collections.util.NullReadWriteLock;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;

public abstract class AbstractHugeCollection<E> extends AbstractHugeContainer implements HugeCollection<E>, RandomAccess {
  private final Class<E> elementType;
  private final Deque<SubList<E>> subListPool = new ArrayDeque<SubList<E>>();
  private final Deque<SelectedList<E>> selectedListPool = new ArrayDeque<SelectedList<E>>();

  protected AbstractHugeCollection(Class<E> elementType, SizeHolder size) {
    super(size);
    this.elementType = elementType;
  }

  public boolean add(E e) {
    final long size = longSize();
    setSize(size + 1);
    E e2 = get(size);
    ((Copyable<E>) e2).copyFrom(e);
    recycle(e2);
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
    SelectedList<E> list = acquireSelectedList();
    HugeIterator<E> iter = null;
    try {
      iter = iterator();
      while (iter.hasNext()) {
        final E next = iter.next();
        if (predicate.test(next))
          list.add(next);
      }
      return list;
    } finally {
      recycle(iter);
    }
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
    Set set = new HashSet(c);
    boolean b = false;
    HugeIterator<E> iterator = iterator();
    try {
      while (iterator.hasNext()) {
        if (set.contains(iterator.next())) {
          iterator.remove();
          b = true;
        }
      }
    } finally {
      iterator.recycle();
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

  public List<E> subList(int fromIndex, int toIndex) {
    return subList((long) fromIndex, (long) toIndex);
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

  @Override
  public boolean remove(Object o) {
    long idx = indexOf(o);
    if (idx >= 0) {
      recycle(remove(idx));
      return true;
    }
    return false;
  }

  public E set(int index, E element) {
    return set((long) index, element);
  }

  public E set(long index, E element) {
    E e = get(index);
    E i = ((Copyable<E>) e).copyOf();
    ((Copyable<E>) e).copyFrom(element);
    return i;
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

  public void subListPoolAdd(SubList<E> es) {
    subListPool.add(es);
  }

  public HugeList<E> subList(long fromIndex, long toIndex) {
    final SubList<E> subList = subListPool.poll();
    return subList == null ? new SubList<E>(this, fromIndex, toIndex) : subList;
  }

  protected SelectedList<E> acquireSelectedList() {
    final SelectedList<E> list = selectedListPool.poll();
    return list == null ? new SelectedList<E>(elementType, this) : list;
  }
}
