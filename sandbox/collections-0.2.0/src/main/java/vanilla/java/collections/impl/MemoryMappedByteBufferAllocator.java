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
    final int capacity = partitionSize * elementSize;
    final RandomAccessFile raf = new RandomAccessFile(new File(baseDirectory, type + "-" + num), "rw");

    final MappedByteBuffer buffer = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, capacity);
    this.lastBuffer = buffer;
    files.put(raf, buffer);
    return new Cleaner() {
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
      final MappedByteBuffer buffer = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 8 * MemoryMappedSizeHolder.LONGS);
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
    static final int CAPACITY = 1;
    static final int PARTITION_SIZE = 2;
    static final int LONGS = 3;

    private final MappedByteBuffer buffer;
    private final LongBuffer sizes;
    private final RandomAccessFile raf;

    public MemoryMappedSizeHolder(MappedByteBuffer buffer, RandomAccessFile raf) {
      this.buffer = buffer;
      this.sizes = buffer.order(ByteOrder.nativeOrder()).asLongBuffer();
      this.raf = raf;
    }

    @Override
    public void size(long size) {
      sizes.put(SIZE, size);
    }

    @Override
    public long size() {
      return sizes.get(SIZE);
    }

    @Override
    public void capacity(long capacity) {
      sizes.put(CAPACITY, capacity);
    }

    @Override
    public long capacity() {
      return sizes.get(CAPACITY);
    }

    @Override
    public void partitionSize(long partitionSize) {
      sizes.put(PARTITION_SIZE, partitionSize);
    }

    @Override
    public long partitionSize() {
      return sizes.get(PARTITION_SIZE);
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
