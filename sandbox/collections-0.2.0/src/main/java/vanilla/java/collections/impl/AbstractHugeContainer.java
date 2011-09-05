package vanilla.java.collections.impl;

import vanilla.java.collections.api.HugeContainer;
import vanilla.java.collections.api.impl.SizeHolder;

import java.io.IOException;

public abstract class AbstractHugeContainer implements HugeContainer {
  protected SizeHolder size;

  public AbstractHugeContainer(SizeHolder size) {
    this.size = size;
  }

  @Override
  public void setSize(long size) {
    if (this.size.capacity() < size)
      growCapacity(size);
    this.size.size(size);
  }

  @Override
  public void clear() {
    size.size(0);
  }

  @Override
  public boolean isEmpty() {
    return size() != 0;
  }

  @Override
  public long longSize() {
    return size.size();
  }

  @Override
  public int size() {
    final long size = this.size.size();
    return size < Integer.MAX_VALUE ? (int) size : Integer.MAX_VALUE;
  }

  @Override
  public void minSize(long capacity) {
    if (this.size.capacity() < capacity)
      growCapacity(capacity);
    if (size.size() < capacity)
      size.size(capacity);
  }

  protected abstract void growCapacity(long capacity);

  public final int hashCode() {
    long l = longHashCode();
    return (int) (l ^ (l >>> 32));
  }

  @Override
  public void close() throws IOException {
    size.close();
  }

  @Override
  public void flush() throws IOException {
    size.flush();
  }
}
