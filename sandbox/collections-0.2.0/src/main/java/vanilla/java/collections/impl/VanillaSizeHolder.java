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

public class VanillaSizeHolder extends SimpleSizeHolder {
  private long size;
  private long capacity;
  private int partitionSize;

  @Override
  public void size(long size) {
    this.size = size;
  }

  @Override
  public long size() {
    return size;
  }

  @Override
  public void capacity(long capacity) {
    this.capacity = capacity;
  }

  @Override
  public long capacity() {
    return capacity;
  }

  public void partitionSize(int partitionSize) {
    this.partitionSize = partitionSize;
  }

  public int partitionSize() {
    return partitionSize;
  }
}
