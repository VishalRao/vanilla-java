package vanilla.java.collections.hand;

import vanilla.java.collections.api.impl.HugeElement;
import vanilla.java.collections.util.HugeCollections;

class HTPointer implements HT, HugeElement {
  private long index = Long.MIN_VALUE;
  private final HTArrayList list;
  private HTPartition partition;
  private int offset;

  public HTPointer(HTArrayList list) {
    this.list = list;
  }

  @Override
  public void setInt(int i) {
    partition.setInt(offset, i);
  }

  @Override
  public int getInt() {
    return partition.getInt(offset);
  }

  @Override
  public void setText(String id) {
    partition.setText(offset, id);
  }

  @Override
  public String getText() {
    return partition.getText(offset);
  }

  @Override
  public long longHashCode() {
    return HugeCollections.hashCode(getInt()) * 31L + HugeCollections.hashCode(getText());
  }

  @Override
  public long index() {
    return index;
  }

  @Override
  public void index(long index) {
    if (index / list.partitionSize() != this.index / list.partitionSize()) {
      partition = list.partitionFor(index);
      this.index = index;
    }
    offset = (int) (index % list.partitionSize());
  }

  @Override
  public void recycle() {
  }
}
