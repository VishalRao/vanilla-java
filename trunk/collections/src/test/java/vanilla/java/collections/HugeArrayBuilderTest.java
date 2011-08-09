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

import org.junit.Ignore;
import org.junit.Test;
import vanilla.java.collections.api.HugeArrayList;
import vanilla.java.collections.hand.MutableTypesArrayList;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class HugeArrayBuilderTest {
    private static final ElementType[] elementTypes = ElementType.values();
    private static final long length = 10 * 1000 * 1000L;

    interface MutableBoolean {
        public void setFlag(boolean b);

        public boolean getFlag();
    }

    @Ignore
    @org.junit.Test
    public void testCreate() throws Exception {
        Thread t = monitorThread();

//        final long length = 128 * 1000 * 1000 * 1000L;
        final long length = 10 * 1000 * 1000 * 1000L;
        HugeArrayList<MutableBoolean> hugeList = new HugeArrayBuilder<MutableBoolean>() {{
            capacity = length;
        }}.create();
        List<MutableBoolean> list = hugeList;
        assertEquals(0, list.size());

        hugeList.setSize(length);

        System.gc();

        assertEquals(Integer.MAX_VALUE, list.size());
        assertEquals(length, hugeList.longSize());

        boolean b = false;
        long count = 0;
        for (MutableBoolean mb : list) {
            mb.setFlag(b = !b);
            if ((int) count++ == 0)
                System.out.println("set " + count);
        }

        b = false;
        count = 0;
        for (MutableBoolean mb : list) {
            boolean b2 = mb.getFlag();
            boolean expected = b = !b;
            if (b2 != expected)
                assertEquals(expected, b2);
            if ((int) count++ == 0)
                System.out.println("get " + count);
        }
        t.interrupt();
    }

    @Test
    public void testCreateTypes() throws Exception {
        gcPrintUsed();

        HugeArrayList<MutableTypes> hugeList = new HugeArrayBuilder<MutableTypes>() {
        }.create();
        List<MutableTypes> list = hugeList;
        assertEquals(0, list.size());

        hugeList.setSize(length);

        Thread t = monitorThread();

        assertEquals(length, list.size());
        assertEquals(length, hugeList.longSize());

        exerciseList(list, length);

        t.interrupt();
        gcPrintUsed();
        assertEquals(length, list.size());
    }

    @Ignore
    @Test
    public void testCreateTypes2() throws Exception {
        gcPrintUsed();

        HugeArrayList<MutableTypes> hugeList = new MutableTypesArrayList(1024 * 1024);
        List<MutableTypes> list = hugeList;
        assertEquals(0, list.size());

        hugeList.setSize(length);

        Thread t = monitorThread();

        assertEquals(length, list.size());
        assertEquals(length, hugeList.longSize());

        exerciseList(list, length);

        t.interrupt();
        gcPrintUsed();
        assertEquals(length, list.size());
    }

    @Ignore
    @Test
    public void testCreateJavaBean() throws Exception {
        gcPrintUsed();

        List<MutableTypes> list = new ArrayList<MutableTypes>();
        assertEquals(0, list.size());

        Thread t = monitorThread();

        for (int i = 0; i < length; i++)
            list.add(new MutableTypesImpl());

        exerciseList(list, length);
        t.interrupt();
        gcPrintUsed();
        assertEquals(length, list.size());
    }

    static class MutableTypesImpl implements MutableTypes {
        private boolean b;
        private Boolean b2;
        private byte b3;
        private Byte b4;
        private char ch;
        private short s;
        private int i;
        private float f;
        private long l;
        private double d;
        private ElementType elementType;
        private String text;

        @Override
        public void setBoolean(boolean b) {
            this.b = b;
        }

        @Override
        public boolean getBoolean() {
            return b;
        }

        @Override
        public void setBoolean2(Boolean b) {
            this.b2 = b;
        }

        @Override
        public Boolean getBoolean2() {
            return b2;
        }

        @Override
        public void setByte(byte b) {
            this.b3 = b;
        }

        @Override
        public byte getByte() {
            return b3;
        }

        @Override
        public void setByte2(Byte b) {
            this.b4 = b;
        }

        @Override
        public Byte getByte2() {
            return b4;
        }

        @Override
        public void setChar(char ch) {
            this.ch = ch;
        }

        @Override
        public char getChar() {
            return ch;
        }

        @Override
        public void setShort(short s) {
            this.s = s;
        }

        @Override
        public short getShort() {
            return s;
        }

        @Override
        public void setInt(int i) {
            this.i = i;
        }

        @Override
        public int getInt() {
            return i;
        }

        @Override
        public void setFloat(float f) {
            this.f = f;
        }

        @Override
        public float getFloat() {
            return f;
        }

        @Override
        public void setLong(long l) {
            this.l = l;
        }

        @Override
        public long getLong() {
            return l;
        }

        @Override
        public void setDouble(double d) {
            this.d = d;
        }

        @Override
        public double getDouble() {
            return d;
        }

        @Override
        public void setElementType(ElementType elementType) {
            this.elementType = elementType;
        }

        @Override
        public ElementType getElementType() {
            return elementType;
        }

        @Override
        public void setString(String text) {
            this.text = text;
        }

        @Override
        public String getString() {
            return text;
        }
    }

    private static void exerciseList(List<MutableTypes> list, long length) {
        assertEquals(length, list.size());
        gcPrintUsed();

        String[] strings = new String[1024];
        for (int i = 0; i < strings.length; i++)
            strings[i] = Integer.toString(i);

        long start = System.currentTimeMillis();
        do {
            System.out.println("Updating");
            int i = 0;
            long startWrite = System.nanoTime();
            for (MutableTypes mb : list) {
                mb.setBoolean(i % 2 == 0);
                mb.setBoolean2(i % 3 == 0 ? null : i % 3 == 1);
                mb.setByte((byte) i);
                mb.setByte2(i % 31 == 0 ? null : (byte) i);
                mb.setChar((char) i);
                mb.setShort((short) i);
                mb.setInt(i);
                mb.setFloat(i);
                mb.setLong(i);
                mb.setDouble(i);
                mb.setElementType(elementTypes[i % elementTypes.length]);
                mb.setString(strings[i % strings.length]);
                i++;
            }
            long timeWrite = System.nanoTime() - startWrite;
            System.out.printf("Took %,d ns per object write%n", timeWrite / list.size());

            System.out.println("Checking");
            long startRead = System.nanoTime();
            i = 0;
            for (MutableTypes mb : list) {
                {
                    boolean v = mb.getBoolean();
                    boolean expected = i % 2 == 0;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    Boolean v = mb.getBoolean2();
                    Boolean expected = i % 3 == 0 ? null : i % 3 == 1;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    byte v = mb.getByte();
                    byte expected = (byte) i;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    Byte v = mb.getByte2();
                    Byte expected = i % 31 == 0 ? null : (byte) i;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    char v = mb.getChar();
                    char expected = (char) i;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    short v = mb.getShort();
                    short expected = (short) i;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    int v = mb.getInt();
                    int expected = i;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    float v = mb.getFloat();
                    float expected = i;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    long v = mb.getLong();
                    long expected = i;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    double v = mb.getDouble();
                    double expected = i;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    ElementType v = mb.getElementType();
                    ElementType expected = elementTypes[i % elementTypes.length];
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    String v = mb.getString();
                    String expected = strings[i % strings.length];
                    if (v != expected)
                        assertEquals(expected, v);
                }
                i++;
            }
            long timeRead = System.nanoTime() - startRead;
            System.out.printf("Took %,d ns per object read/check%n", timeRead / list.size());
            System.gc();
        } while (System.currentTimeMillis() - start < 60 * 1000);
        System.out.println("Finished");
    }


    static long start = System.currentTimeMillis();

    private static Thread monitorThread() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    printUsed();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ignored) {
                        break;
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
        return t;
    }

    private static void gcPrintUsed() {
        System.gc();
        Thread.yield();

        printUsed();
    }

    private static void printUsed() {
        double directUsed;
        try {
            directUsed = (Long) (reservedMemory.get(null));
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
        System.out.printf((System.currentTimeMillis() - start) / 1000
                + " sec - used %6.1f MB heap, %6.1f MB direct.%n",
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1e6,
                directUsed / 1e6
        );
    }

    static final Field reservedMemory;

    static {
        try {
            reservedMemory = Class.forName("java.nio.Bits").getDeclaredField("reservedMemory");
            reservedMemory.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }
}
