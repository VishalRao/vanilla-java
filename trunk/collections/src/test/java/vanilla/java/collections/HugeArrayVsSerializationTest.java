package vanilla.java.collections;

import org.junit.Test;
import vanilla.java.collections.api.HugeArrayList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HugeArrayVsSerializationTest {
    @Test
    public void testSearchAndUpdateCollection() {
        int length = 20 * 1000 * 1000;
        HugeArrayBuilder<MutableTypes> mtb = new HugeArrayBuilder<MutableTypes>() {
        };
        HugeArrayList<MutableTypes> mts = mtb.create();
        mts.setSize(length);
        long start = System.nanoTime();
        for (MutableTypes mt : mts) {
            mt.setInt(mt.getInt() + 1);
        }
        long time = System.nanoTime() - start;
        System.out.printf("Huge Collection update one field, took an average %,d ns.%n", time / length);
    }

    @Test
    public void testSearchAndUpdateCollectionHeap() {
        int length = 7 * 1000 * 1000;
        List<MutableTypes> mts = new ArrayList<MutableTypes>();
        HugeArrayBuilder<MutableTypes> mtb = new HugeArrayBuilder<MutableTypes>() {
        };
        for (int i = 0; i < length; i++)
            mts.add(mtb.createBean());

        long start = System.nanoTime();
        for (MutableTypes mt : mts) {
            mt.setInt(mt.getInt() + 1);
        }
        long time = System.nanoTime() - start;
        System.out.printf("List<JavaBean>, update one field took an average %,d ns.%n", time / length);
    }

    @Test
    public void testSearchAndUpdateCollectionSerialization() throws IOException, ClassNotFoundException {
        int length = 200 * 1000;
        List<byte[]> mts = new ArrayList<byte[]>();
        HugeArrayBuilder<MutableTypes> mtb = new HugeArrayBuilder<MutableTypes>() {
        };
        final MutableTypes bean = mtb.createBean();
        byte[] bytes = toBytes(bean);
        for (int i = 0; i < length; i++)
            mts.add(bytes);

        long start = System.nanoTime();
        for (int i = 0, mtsSize = mts.size(); i < mtsSize; i++) {
            MutableTypes mt = (MutableTypes) fromBytes(mts.get(i));
            mt.setInt(mt.getInt() + 1);
            mts.set(i, toBytes(mt));
        }
        long time = System.nanoTime() - start;
        System.out.printf("List<byte[]> update one field took an average %,d ns.%n", time / length);
    }

    public static byte[] toBytes(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return baos.toByteArray();
    }

    public static Object fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return ois.readObject();
    }
}
