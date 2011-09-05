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

import vanilla.java.collections.api.HugeListIterator;
import vanilla.java.collections.api.impl.ByteBufferAllocator;
import vanilla.java.collections.impl.AbstractHugeArrayList;
import vanilla.java.collections.impl.VanillaHugeListIterator;

import java.io.IOException;

class HTArrayList extends AbstractHugeArrayList<HT> {
  public HTArrayList(int partitionSize, Class<HT> elementType, ByteBufferAllocator allocator) {
    super(partitionSize, elementType, allocator);
  }

  protected HTPartition createPartition(int partitionNumber) throws IOException {
    return new HTPartition(this, allocator, partitionNumber);
  }

  @Override
  protected HT createPointer() {
    return new HTPointer(this);
  }

  @Override
  protected HT createImpl() {
    return new HTImpl();
  }

  @Override
  protected HugeListIterator<HT> createIterator() {
    return new VanillaHugeListIterator<HT>(this, createPointer());
  }

  public HTPartition partitionFor(long index) {
    return (HTPartition) super.partitionFor(index);
  }

  public void pointerPoolAdd(HTPointer htPointer) {
    pointerPool.add(htPointer);
  }

  public void implPoolAdd(HTImpl htImpl) {
    implPool.add(htImpl);
  }

  @Override
  public void recycle(Object recycleable) {
    if (recycleable instanceof HTPointer)
      pointerPoolAdd((HTPointer) recycleable);
    else if (recycleable instanceof HTImpl)
      implPoolAdd((HTImpl) recycleable);
    else
      super.recycle(recycleable);
  }
}
