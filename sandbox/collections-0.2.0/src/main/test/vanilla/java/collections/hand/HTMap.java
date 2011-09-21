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

package vanilla.java.collections.hand;

import vanilla.java.collections.api.impl.ByteBufferAllocator;
import vanilla.java.collections.api.impl.HugePartition;
import vanilla.java.collections.impl.AbstractHugeHashMap;

import java.io.IOException;

public class HTMap extends AbstractHugeHashMap<HTKey, HT> {
  public HTMap(int partitionSize, Class<HTKey> keyType, Class<HT> valueType, ByteBufferAllocator allocator) {
    super(partitionSize, keyType, valueType, allocator);
  }

  @Override
  protected HugePartition createPartition(int partitionNumber) throws IOException {
    throw new Error("Not implemented");
  }

  @Override
  protected void copyValue(HT ht, HT value) {
    throw new Error("Not implemented");
  }

  @Override
  protected long longHashCodeFor(HTKey key) {
    throw new Error("Not implemented");
  }

  @Override
  protected HTKey acquireKeyElement(long index) {
    throw new Error("Not implemented");
  }

  @Override
  protected void growBuffer(int loHash) {
    throw new Error("Not implemented");
  }

  @Override
  protected void ensureCapacity(long i) {
    throw new Error("Not implemented");
  }
}
