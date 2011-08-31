package vanilla.java.collections.api.impl;

import vanilla.java.collections.api.Recycleable;

public interface HugeElement extends Recycleable {
  long longHashCode();

  long index();

  void index(long index);
}
