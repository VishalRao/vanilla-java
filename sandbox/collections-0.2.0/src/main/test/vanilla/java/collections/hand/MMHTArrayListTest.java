package vanilla.java.collections.hand;

import org.junit.Test;
import vanilla.java.collections.impl.MemoryMappedByteBufferAllocator;

import java.io.File;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static vanilla.java.collections.util.HugeCollections.close;
import static vanilla.java.collections.util.HugeCollections.recycle;

public class MMHTArrayListTest {
  public static HTArrayList createList(int partitionSize) {
    // a very small partition size to show problems quickly.
    return new HTArrayList(partitionSize, HT.class, new MemoryMappedByteBufferAllocator(new File("tmp")));
  }

  @Test
  public void createClose() {
    // without close(list) this causes multiple Full GC,
    // however with close, no GCs
    for (int i = 0; i < 10 * 1000; i++) {
      List<HT> list = createList(64 * 1024);
      list.add(new HTImpl());
      close(list);
    }
  }

  @Test
  public void testAdd() {
    List<HT> list = createList(1024);
    // force it to grow.
    for (int i = 0; i < 100 * 1000; i++)
      list.add(new HTImpl(i, "hello"));
    // check the values added are there
    for (int i = 0; i < 100 * 1000; i++)
      assertEquals(i, list.get(i).getInt());
    close(list);
  }

  @Test
  public void testRemove() {
    List<HT> list = createList(1024);
    // force it to grow.
    for (int i = 0; i < 100 * 1000; i++)
      list.add(new HTImpl(i, "hello"));
    // check the values added are there
    for (int i = 100 * 1000 - 1; i >= 0; i--) {
      list.remove(i);
      assertEquals(i, list.size());
    }
    close(list);
  }

  @Test
  public void testGet() {
    List<HT> list = createList(1024);
    // force it to grow.
    for (int i = 0; i < 100 * 1000; i++)
      list.add(new HTImpl(i, "hello-" + i));
    // check the values added are there
    for (int i = 0; i < 100 * 1000; i++) {
      final HT ht = list.get(i);
      assertEquals(i, ht.getInt());
      assertEquals("hello-" + i, ht.getText());
      recycle(ht);
    }
    close(list);
  }

  @Test
  public void testContainsIndexOf() {
    List<HT> list = createList(1024);
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
