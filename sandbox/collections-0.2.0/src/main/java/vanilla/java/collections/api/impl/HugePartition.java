package vanilla.java.collections.api.impl;

import java.io.Closeable;
import java.io.Flushable;
import java.util.concurrent.locks.ReadWriteLock;

public interface HugePartition extends Flushable, Closeable {
  ReadWriteLock lock();

  void clear();

  void destroy();

  void compact();
}
