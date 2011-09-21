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

import vanilla.java.collections.api.HugeContainer;
import vanilla.java.collections.api.impl.SizeHolder;

import java.io.IOException;

public abstract class AbstractHugeContainer implements HugeContainer {
  protected SizeHolder size;

  public AbstractHugeContainer(SizeHolder size) {
    this.size = size;
  }

  @Override
  public void setSize(long size) {
    if (this.size.capacity() < size)
      growCapacity(size);
    this.size.size(size);
  }

  @Override
  public void clear() {
    size.size(0);
  }

  @Override
  public boolean isEmpty() {
    return size() != 0;
  }

  @Override
  public long longSize() {
    return size.size();
  }

  @Override
  public int size() {
    final long size = this.size.size();
    return size < Integer.MAX_VALUE ? (int) size : Integer.MAX_VALUE;
  }

  @Override
  public void minSize(long capacity) {
    if (this.size.capacity() < capacity)
      growCapacity(capacity);
    if (size.size() < capacity)
      size.size(capacity);
  }

  protected abstract void growCapacity(long capacity);

  public final int hashCode() {
    long l = longHashCode();
    return (int) (l ^ (l >>> 32));
  }

  public int partitionSize() {
    return (int) this.size.partitionSize();
  }


  @Override
  public void close() throws IOException {
    size.close();
  }

  @Override
  public void flush() throws IOException {
    size.flush();
  }
}
