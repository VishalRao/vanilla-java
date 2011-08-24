package vanilla.java.collections.impl;

/*
 * Copyright 2011 Peter Lawrey
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import vanilla.java.collections.api.HugeAllocation;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHugeContainer<T, TA extends HugeAllocation> {
  protected final int allocationSize;
  protected final List<TA> allocations = new ArrayList<TA>();
  protected long longSize;

  public AbstractHugeContainer(int allocationSize) {
    this.allocationSize = allocationSize;
  }

  public void ensureCapacity(long size) {
    long blocks = (size + allocationSize - 1) / allocationSize;
    while (blocks > allocations.size()) {
      allocations.add(createAllocation());
    }
  }

  protected abstract TA createAllocation();


  public int size() {
    return longSize < Integer.MAX_VALUE ? (int) longSize : Integer.MAX_VALUE;
  }

  public long longSize() {
    return longSize;
  }

  public boolean isEmpty() {
    return longSize == 0;
  }


  public void setSize(long length) {
    ensureCapacity(length);
    longSize = length;
  }

  public TA getAllocation(long index) {
    return allocations.get((int) (index / allocationSize));
  }

  public void clear() {
    for (TA allocation : allocations) {
      allocation.clear();
    }
    longSize = 0;
  }

  public void compact() {
    int allocationsNeeded = (int) (longSize() / allocationSize + 1);
    while (allocations.size() > allocationsNeeded) {
      allocations.remove(allocations.size() - 1).destroy();
    }
    compactStart();
    for (int i = 0, allocationsSize = allocations.size(); i < allocationsSize; i++) {
      compactOnAllocation(allocations.get(i), Math.min(longSize - i * allocationsSize, allocationSize));
    }
    compactEnd();
  }

  protected abstract void compactStart();

  protected abstract void compactOnAllocation(TA ta, long i);

  protected abstract void compactEnd();

}
