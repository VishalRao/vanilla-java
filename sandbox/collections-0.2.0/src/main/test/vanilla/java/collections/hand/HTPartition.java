package vanilla.java.collections.hand;

import vanilla.java.collections.api.impl.ByteBufferAllocator;
import vanilla.java.collections.api.impl.Cleaner;
import vanilla.java.collections.api.impl.FieldModel;
import vanilla.java.collections.api.impl.HugePartition;

import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.locks.ReadWriteLock;

public class HTPartition implements HugePartition {
  private final FieldModel<String> textModel;
  private final HTArrayList list;
  private final Cleaner reserved;
  private final IntBuffer intBuffer;
  private final CharBuffer textBuffer;

  public HTPartition(HTArrayList list, ByteBufferAllocator allocator) {
    textModel = null;
    this.list = list;
    final int partitionSize = list.partitionSize();
    reserved = allocator.reserve(partitionSize, 6);
    intBuffer = allocator.acquireIntBuffer();
    textBuffer = allocator.acquireCharBuffer();
    allocator.endOfReserve();
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
    throw new Error("NI");
  }

  public String getText(int offset) {
    throw new Error("NI");
  }
}
