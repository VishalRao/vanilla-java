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

import java.util.NavigableMap;

public interface HugeNavigableMap<K, V> extends NavigableMap<K, V>, HugeSortedMap<K, V> {
  @Override
  HugeEntry<K, V> lowerEntry(K key);

  @Override
  HugeEntry<K, V> floorEntry(K key);

  @Override
  HugeEntry<K, V> ceilingEntry(K key);

  @Override
  HugeEntry<K, V> higherEntry(K key);

  @Override
  HugeEntry<K, V> firstEntry();

  @Override
  HugeEntry<K, V> lastEntry();

  @Override
  HugeEntry<K, V> pollFirstEntry();

  @Override
  HugeEntry<K, V> pollLastEntry();

  @Override
  HugeNavigableMap<K, V> descendingMap();

  @Override
  HugeNavigableSet<K> navigableKeySet();

  @Override
  HugeNavigableSet<K> descendingKeySet();

  @Override
  HugeNavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive);

  @Override
  HugeNavigableMap<K, V> headMap(K toKey, boolean inclusive);

  @Override
  HugeNavigableMap<K, V> tailMap(K fromKey, boolean inclusive);
}
