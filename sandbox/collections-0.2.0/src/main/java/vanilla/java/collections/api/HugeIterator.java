package vanilla.java.collections.api;

import vanilla.java.collections.api.impl.HugeElement;

import java.util.Iterator;

public interface HugeIterator<E> extends Iterator<E>, Recycleable {
  HugeElement nextElement();

  void index(long index);

  long index();
}
