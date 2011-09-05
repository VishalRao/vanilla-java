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

import java.util.SortedSet;

public interface HugeSortedSet<E> extends HugeSet<E>, SortedSet<E> {
  @Override
  HugeSortedSet<E> subSet(E fromElement, E toElement);

  @Override
  HugeSortedSet<E> headSet(E toElement);

  @Override
  HugeSortedSet<E> tailSet(E fromElement);
}
