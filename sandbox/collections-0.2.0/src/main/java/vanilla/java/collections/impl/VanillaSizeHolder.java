package vanilla.java.collections.impl;

import vanilla.java.collections.api.impl.SizeHolder;

import java.io.IOException;

public class VanillaSizeHolder implements SizeHolder {
  private long size;
  private long capacity;

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

  @Override
  public void close() throws IOException {

  }

  @Override
  public void flush() throws IOException {

  }
}
