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
import vanilla.java.collections.api.HugeElement;
import vanilla.java.collections.hand.HandTypes;
import vanilla.java.collections.hand.HandTypesArrayList;
import vanilla.java.collections.hand.HandTypesImpl;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class HugeArrayBuilderTest {
    private static final ElementType[] elementTypes = ElementType.values();
    private static final long length = 10 * 1000 * 1000L;

    interface MutableBoolean {
        public void setFlag(boolean b);

        public boolean getFlag();
    }

    @Ignore
    @Test
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

        // test the class can be create more than once.
        HugeArrayList<MutableTypes> hugeList0 = new HugeArrayBuilder<MutableTypes>() {{
            capacity = 1024 * 1024;
        }}.create();

        HugeArrayList<MutableTypes> hugeList = new HugeArrayBuilder<MutableTypes>() {{
            capacity = length;
        }}.create();
        List<MutableTypes> list = hugeList;
        assertEquals(0, list.size());

        hugeList.setSize(length);

        Thread t = monitorThread();

        assertEquals(length, list.size());
        assertEquals(length, hugeList.longSize());

        exerciseList(list, length, new HAListSetSize(hugeList));

        t.interrupt();
        gcPrintUsed();
        assertEquals(length, list.size());
    }

    @Test
    public void testAddTypes() throws Exception {
        gcPrintUsed();

        final HugeArrayBuilder<MutableTypes> builder = new HugeArrayBuilder<MutableTypes>() {{
            capacity = length;
        }};
        final MutableTypes bean = builder.createBean();
        final MutableTypes bean2 = builder.createBean();
        HugeArrayList<MutableTypes> hugeList = builder.create();
        List<MutableTypes> list = hugeList;
        assertEquals(0, list.size());

        final int elements = 2;
        for (int i = 0; i < elements; i++) {
            setFields(bean, i + 1);
            ((HugeElement) bean2).copyOf(bean);
            final String expected = bean.toString();
            final String actual0 = bean2.toString();
            assertEquals(expected, actual0);

            list.add(bean);
            final String actual = list.get(i).toString();
            assertEquals(expected, actual);
        }
        assertEquals(elements, list.size());
        for (int i = 0; i < elements; i++) {
            final MutableTypes mt = list.get(i);
            assertEquals(i + 1, mt.getInt());
            hugeList.recycle(mt);
        }
        assertEquals(elements, list.size());
    }

    @Test
    public void testRemoveTypes() throws Exception {
        gcPrintUsed();

        final HugeArrayBuilder<MutableTypes> builder = new HugeArrayBuilder<MutableTypes>() {{
            capacity = length;
            setRemoveReturnsNull = false;
        }};
        final MutableTypes bean = builder.createBean();
        HugeArrayList<MutableTypes> hugeList = builder.create();
        List<MutableTypes> list = hugeList;
        assertEquals(0, list.size());

        final int elements = 2;
        for (int i = 0; i < elements; i++) {
            setFields(bean, i + 1);
            list.add(bean);
        }
        assertEquals(elements, list.size());

        Set<Integer> integers = new LinkedHashSet<Integer>();
        for (int i = 0; i < elements; i++) {
            MutableTypes mt = list.remove(i);
            integers.add(mt.getInt());
            hugeList.recycle(mt);
        }
        assertEquals(0, list.size());
    }

    @Test
    public void testCreateObjectTypes() throws Exception {
        gcPrintUsed();

        HugeArrayList<ObjectTypes> hugeList = new HugeArrayBuilder<ObjectTypes>() {{
            capacity = length;
        }}.create();
        List<ObjectTypes> list = hugeList;
        assertEquals(0, list.size());

        hugeList.setSize(length);

        Thread t = monitorThread();

        assertEquals(length, list.size());
        assertEquals(length, hugeList.longSize());

        exerciseObjectList(list, length);

        t.interrupt();
        gcPrintUsed();
        assertEquals(length, list.size());
    }


    @Ignore
    @Test
    public void testCreateTypes2() throws Exception {
        gcPrintUsed();

        HugeArrayList<HandTypes> hugeList = new HandTypesArrayList(1024 * 1024, false);
        List<HandTypes> list = hugeList;
        assertEquals(0, list.size());

        hugeList.setSize(length);

        Thread t = monitorThread();

        assertEquals(length, list.size());
        assertEquals(length, hugeList.longSize());

        exerciseList((HugeArrayList) list, length, new HAListSetSize(hugeList));

        t.interrupt();
        gcPrintUsed();
        assertEquals(length, list.size());
    }

    @Ignore
    @Test
    public void testCreateObjectTypeJavaBean() throws Exception {
        gcPrintUsed();

        List<ObjectTypes> list = new ArrayList<ObjectTypes>();
        assertEquals(0, list.size());

        Thread t = monitorThread();

        for (int i = 0; i < length; i++)
            list.add(new ObjectTypesJavaBean());

        exerciseObjectList(list, length);
        t.interrupt();
        gcPrintUsed();
        assertEquals(length, list.size());
    }

    @Test
    public void testRemoveAll() throws Exception {
        gcPrintUsed();

        final HugeArrayList<MutableTypes> hugeList = new HugeArrayBuilder<MutableTypes>() {{
            capacity = length;
            setRemoveReturnsNull = true;
        }}.create();
        List<MutableTypes> list = hugeList;
        assertEquals(0, list.size());

        Thread t = monitorThread();

        removeFromList(list, new HAListSetSize(hugeList));
        t.interrupt();
        gcPrintUsed();
        assertEquals(0, list.size());
    }

    @Ignore
    @Test
    public void testRemoveAllJavaBean() throws Exception {
        gcPrintUsed();

        final List<MutableTypes> list = new ArrayList<MutableTypes>();
        assertEquals(0, list.size());

        Thread t = monitorThread();

        removeFromList(list, new ArrayListSetSize(list));
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

        exerciseList(list, length, new ArrayListSetSize(list));
        t.interrupt();
        gcPrintUsed();
        assertEquals(length, list.size());
    }

    @Test
    public void testToStringHashCodeEquals() {
        final int size = 32 * 1024;
        HugeArrayList<MutableTypes> list = new HugeArrayBuilder<MutableTypes>() {{
            capacity = size;
        }}.create();
        list.setSize(size);
        populate(list);
        assertFalse(list.get(63).equals(list.get(64)));
        Set<Integer> hashCodes = new LinkedHashSet<Integer>();
        for (int n = 0; n < size; n++) {
            final MutableTypes mt = list.get(n);
            hashCodes.add(mt.hashCode());
            list.recycle(mt);
        }
        assertEquals(size, hashCodes.size());
    }

    private static void exerciseList(List<MutableTypes> list, long length, Runnable setSize) {
        assertEquals(length, list.size());
        gcPrintUsed();

        long start = System.currentTimeMillis();
        do {
            System.out.println("Updating");
            long startWrite = System.nanoTime();
            setSize.run();
            populate(list);
            int i;
            long timeWrite = System.nanoTime() - startWrite;
            System.out.printf("Took %,d ns per object write%n", timeWrite / list.size());

            assertEquals("MutableTypes{boolean=true, byte=64, short=64, char=@, int=64, long=64, float=64.0, double=64.0, string=64, boolean2=true, byte2=64, elementType=TYPE}"
                    , list.get(64).toString());

            System.out.println("Checking");
            long startRead = System.nanoTime();
            i = 0;
            for (MutableTypes mb : list) {
                validate(mb, i);
                i++;
            }
            long timeRead = System.nanoTime() - startRead;
            System.out.printf("Took %,d ns per object read/check%n", timeRead / list.size());

            long scanStart = System.nanoTime();
            for (MutableTypes mb : list) {
                if (mb.getInt() == i - 1)
                    break;
            }
            long scanTime = System.nanoTime() - scanStart;
            System.out.printf("Took %,d ns per field to scan%n", scanTime / list.size());

            long randomStart = System.nanoTime();
            for (int n = list.size() / 10, len = list.size(), p = 0; n > 0; n--) {
                p = (p + 101912) % len;
                final MutableTypes mt = list.get(p);
                validate(mt, p);
                if (list instanceof HugeArrayList)
                    ((HugeArrayList) list).recycle(mt);
            }
            long randomTime = System.nanoTime() - randomStart;
            System.out.printf("Took %,d ns per object to access randomly%n", randomTime * 10 / list.size());
            System.gc();
        } while (System.currentTimeMillis() - start < 10 * 1000);
        System.out.println("Finished");
    }

    private static void removeFromList(List<MutableTypes> list, Runnable setSize) {
        gcPrintUsed();

        long start = System.currentTimeMillis();
        do {
            System.out.println("Updating");
            long startWrite = System.nanoTime();
            setSize.run();
            populate(list);
            int i;
            long timeWrite = System.nanoTime() - startWrite;
            System.out.printf("Took %,d ns per object write%n", timeWrite / list.size());

            System.out.println("Removing");
            long startRemove = System.nanoTime();
            while (list.size() >= 3) {
                int size = list.size();
                // remove from the start.
                final MutableTypes mt0 = list.remove(0);
                assertEquals(size - 1, list.size());
                // remove from the middle.
                final MutableTypes mt1 = list.remove(list.size() / 2);
                assertEquals(size - 2, list.size());
                // remove from the end.
                final MutableTypes mt2 = list.remove(list.size() - 1);
                assertEquals(size - 3, list.size());
                if (list instanceof HugeArrayList) {
                    HugeArrayList hal = (HugeArrayList) list;
                    hal.recycle(mt2);
                    hal.recycle(mt1);
                    hal.recycle(mt0);
                }
//                System.out.println(list.size());
            }
            while (!list.isEmpty())
                // remove from the start.
                list.remove(0);

            long timeRemove = System.nanoTime() - startRemove;
            System.out.printf("Took %,d ns per object remove%n", timeRemove / length);

            System.gc();
        } while (System.currentTimeMillis() - start < 10 * 1000);
        System.out.println("Finished");
    }

    private static void validate(MutableTypes mb, int i) {
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
    }

    static final String[] strings = new String[1024];

    static {
        for (int i = 0; i < strings.length; i++)
            strings[i] = Integer.toString(i);
    }

    private static void populate(List<MutableTypes> list) {
        int i = 0;
        for (MutableTypes mb : list) {
            setFields(mb, i);
            i++;
        }
    }

    private static void setFields(MutableTypes mb, int i) {
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
    }

    private void exerciseObjectList(List<ObjectTypes> list, long length) {
        assertEquals(length, list.size());
        gcPrintUsed();

        ObjectTypes.A a = new ObjectTypes.A();
        ObjectTypes.B b = new ObjectTypes.B();
        ObjectTypes.C c = new ObjectTypes.C();
        ObjectTypes.D d = new ObjectTypes.D();

        long start = System.currentTimeMillis();
        do {
            System.out.println("Updating");
            long startWrite = System.nanoTime();
            for (ObjectTypes mb : list) {
                mb.setA(a);
                mb.setB(b);
                mb.setC(c);
                mb.setD(d);
            }
            long timeWrite = System.nanoTime() - startWrite;
            System.out.printf("Took %,d ns per object write%n", timeWrite / list.size());

            System.out.println("Checking");
            long startRead = System.nanoTime();
            for (ObjectTypes mb : list) {
                {
                    ObjectTypes.A v = mb.getA();
                    ObjectTypes.A expected = a;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    ObjectTypes.B v = mb.getB();
                    ObjectTypes.B expected = b;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    ObjectTypes.C v = mb.getC();
                    ObjectTypes.C expected = c;
                    if (v != expected)
                        assertEquals(expected, v);
                }
                {
                    ObjectTypes.D v = mb.getD();
                    ObjectTypes.D expected = d;
                    if (v != expected)
                        assertEquals(expected, v);
                }
            }
            long timeRead = System.nanoTime() - startRead;
            System.out.printf("Took %,d ns per object read/check%n", timeRead / list.size());
            System.gc();
        } while (System.currentTimeMillis() - start < 10 * 1000);
        System.out.println("Finished");
    }


    static class ObjectTypesJavaBean implements ObjectTypes {
        private A a;
        private B b;
        private C c;
        private D d;

        @Override
        public void setA(A a) {
            this.a = a;
        }

        @Override
        public A getA() {
            return a;
        }

        @Override
        public void setB(B b) {
            this.b = b;
        }

        @Override
        public B getB() {
            return b;
        }

        @Override
        public void setC(C c) {
            this.c = c;
        }

        @Override
        public C getC() {
            return c;
        }

        @Override
        public void setD(D d) {
            this.d = d;
        }

        @Override
        public D getD() {
            return d;
        }
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

    private static class ArrayListSetSize implements Runnable {
        private final List<MutableTypes> list;

        public ArrayListSetSize(List<MutableTypes> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (int i = 0; i < length; i++)
                list.add(new HandTypesImpl());
        }
    }

    private static class HAListSetSize implements Runnable {
        private final HugeArrayList<? extends MutableTypes> hugeList;

        public HAListSetSize(HugeArrayList<? extends MutableTypes> hugeList) {
            this.hugeList = hugeList;
        }

        @Override
        public void run() {
            hugeList.setSize(length);
        }
    }
}
