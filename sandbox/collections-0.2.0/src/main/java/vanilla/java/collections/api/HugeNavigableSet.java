package vanilla.java.collections.api;

import java.util.NavigableSet;

public interface HugeNavigableSet<E> extends HugeSet<E>, NavigableSet<E> {
  @Override
  HugeNavigableSet<E> descendingSet();

  @Override
  HugeIterator<E> descendingIterator();

  @Override
  HugeNavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive);

  @Override
  HugeNavigableSet<E> headSet(E toElement, boolean inclusive);

  @Override
  HugeNavigableSet<E> tailSet(E fromElement, boolean inclusive);

  @Override
  HugeSortedSet<E> subSet(E fromElement, E toElement);

  @Override
  HugeSortedSet<E> headSet(E toElement);

  @Override
  HugeSortedSet<E> tailSet(E fromElement);
}
