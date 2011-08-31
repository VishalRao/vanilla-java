package vanilla.java.collections.api;

import java.util.ListIterator;

public interface HugeListIterator<E> extends HugeIterator<E>, ListIterator<E> {
  long longNextIndex();

  long longIndex();

  long longPreviousIndex();
}
