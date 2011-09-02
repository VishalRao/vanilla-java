package vanilla.java.collections.api.impl;

import java.util.concurrent.locks.ReadWriteLock;

public interface HugePartition {
  ReadWriteLock lock();

  void clear();

  void destroy();

  void compact();
}
