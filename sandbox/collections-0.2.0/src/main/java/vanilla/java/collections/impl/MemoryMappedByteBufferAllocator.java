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
import vanilla.java.collections.util.HugeCollections;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryMappedByteBufferAllocator implements ByteBufferAllocator {
  private final File baseDirectory;
  private final Map<RandomAccessFile, MappedByteBuffer> files = new ConcurrentHashMap<RandomAccessFile, MappedByteBuffer>();
  private int partitionSize;
  private MappedByteBuffer lastBuffer;

  public MemoryMappedByteBufferAllocator(File baseDirectory) {
    this.baseDirectory = baseDirectory;
    baseDirectory.mkdirs();
  }

  @Override
  public Cleaner reserve(int partitionSize, int elementSize, String type, int num) throws IOException {
    this.partitionSize = partitionSize;
    final File file = new File(baseDirectory, type + "-" + num);
    int capacity = partitionSize * elementSize;
    long fileSize = file.length();
    if (fileSize > capacity) capacity = (int) fileSize;

    final RandomAccessFile raf = new RandomAccessFile(file, "rw");

    final MappedByteBuffer buffer = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, capacity);
    this.lastBuffer = buffer;
    files.put(raf, buffer);
    return new Cleaner() {
      @Override
      public void flush() throws IOException {
        buffer.force();
      }

      @Override
      public void clean() {
        buffer.force();
        ((DirectBuffer) buffer).cleaner().clean();
        HugeCollections.close(raf);
        files.remove(raf);
      }
    };
  }

  private ByteBuffer acquire(int size) {
    lastBuffer.limit(lastBuffer.position() + size);
    ByteBuffer bb = lastBuffer.slice().order(ByteOrder.nativeOrder());
    lastBuffer.position(lastBuffer.limit());
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
    lastBuffer = null;
  }

  @Override
  public void close() throws IOException {
    for (Map.Entry<RandomAccessFile, MappedByteBuffer> entry : files.entrySet()) {
      entry.getValue().force();
      entry.getKey().close();
    }
  }

  @Override
  public void flush() throws IOException {
    for (MappedByteBuffer buffer : files.values()) {
      buffer.force();
    }
  }

  @Override
  public SizeHolder sizeHolder() {
    try {
      final RandomAccessFile raf = new RandomAccessFile(new File(baseDirectory, "size"), "rw");
      final MappedByteBuffer buffer = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, MemoryMappedSizeHolder.LENGTH);
      files.put(raf, buffer);
      return new MemoryMappedSizeHolder(buffer, raf);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to create " + new File(baseDirectory, "size"), e);
    }
  }

  @Override
  public File baseDirectory() {
    return baseDirectory;
  }

  class MemoryMappedSizeHolder implements SizeHolder {
    static final int SIZE = 0;
    static final int CAPACITY = SIZE + 8;
    static final int PARTITION_SIZE = CAPACITY + 8;
    static final int KEY_PARTITIONS = PARTITION_SIZE + 4;
    static final int LENGTH = KEY_PARTITIONS + 4;

    private final MappedByteBuffer buffer;
    private final RandomAccessFile raf;

    public MemoryMappedSizeHolder(MappedByteBuffer buffer, RandomAccessFile raf) {
      this.buffer = buffer;
      buffer.order(ByteOrder.nativeOrder());
      this.raf = raf;
    }

    @Override
    public void size(long size) {
      buffer.putLong(SIZE, size);
    }

    @Override
    public long size() {
      return buffer.getLong(SIZE);
    }

    @Override
    public void addToSize(int i) {
      buffer.putLong(SIZE, buffer.getLong(SIZE) + i);
    }

    @Override
    public void capacity(long capacity) {
      buffer.putLong(CAPACITY, capacity);
    }

    @Override
    public long capacity() {
      return buffer.getLong(CAPACITY);
    }

    @Override
    public void partitionSize(int partitionSize) {
      buffer.putInt(PARTITION_SIZE, partitionSize);
    }

    @Override
    public int partitionSize() {
      return buffer.get(PARTITION_SIZE);
    }

    @Override
    public void keyPartitions(int keyPartitions) {
      buffer.putInt(KEY_PARTITIONS, keyPartitions);
    }

    @Override
    public int keyPartitions() {
      return buffer.getInt(KEY_PARTITIONS);
    }

    @Override
    public void close() throws IOException {
      buffer.force();
      ((DirectBuffer) buffer).cleaner().clean();
      HugeCollections.close(raf);
      files.remove(raf);
    }

    @Override
    public void flush() throws IOException {
      buffer.force();
    }
  }
}
