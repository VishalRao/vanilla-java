package vanilla.java.collections.api;

import java.util.concurrent.ConcurrentMap;

public interface HugeMap<K, V> extends HugeContainer, ConcurrentMap<K, V> {
  @Override
  HugeSet<K> keySet();

  @Override
  HugeCollection<V> values();

  @Override
  HugeSet<Entry<K, V>> entrySet();

  HugeCollection<HugeEntry<K, V>> filter(Predicate<HugeEntry<K, V>> predicate);

  <T> HugeCollection<T> forEach(Procedure<HugeEntry<K, V>, T> procedure);

  int update(Updater<HugeEntry<K, V>> updater);

  Class<K> keyType();

  Class<V> valueType();
}
