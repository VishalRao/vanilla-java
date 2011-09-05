package vanilla.java.collections.api;

import java.io.Closeable;
import java.io.Flushable;
import java.util.concurrent.locks.ReadWriteLock;

public interface HugeContainer extends Flushable, Closeable {
  /**
   * @return a global lock for the collection.
   */
  ReadWriteLock lock();

  /**
   * @return the size if less than Integer.MAX_VALUE or Integer.MAX_VALUE
   */
  int size();

  /**
   * Grow or shrink the size
   *
   * @param size to make the collection.
   */
  void setSize(long size);

  /**
   * @param capacity the minimum capacity for the collection.
   */
  void minSize(long capacity);

  /**
   * @return the size of hte collection.
   */
  long longSize();

  /**
   * @return the size in elements of each partition.
   */
  int partitionSize();

  /**
   * @return is the collection empty.
   */
  boolean isEmpty();

  /**
   * @return printable version of the collection.
   * @throws OutOfMemoryError if the collection is too large
   */
  String toString() throws OutOfMemoryError;

  /**
   * @param o another Collection of the same type.
   * @return true if the same type with equal elements.
   */
  boolean equals(Object o);

  /**
   * @return hashCode of the collection.
   */
  int hashCode();

  /**
   * @return hashCode of the collection.
   */
  long longHashCode();

  /*
   * Closes the collection with a warning if not closed already.
   */
//  void finalize() throws Throwable;

  /**
   * Clear all elements of the collection.
   */
  void clear();

  /**
   * @param recycleable to be recycled or discard.
   */
  void recycle(Object recycleable);

  /**
   * reduce memory consumption and remove fragmentation
   */
  void compact();
}
