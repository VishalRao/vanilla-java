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
import vanilla.java.collections.api.impl.Cleaner;
import vanilla.java.collections.api.impl.HugePartition;
import vanilla.java.collections.impl.Enumerated16FieldModel;

import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.locks.ReadWriteLock;

class HTPartition implements HugePartition {
  private final Enumerated16FieldModel<String> textModel = new Enumerated16FieldModel<String>("text");
  private final HTArrayList list;
  private final ByteBufferAllocator allocator;
  private final int partitionNumber;
  private final Cleaner reserved;
  private final IntBuffer intBuffer;
  private final CharBuffer textBuffer;

  public HTPartition(HTArrayList list, ByteBufferAllocator allocator, int partitionNumber) throws IOException {
    this.list = list;
    this.allocator = allocator;
    this.partitionNumber = partitionNumber;
    final int partitionSize = list.partitionSize();
    reserved = allocator.reserve(partitionSize, 6, "part", partitionNumber);
    intBuffer = allocator.acquireIntBuffer();
    textBuffer = allocator.acquireCharBuffer();
    allocator.endOfReserve();

    textModel.load(allocator.baseDirectory(), partitionNumber);
  }

  @Override
  public ReadWriteLock lock() {
    return list.lock();
  }

  @Override
  public void clear() {
    textModel.clear();
    for (int i = 0; i < textBuffer.capacity(); i++)
      textBuffer.put(i, (char) 0);
  }

  @Override
  public void destroy() {
    reserved.clean();
  }

  public void setInt(int offset, int i) {
    intBuffer.put(offset, i);
  }

  public int getInt(int offset) {
    return intBuffer.get(offset);
  }

  public void setText(int offset, String id) {
    textModel.set(textBuffer, offset, id);
  }

  public String getText(int offset) {
    return textModel.get(textBuffer, offset);
  }

  public void compact() {

  }

  public void flush() {
    final File dir = allocator.baseDirectory();
    if (dir == null) return;
    textModel.save(dir, partitionNumber);
  }

  @Override
  public void close() throws IOException {
    flush();
    reserved.clean();
  }
}
