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

package vanilla.java.collections.api;

import java.util.Collection;
import java.util.List;

public interface HugeList<E> extends HugeCollection<E>, List<E> {
  void add(long index, E element);

  boolean addAll(long index, Collection<? extends E> c);

  E get(long index);

  @Override
  HugeListIterator<E> listIterator();

  @Override
  HugeListIterator<E> listIterator(int index);

  HugeListIterator<E> listIterator(long index);

  HugeListIterator<E> listIterator(long start, long end);

  long longIndexOf(Object o);

  long longLastIndexOf(Object o);

  E remove(long index);

  E set(long index, E element);

  HugeList<E> subList(long fromIndex, long toIndex);

  boolean update(long index, Updater<E> updater);
}
