package vanilla.java.collections.hand;

import vanilla.java.collections.api.HugeListIterator;
import vanilla.java.collections.api.impl.ByteBufferAllocator;
import vanilla.java.collections.impl.AbstractHugeArrayList;

public class HTArrayList extends AbstractHugeArrayList<HT> {
  public HTArrayList(int partitionSize, Class<HT> elementType, ByteBufferAllocator allocator) {
    super(partitionSize, elementType, allocator);
  }

  @Override
  protected void growCapacity(long capacity) {
    long partitions = (capacity + partitionSize - 1) / partitionSize;
    while (this.partitions.size() < partitions)
      this.partitions.add(new HTPartition(this, allocator));
  }

  @Override
  protected HugeListIterator<HT> createIterator() {
    throw new Error("Not implemented");
  }

  @Override
  protected HT createPointer() {
    return new HTPointer(this);
  }

  public HTPartition partitionFor(long index) {
    return (HTPartition) partitions.get((int) (index / partitionSize));
  }
}
