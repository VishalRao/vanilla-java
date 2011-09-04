package vanilla.java.collections.api;

import java.util.Collection;
import java.util.List;

public interface HugeList<E> extends HugeCollection<E>, List<E> {
  void add(long index, E element);

  boolean addAll(long index, Collection<? extends E> c);

  E get(long index);

  @Override
  HugeListIterator<E> listIterator();

  @Override
  HugeListIterator<E> listIterator(int index);

  HugeListIterator<E> listIterator(long index);

  HugeListIterator<E> listIterator(long start, long end);

  long longIndexOf(Object o);

  long longLastIndexOf(Object o);

  E remove(long index);

  E set(long index, E element);

  HugeList<E> subList(long fromIndex, long toIndex);

  boolean update(long index, Updater<E> updater);
}
