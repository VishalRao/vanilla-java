package vanilla.java.collections.api;

import java.util.Map;

public interface HugeEntry<K, V> extends Map.Entry<K, V> {
  long index();
}
