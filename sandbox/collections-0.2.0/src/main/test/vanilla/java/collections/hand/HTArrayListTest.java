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

import static junit.framework.Assert.assertEquals;
import static vanilla.java.collections.util.HugeCollections.close;
import static vanilla.java.collections.util.HugeCollections.recycle;

import java.util.List;
import java.util.ListIterator;

import org.junit.Ignore;
import org.junit.Test;

import vanilla.java.collections.api.HugeCollection;
import vanilla.java.collections.api.HugeList;
import vanilla.java.collections.api.Predicate;
import vanilla.java.collections.impl.DirectByteBufferAllocator;

public class HTArrayListTest {
  public static HTArrayList createList(int partitionSize) {
    // a very small partition size to show problems quickly.
    return new HTArrayList(partitionSize, HT.class, new DirectByteBufferAllocator());
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
  public void testAddAll() {
	    List<HT> list0 = createList(100);	   
	    for (int i = 0; i < 100; i++) {
	    	list0.add(new HTImpl(i, "hello-" + i));
	    }	      
	    assertEquals(100, list0.size());

	    List<HT> list1 = createList(100);	   
	    for (int i = 0; i < 100; i++) {
	    	list1.add(new HTImpl(i, "hello-" + i));
	    }	      
	    assertEquals(100, list1.size());
	    
	    list0.addAll(list1);
	    assertEquals(200, list0.size());
	    
	    close(list0);
	    close(list1);
  }
  
  @Test
  @Ignore("ListIterator not yet implemented?")
  public void testListIterator() {
	  List<HT> list = createList(10); //new ArrayList<HT>();
      HT ht = new HTImpl(0, "hello-0");
          
      ListIterator<HT> iter = list.listIterator();
      iter.add(ht);
      assertEquals("hello-0", list.get(0).getText());
      
      iter.previous();
      iter.next();
      iter.remove();
      assertEquals(0, list.size());
      
      iter.add(ht);
      assertEquals(0, list.get(0).getInt());
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
    List<HT> list = createList(4 * 1024);
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

  @Test
  public void testFilter() {
    HugeList<HT> list = createList(1024);
    // force it to grow.
    for (int i = 0; i < 1000; i++)
      list.add(new HTImpl(i, "hello-" + i));
    // filter
    final HugeCollection<HT> list0 = list.filter(new Predicate<HT>() {
      @Override
      public boolean test(HT ht) {
        final String text = ht.getText();
        return text.endsWith("0");
      }
    });
    assertEquals(list.size() / 10, list0.size());
    int count = 0;
    for (HT ht : list0) {
      assertEquals(count++ * 10, ht.getInt());
    }

    // todo fix.
    final HugeCollection<HT> list2_0 = list0.filter(new Predicate<HT>() {
      @Override
      public boolean test(HT ht) {
        final String text = ht.getText();
        return text.startsWith("hello-2");
      }
    });
    assertEquals(11, list2_0.size());
/*
    for (HT ht : list2_0) {
      System.out.println(ht);
    }
*/
  }
}
