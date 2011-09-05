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

import vanilla.java.collections.api.HugeListIterator;
import vanilla.java.collections.api.impl.Copyable;
import vanilla.java.collections.api.impl.HugeElement;

public class VanillaHugeListIterator<E> implements HugeListIterator<E> {
  private final AbstractHugeArrayList<E> list;
  private final E pointer;
  private long end;

  public VanillaHugeListIterator(AbstractHugeArrayList<E> list, E pointer) {
    this.list = list;
    this.pointer = pointer;
    index(-1);
  }

  @Override
  public void add(E e) {
    list.add(index(), e);
  }

  @Override
  public long longNextIndex() {
    return index() + 1;
  }

  @Override
  public long index() {
    return ((HugeElement) pointer).index();
  }

  @Override
  public long longPreviousIndex() {
    return index() - 1;
  }

  public void end(long end) {
    this.end = end;
  }

  @Override
  public HugeElement nextElement() {
    return (HugeElement) next();
  }

  @Override
  public void index(long index) {
    ((HugeElement) pointer).index(index);
  }

  @Override
  public boolean hasPrevious() {
    return index() > 0;
  }

  @Override
  public E previous() {
    index(index() - 1);
    return pointer;
  }

  @Override
  public int nextIndex() {
    long index = index() + 1;
    if (index > Integer.MAX_VALUE)
      throw new IllegalStateException("Index too large for this method, use longNextIndex()");
    return (int) index;
  }

  @Override
  public int previousIndex() {
    long index = index() - 1;
    if (index > Integer.MAX_VALUE)
      throw new IllegalStateException("Index too large for this method, use longPreviousIndex()");
    return (int) index;
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public void set(E e) {
    ((Copyable<E>) pointer).copyFrom(e);
  }

  @Override
  public boolean hasNext() {
    return index() + 1 < end;
  }

  @Override
  public E next() {
    index(index() + 1);
    return pointer;
  }

  @Override
  public void remove() {
    list.remove(index());
  }

  @Override
  public void recycle() {
    list.recycle(this);
  }
}
