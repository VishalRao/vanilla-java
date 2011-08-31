package vanilla.java.collections.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public enum NullReadWriteLock implements ReadWriteLock {
  INSTANCE;

  @Override
  public Lock readLock() {
    return NullLock.INSTANCE;
  }

  @Override
  public Lock writeLock() {
    return NullLock.INSTANCE;
  }

}
