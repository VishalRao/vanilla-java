package vanilla.java.collections.api;

import java.util.SortedMap;

public interface HugeSortedMap<K, V> extends SortedMap<K, V>, HugeMap<K, V> {
  @Override
  HugeSortedMap<K, V> subMap(K fromKey, K toKey);

  @Override
  HugeSortedMap<K, V> headMap(K toKey);

  @Override
  HugeSortedMap<K, V> tailMap(K fromKey);

  @Override
  HugeSet<Entry<K, V>> entrySet();
}
