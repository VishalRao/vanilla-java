package vanilla.java.collections.impl;

import sun.nio.ch.DirectBuffer;
import vanilla.java.collections.api.HugeIterator;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

public class HugeCollectionView<E> extends AbstractHugeCollection<E> {
  private final List<LongBuffer> longBuffers = new ArrayList<LongBuffer>();

  public HugeCollectionView(Class<E> elementType) {
    super(elementType);
  }


  @Override
  public boolean add(E e) {
    throw new Error("Not implemented");
  }

  @Override
  public HugeIterator<E> iterator() {
    throw new Error("Not implemented");
  }

  @Override
  protected void growCapacity(long capacity) {
    throw new Error("Not implemented");
  }

  @Override
  public boolean remove(Object o) {
    throw new Error("Not implemented");
  }

  @Override
  public void recycle(Object recycleable) {
  }

  @Override
  public void recycle() {
    for (LongBuffer longBuffer : longBuffers) {
      ((DirectBuffer) longBuffer).cleaner().clean();
    }
    longBuffers.clear();
  }
}
