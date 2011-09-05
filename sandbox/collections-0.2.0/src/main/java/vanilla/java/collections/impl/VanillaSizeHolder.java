package vanilla.java.collections.impl;

import vanilla.java.collections.api.impl.SizeHolder;

import java.io.IOException;

public class VanillaSizeHolder implements SizeHolder {
  private long size;
  private long capacity;
  private long partitionSize;

  @Override
  public void size(long size) {
    this.size = size;
  }

  @Override
  public long size() {
    return size;
  }

  @Override
  public void capacity(long capacity) {
    this.capacity = capacity;
  }

  @Override
  public long capacity() {
    return capacity;
  }

  public void partitionSize(long partitionSize) {
    this.partitionSize = partitionSize;
  }

  public long partitionSize() {
    return partitionSize;
  }

  @Override
  public void close() throws IOException {

  }

  @Override
  public void flush() throws IOException {

  }
}
