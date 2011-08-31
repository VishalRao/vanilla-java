package vanilla.java.collections.api;

import java.util.SortedSet;

public interface HugeSortedSet<E> extends HugeSet<E>, SortedSet<E> {
  @Override
  HugeSortedSet<E> subSet(E fromElement, E toElement);

  @Override
  HugeSortedSet<E> headSet(E toElement);

  @Override
  HugeSortedSet<E> tailSet(E fromElement);
}
