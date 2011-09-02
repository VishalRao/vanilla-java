package vanilla.java.collections.impl;

import sun.nio.ch.DirectBuffer;
import vanilla.java.collections.api.impl.ByteBufferAllocator;
import vanilla.java.collections.api.impl.Cleaner;

import java.io.IOException;
import java.nio.*;

public class DirectByteBufferAllocator implements ByteBufferAllocator {
  private ByteBuffer buffer;
  private int partitionSize;

  @Override
  public Cleaner reserve(int partitionSize, int elementSize) {
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
}
