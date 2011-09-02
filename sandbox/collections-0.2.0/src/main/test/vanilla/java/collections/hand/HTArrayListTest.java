package vanilla.java.collections.hand;

import org.junit.Test;
import vanilla.java.collections.impl.DirectByteBufferAllocator;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class HTArrayListTest {
  public static HTArrayList createList() {
    // a very small partition size to show problems quickly.
    return new HTArrayList(1024, HT.class, new DirectByteBufferAllocator());
  }

  @Test
  public void testAdd() {
    List<HT> list = createList();
    // force it to grow.
    for (int i = 0; i < 10 * 1000; i++)
      list.add(new HTImpl(i, "hello"));
    // check the values added are there
    for (int i = 0; i < 10 * 1000; i++)
      assertEquals(i, list.get(i).getInt());

  }
}
