package vanilla.java.collections.api.impl;

public interface Copyable<T> {
  void copyFrom(T t);

  T copyOf();
}
