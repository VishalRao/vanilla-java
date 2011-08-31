package vanilla.java.collections.api;

public interface Procedure<E, T> {
  T apply(E e);
}
