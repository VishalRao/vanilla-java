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

public interface HugeCollection<E> extends HugeContainer, Collection<E>, HugeIterable<E>, Recycleable {
  /**
   * @return the base type of each element.
   */
  Class<E> elementType();

  /**
   * @param predicate to filter on
   * @return a collection of elements which match
   */
  HugeCollection<E> filter(Predicate<E> predicate);

  /**
   * Process each element and return a collection of results.
   *
   * @param procedure to apply to each element.
   * @return a collection of all the results.
   * @since Not implemented yet.
   */
  <T> HugeCollection<T> forEach(Procedure<E, T> procedure);

  /**
   * Process each element and return a collection of results.
   *
   * @param predicate matching elements to process
   * @param procedure to apply to each matched element.
   * @return a collection of all the results.
   * @since Not implemented yet.
   */
  <T> HugeCollection<T> forEach(Predicate<E> predicate, Procedure<E, T> procedure);

  /**
   * Apply a check to any number of elements.
   *
   * @param updater to transform the element.
   * @return number of elements changed.
   */
  long update(Updater<E> updater);

  /**
   * Apply a check to any number of elements.
   *
   * @param predicate elements to match
   * @param updater   to transform the matching element.
   * @return number of elements changed.
   */
  long update(Predicate<E> predicate, Updater<E> updater);
}
