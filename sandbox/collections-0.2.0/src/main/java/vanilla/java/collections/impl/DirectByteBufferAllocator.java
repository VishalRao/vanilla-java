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

import sun.nio.ch.DirectBuffer;
import vanilla.java.collections.api.impl.ByteBufferAllocator;
import vanilla.java.collections.api.impl.Cleaner;
import vanilla.java.collections.api.impl.SizeHolder;

import java.io.File;
import java.io.IOException;
import java.nio.*;

public class DirectByteBufferAllocator implements ByteBufferAllocator {
  private ByteBuffer buffer;
  private int partitionSize;

  @Override
  public Cleaner reserve(int partitionSize, int elementSize, String type, int num) {
    this.partitionSize = partitionSize;
    final ByteBuffer buffer = this.buffer = ByteBuffer.allocateDirect(partitionSize * elementSize);
    return new Cleaner() {
      @Override
      public void clean() {
        ((DirectBuffer) buffer).cleaner().clean();
      }
    };
  }

  private ByteBuffer acquire(int size) {
    buffer.limit(buffer.position() + size);
    ByteBuffer bb = buffer.slice().order(ByteOrder.nativeOrder());
    buffer.position(buffer.limit());
    return bb;
  }

  @Override
  public ByteBuffer acquireBooleanBuffer() {
    return acquire((partitionSize + 7) / 8);
  }

  @Override
  public ByteBuffer acquireByteBuffer() {
    return acquire(partitionSize);
  }

  @Override
  public CharBuffer acquireCharBuffer() {
    return acquire(partitionSize * 2).asCharBuffer();
  }

  @Override
  public ShortBuffer acquireShortBuffer() {
    return acquire(partitionSize * 2).asShortBuffer();
  }

  @Override
  public IntBuffer acquireIntBuffer() {
    return acquire(partitionSize * 4).asIntBuffer();
  }

  @Override
  public FloatBuffer acquireFloatBuffer() {
    return acquire(partitionSize * 4).asFloatBuffer();
  }

  @Override
  public LongBuffer acquireLongBuffer() {
    return acquire(partitionSize * 8).asLongBuffer();
  }

  @Override
  public DoubleBuffer acquireDoubleBuffer() {
    return acquire(partitionSize * 8).asDoubleBuffer();
  }

  @Override
  public void endOfReserve() {
    // blow up if behaviour is not correct.
    buffer = null;
  }

  @Override
  public void close() throws IOException {
  }

  @Override
  public void flush() throws IOException {
  }

  @Override
  public SizeHolder sizeHolder() {
    return new VanillaSizeHolder();
  }

  @Override
  public File baseDirectory() {
    return null;
  }
}
