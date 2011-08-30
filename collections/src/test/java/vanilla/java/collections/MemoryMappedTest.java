package vanilla.java.collections;

/*
 * Copyright 2011 Peter Lawrey
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import org.junit.Test;
import vanilla.java.collections.api.HugeArrayList;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static vanilla.java.collections.HugeArrayBuilderTest.populate;
import static vanilla.java.collections.HugeArrayBuilderTest.validate;

public class MemoryMappedTest {
  interface MutableBooleans {
    public void setOne(boolean b);

    public boolean getOne();

    public void setTwo(boolean b);

    public boolean getTwo();

    public void setThree(Boolean b);

    public Boolean getThree();

    public void setFour(Boolean b);

    public Boolean getFour();

  }

  @Test
  public void testMemoryMappedBoolean() throws IOException {
    final int length = 1024 * 1024;
    HugeArrayBuilder<MutableBooleans> hab = new HugeArrayBuilder<MutableBooleans>() {
      {
        baseDirectory = "/d/tmp";
        capacity = length;
        allocationSize = 256 * 1024;
      }
    };
    final HugeArrayList<MutableBooleans> list = hab.create();
    list.setSize(length);
    int i = 0;
    for (MutableBooleans mi : list) {
      mi.setOne(i % 11 < 6);
      mi.setTwo(i % 13 < 7);
      mi.setThree(i % 15 < 8);
      mi.setFour(i % 17 < 9);
      i++;
    }
    list.close();

    HugeArrayBuilder<MutableBooleans> hab2 = new HugeArrayBuilder<MutableBooleans>() {
      {
        baseDirectory = "/d/tmp";
        capacity = length;
        allocationSize = 256 * 1024;
      }
    };
    final HugeArrayList<MutableBooleans> list2 = hab2.create();
    list2.setSize(length);
    int j = 0;
    for (MutableBooleans mi : list) {
      assertEquals(j % 11 < 6, mi.getOne());
      assertEquals(j % 13 < 7, mi.getTwo());
      assertEquals((Boolean) (j % 15 < 8), mi.getThree());
      assertEquals((Boolean) (j % 17 < 9), mi.getFour());
      j++;
    }
    list2.close();
  }

  interface MutableBytes {
    public void setOne(byte b);

    public byte getOne();

    public void setTwo(byte b);

    public byte getTwo();

    public void setThree(Byte b);

    public Byte getThree();

    public void setFour(Byte b);

    public Byte getFour();

  }

  @Test
  public void testMemoryMappedByte() throws IOException {
    final int length = 1024 * 1024;
    HugeArrayBuilder<MutableBytes> hab = new HugeArrayBuilder<MutableBytes>() {
      {
        baseDirectory = "/d/tmp";
        capacity = length;
        allocationSize = 256 * 1024;
      }
    };
    final HugeArrayList<MutableBytes> list = hab.create();
    list.setSize(length);
    int i = 0;
    for (MutableBytes mi : list) {
      mi.setOne((byte) i);
      mi.setTwo((byte) (i * 13));
      mi.setThree((byte) (i * 31));
      mi.setFour((byte) (i * 37));
      i++;
    }
    list.close();

    HugeArrayBuilder<MutableBytes> hab2 = new HugeArrayBuilder<MutableBytes>() {
      {
        baseDirectory = "/d/tmp";
        capacity = length;
        allocationSize = 256 * 1024;
      }
    };
    final HugeArrayList<MutableBytes> list2 = hab2.create();
    list2.setSize(length);
    int j = 0;
    for (MutableBytes mi : list) {
      assertEquals((byte) j, mi.getOne());
      assertEquals((byte) (j * 13), mi.getTwo());
      assertEquals((Byte) (byte) (j * 31), mi.getThree());
      assertEquals((Byte) (byte) (j * 37), mi.getFour());
      j++;
    }
    list2.close();
  }

  interface MutableInts {
    public void setInt(int i);

    public int getInt();

    public void setInt2(int i);

    public int getInt2();
  }

  @Test
  public void testMemoryMappedInt() throws IOException {
    final int length = 1024 * 1024;
    HugeArrayBuilder<MutableInts> hab = new HugeArrayBuilder<MutableInts>() {
      {
        baseDirectory = "/d/tmp";
        capacity = length;
        allocationSize = 256 * 1024;
      }
    };
    final HugeArrayList<MutableInts> list = hab.create();
    list.setSize(length);
    int i = 0;
    for (MutableInts mi : list) {
      mi.setInt(i++);
      mi.setInt2(i++);
    }
    list.close();

    HugeArrayBuilder<MutableInts> hab2 = new HugeArrayBuilder<MutableInts>() {
      {
        baseDirectory = "/d/tmp";
        capacity = length;
        allocationSize = 256 * 1024;
      }
    };
    final HugeArrayList<MutableInts> list2 = hab2.create();
    list2.setSize(length);
    int j = 0;
    for (MutableInts mi : list) {
      assertEquals(j++, mi.getInt());
      assertEquals(j++, mi.getInt2());
    }
    list2.close();
  }

  @Test
  public void testMemoryMapped2() throws IOException {
    final int length = 1024 * 1024;
    HugeArrayBuilder<MutableTypes> hab = new HugeArrayBuilder<MutableTypes>() {
      {
        baseDirectory = "/d/tmp";
        capacity = length;
        allocationSize = 256 * 1024;
      }
    };
    final HugeArrayList<MutableTypes> list = hab.create();
    list.setSize(length);
    populate(list);
    list.close();

    HugeArrayBuilder<MutableTypes> hab2 = new HugeArrayBuilder<MutableTypes>() {
      {
        baseDirectory = "/d/tmp";
        capacity = length;
        allocationSize = 256 * 1024;
      }
    };
    final HugeArrayList<MutableTypes> list2 = hab2.create();
    list2.setSize(length);
    validate(list2);
    list2.close();
  }
}
