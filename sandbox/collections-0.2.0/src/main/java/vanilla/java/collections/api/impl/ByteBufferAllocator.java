package vanilla.java.collections.api.impl;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.nio.*;

public interface ByteBufferAllocator extends Flushable, Closeable {
  Cleaner reserve(int partitionSize, int elementSize, String type, int num) throws IOException;

  ByteBuffer acquireBooleanBuffer();

  ByteBuffer acquireByteBuffer();

  CharBuffer acquireCharBuffer();

  ShortBuffer acquireShortBuffer();

  IntBuffer acquireIntBuffer();

  FloatBuffer acquireFloatBuffer();

  LongBuffer acquireLongBuffer();

  DoubleBuffer acquireDoubleBuffer();

  void endOfReserve();

  SizeHolder sizeHolder();

  File baseDirectory();
}
