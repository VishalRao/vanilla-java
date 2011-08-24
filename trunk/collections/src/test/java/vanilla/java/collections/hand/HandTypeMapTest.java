package vanilla.java.collections.hand;

import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

public class HandTypeMapTest {
  @Test
  public void putGetSize() {
    HandTypesMap map = new HandTypesMap(64 * 1024, true);
    HandTypesKeyImpl key = new HandTypesKeyImpl();
    HandTypesImpl value = new HandTypesImpl();
    long start = System.nanoTime();
    final int size = 10;
    for (int i = 0; i < size; i++) {
      put(map, key, value, i, false);
      put(map, key, value, i, true);
    }
    for (int i = 0; i < size; i++) {
      get(map, key, i, false);
      get(map, key, i, true);
    }
    long time = System.nanoTime() - start;
    System.out.printf("Took %.3f seconds to run", time / 1e9);
    System.out.println(Arrays.toString(map.sizes()));
    System.out.println(Arrays.toString(map.capacities()));
  }

  private static void put(HandTypesMap map, HandTypesKeyImpl key, HandTypesImpl value, int i, boolean flag) {
    final int k = i;
    key.setBoolean(flag);
    key.setInt(k);
    value.setBoolean(flag);
    value.setInt(k);
    map.put(key, value);
  }

  private static void get(HandTypesMap map, HandTypesKeyImpl key, int i, boolean flag) {
    final int k = i;
    key.setBoolean(flag);
    key.setInt(k);
    HandTypes ht = map.get(k);
    assertEquals(k, ht.getInt());
    assertEquals(flag, ht.getBoolean());
  }
}
