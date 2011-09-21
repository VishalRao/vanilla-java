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
import vanilla.java.collections.api.impl.*;
import vanilla.java.collections.util.NullReadWriteLock;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

public abstract class AbstractHugeHashMap<K, V> extends AbstractHugeContainer implements HugeMap<K, V> {
  private static final long KEY_HASH = Long.MAX_VALUE;
  protected final List<HugePartition> partitions = new ArrayList<HugePartition>();
  protected final IntBuffer[] hashKeys;
  protected final Cleaner[] hashKeyCleaners;
  protected final ByteBufferAllocator allocator;
  private final Class<K> keyType;
  private final Class<V> valueType;

  public AbstractHugeHashMap(int partitionSize, Class<K> keyType, Class<V> valueType, ByteBufferAllocator allocator) {
    super(allocator.sizeHolder());
    this.keyType = keyType;
    this.valueType = valueType;
    this.allocator = allocator;
    if (size.partitionSize() <= 0) {
      size.partitionSize(partitionSize);
    }
    int keyPartitions = size.keyPartitions();
    if (keyPartitions <= 0) {
      keyPartitions = 1;
      while (keyPartitions * keyPartitions * 1024L < partitionSize) keyPartitions <<= 1;
      size.keyPartitions(keyPartitions);
    }
    try {
      hashKeys = new IntBuffer[keyPartitions];
      hashKeyCleaners = new Cleaner[keyPartitions];
      for (int i = 0; i < keyPartitions; i++) {
        hashKeyCleaners[i] = allocator.reserve(1024, 4, "index", i);
        hashKeys[i] = allocator.acquireIntBuffer();
        allocator.endOfReserve();
      }
      size.flush();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void compact() {
  }

  @Override
  protected void growCapacity(long capacity) {
    final int partitionSize = partitionSize();
    long partitions = (capacity + partitionSize - 1) / partitionSize;
    try {
//      System.out.println("Adding partition " + partitions + " for " + capacity);
      while (this.partitions.size() < partitions)
        this.partitions.add(createPartition(this.partitions.size()));

      final long minCapacity = partitions * partitionSize;
      if (minCapacity > this.size.capacity())
        this.size.capacity(minCapacity);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to grow collection", e);
    }
  }

  public HugePartition partitionFor(long index) {
    final int n = (int) (index / partitionSize());
    if (n >= partitions.size())
      growCapacity(index + 1);
    return partitions.get(n);
  }

  protected abstract HugePartition createPartition(int partitionNumber) throws IOException;

  @Override
  public ReadWriteLock lock() {
    return NullReadWriteLock.INSTANCE;
  }

  @Override
  public long longHashCode() {
    throw new Error("Not implemented");
  }

  @Override
  public void recycle(Object recycleable) {
  }

  @Override
  public boolean containsKey(Object key) {
    return keyType.isInstance(key) && indexOf((K) key, false, false) < 0;

  }

  @Override
  public boolean containsValue(Object value) {
    throw new Error("Not implemented");
  }

  @Override
  public V get(Object key) {
    if (!keyType.isInstance(key)) return null;
    long index = indexOf((K) key, false, false);
    if (index < 0) return null;
    return get(index);
  }

  @Override
  public V put(K key, V value) {
    long index = indexOf(key, true, false);
    V v = get(index);
    copyValue(v, value);
    return null;
  }

  protected abstract void copyValue(V v, V value);

  @Override
  public V remove(Object key) {
    if (!keyType.isInstance(key)) return null;
    long index = indexOf((K) key, false, true);
    V v = get(index);
    V ret = v == null ? null : ((Copyable<V>) v).copyOf();
    recycle(v);
    return ret;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public HugeSet<K> keySet() {
    throw new Error("Not implemented");
  }

  @Override
  public HugeCollection<V> values() {
    throw new Error("Not implemented");
  }

  @Override
  public HugeSet<Entry<K, V>> entrySet() {
    throw new Error("Not implemented");
  }

  @Override
  public HugeCollection<HugeEntry<K, V>> filter(Predicate<HugeEntry<K, V>> predicate) {
    throw new Error("Not implemented");
  }

  @Override
  public <T> HugeCollection<T> forEach(Procedure<HugeEntry<K, V>, T> procedure) {
    throw new Error("Not implemented");
  }

  @Override
  public int update(Updater<HugeEntry<K, V>> updater) {
    throw new Error("Not implemented");
  }

  @Override
  public Class<K> keyType() {
    return keyType;
  }

  @Override
  public Class<V> valueType() {
    return valueType;
  }

  @Override
  public V putIfAbsent(K key, V value) {
    throw new Error("Not implemented");
  }

  @Override
  public boolean remove(Object key, Object value) {
    throw new Error("Not implemented");
  }

  @Override
  public boolean replace(K key, V oldValue, V newValue) {
    throw new Error("Not implemented");
  }

  @Override
  public V replace(K key, V value) {
    throw new Error("Not implemented");
  }

  //// implementation details.
  protected abstract long longHashCodeFor(K key);

  // Map
  protected long indexOf(K key, boolean free, boolean remove) {
    long hash = longHashCodeFor(key) & KEY_HASH;
    int loHash = (int) (hash % size.keyPartitions());
    int hiHash = (int) (hash / size.keyPartitions());
    final IntBuffer keysBuffer = this.hashKeys[loHash];

    K ke = acquireKeyElement(0);
    try {
      for (int i = 0, len = keysBuffer.limit(); i < len; i++) {
        final int i1 = keysBuffer.get((hiHash + i) % len);
        if (i1 == 0) {
          if (free) {
            int used = keysBuffer.position();
            final long loc = longSize();
            if (loc >= Integer.MAX_VALUE) throw new Error("long size not implemented.");
            ensureCapacity(loc + 1);
            keysBuffer.put((hiHash + i) % len, (int) (loc + 1));

            if (used > keysBuffer.limit() / 2)
              growBuffer(loHash);
            else
              keysBuffer.position(used + 1);
            size.addToSize(1);
            return loc;
          }
          return -1;
        }
        ((HugeElement) ke).index(i1 - 1);
        if (ke.equals(key)) {
          if (remove) {
            keysBuffer.put((hiHash + i) % len, 0);
            // used field.
            keysBuffer.position(keysBuffer.position() - 1);
          }
          return i1 - 1;
        }
      }
      return -1;
    } finally {
      recycle(ke);
    }
  }

  protected abstract K acquireKeyElement(long index);

  protected abstract void growBuffer(int loHash);


  protected abstract void ensureCapacity(long i);

}
