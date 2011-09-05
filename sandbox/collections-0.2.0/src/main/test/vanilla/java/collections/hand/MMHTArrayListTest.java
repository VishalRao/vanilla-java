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

import org.junit.Test;
import vanilla.java.collections.api.HugeContainer;
import vanilla.java.collections.impl.MemoryMappedByteBufferAllocator;

import java.io.File;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static vanilla.java.collections.util.HugeCollections.close;
import static vanilla.java.collections.util.HugeCollections.recycle;

public class MMHTArrayListTest {

  public static final int PARTITION_SIZE = 8 * 1024;

  public static HTArrayList createList(int partitionSize) {
    // a very small partition size to show problems quickly.
    return new HTArrayList(partitionSize, HT.class, new MemoryMappedByteBufferAllocator(new File("tmp")));
  }

  @Test
  public void createClose() {
    // without close(list) this causes multiple Full GC,
    // however with close, no GCs
    for (int i = 0; i < 100; i++) {
      List<HT> list = createList(PARTITION_SIZE);
      list.add(new HTImpl());
      close(list);
    }
  }

  @Test
  public void testAdd() {
    List<HT> list = createList(PARTITION_SIZE);
    list.clear();
    // force it to grow.
    final int size = 100 * 1000;
    for (int i = 0; i < size; i++)
      list.add(new HTImpl(i, "hello"));
    // check the values added are there
    for (int i = 0; i < size; i++)
      assertEquals(i, list.get(i).getInt());
    close(list);
    List<HT> list2 = createList(PARTITION_SIZE);
    assertEquals(size, list2.size());
    // check the values added are there
    for (int i = 0; i < size; i++)
      assertEquals(i, list2.get(i).getInt());
    close(list2);
  }

  @Test
  public void testRemove() {
    List<HT> list = createList(PARTITION_SIZE);
    list.clear();
    // force it to grow.
    for (int i = 0; i < 100 * 1000; i++)
      list.add(new HTImpl(i, "hello"));
    // check the values added are there
    for (int i = 100 * 1000 - 1; i >= 0; i--) {
      list.remove(i);
      assertEquals(i, list.size());
    }
    close(list);
    List<HT> list2 = createList(PARTITION_SIZE);
    assertEquals(0, list2.size());
    close(list2);
  }

  @Test
  public void testGet() {
    List<HT> list = createList(PARTITION_SIZE);
    list.clear();
    // force it to grow.
    final int size = 100 * 1000;
    for (int i = 0; i < size; i++)
      list.add(new HTImpl(i, "hello-" + i));
    // check the values added are there
    for (int i = 0; i < size; i++) {
      final HT ht = list.get(i);
      assertEquals(i, ht.getInt());
      assertEquals("hello-" + i, ht.getText());
      recycle(ht);
    }
    close(list);
    List<HT> list2 = createList(PARTITION_SIZE * 2);
    assertEquals(PARTITION_SIZE, ((HugeContainer) list2).partitionSize());
    assertEquals(size, list2.size());
    // check the values added are there
    for (int i = 0; i < size; i++) {
      final HT ht = list2.get(i);
      assertEquals(i, ht.getInt());
      assertEquals("hello-" + i, ht.getText());
      recycle(ht);
    }
    close(list2);
  }

  @Test
  public void testContainsIndexOf() {
    List<HT> list = createList(PARTITION_SIZE);
    list.clear();
    // force it to grow.
    final int size = 5 * 1000;
    for (int i = 0; i < size; i += 2)
      list.add(new HTImpl(i, "hello-" + i));
    // check the values added are there
    for (int i = 0; i < size; i++) {
      final HTImpl ht = new HTImpl(i, "hello-" + i);
      boolean b = list.contains(ht);
      int idx = list.indexOf(ht);
      assertEquals("" + i, i % 2 == 0, b);
      assertEquals("" + i, i % 2 == 0 ? i / 2 : -1, idx);
    }
    close(list);
  }
}
