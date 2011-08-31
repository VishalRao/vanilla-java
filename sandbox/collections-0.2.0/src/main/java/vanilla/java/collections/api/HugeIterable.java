package vanilla.java.collections.api;

public interface HugeIterable<E> extends Iterable<E> {
  /**
   * @return an iterator for a HugeCollection which can be recycled.
   */
  public HugeIterator<E> iterator();
}
