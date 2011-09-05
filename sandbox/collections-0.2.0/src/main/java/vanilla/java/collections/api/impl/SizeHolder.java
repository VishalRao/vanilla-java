package vanilla.java.collections.api.impl;

import java.io.Closeable;
import java.io.Flushable;

public interface SizeHolder extends Flushable, Closeable {
  public void size(long size);

  public long size();

  public void capacity(long capacity);

  public long capacity();

  public void partitionSize(long partitionSize);

  public long partitionSize();
}
