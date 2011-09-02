package vanilla.java.collections.hand;

import vanilla.java.collections.api.HugeListIterator;
import vanilla.java.collections.api.impl.ByteBufferAllocator;
import vanilla.java.collections.impl.AbstractHugeArrayList;

public class HTArrayList extends AbstractHugeArrayList<HT> {
  public HTArrayList(int partitionSize, Class<HT> elementType, ByteBufferAllocator allocator) {
    super(partitionSize, elementType, allocator);
  }

  protected HTPartition createPartition() {
    return new HTPartition(this, allocator);
  }

  @Override
  protected HT createPointer() {
    return new HTPointer(this);
  }

  @Override
  protected HT createImpl() {
    return new HTImpl();
  }

  @Override
  protected HugeListIterator<HT> createIterator() {
    throw new Error("Not implemented");
  }

  public HTPartition partitionFor(long index) {
    return (HTPartition) super.partitionFor(index);
  }
}
