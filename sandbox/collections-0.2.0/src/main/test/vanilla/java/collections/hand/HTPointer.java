/*
 * Copyright (c) 2011 Peter Lawrey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vanilla.java.collections.hand;

import vanilla.java.collections.api.impl.Copyable;
import vanilla.java.collections.api.impl.HugeElement;
import vanilla.java.collections.util.HugeCollections;

class HTPointer implements HT, HugeElement, Copyable<HT> {
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
    }
    this.index = index;
    offset = (int) (index % list.partitionSize());
  }

  @Override
  public void recycle() {
    list.pointerPoolAdd(this);
  }

  @Override
  public void copyFrom(HT ht) {
    setInt(ht.getInt());
    setText(ht.getText());
  }

  @Override
  public HT copyOf() {
    HT ht = list.acquireImpl();
    ((Copyable<HT>) ht).copyFrom(this);
    return ht;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof HT)) return false;

    HT ht = (HT) o;

    if (getInt() != ht.getInt()) return false;
    if (!getText().equals(ht.getText())) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return getInt() * 31 + getText().hashCode();
  }


  @Override
  public String toString() {
    return "HT{" +
               "int=" + getInt() +
               ", text='" + getText() + '\'' +
               '}';
  }
}
