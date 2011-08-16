package vanilla.java.collections.hand;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class HandTypeArrayListTest {
    @Test
    public void testHTAL() {
        HandTypesArrayList htal = new HandTypesArrayList(4096, false);
        htal.setSize(5);
        for (int i = 0, htalSize = htal.size(); i < htalSize; i++) {
            HandTypes handTypes = htal.get(i);
            handTypes.setString("" + (i + 1));
        }
        assertEquals(6, htal.stringEnumerated16FieldModel.map().size());
        assertEquals("[null, 1, 2, 3, 4, 5]", htal.stringEnumerated16FieldModel.list().toString());

        htal.get(1).setString("1");
        htal.get(3).setString("3");
        assertEquals("5", htal.get(4).getString());
        htal.compact();
        assertEquals("5", htal.get(4).getString());
        assertEquals(4, htal.stringEnumerated16FieldModel.map().size());
        assertEquals("[null, 1, null, 3, null, 5]", htal.stringEnumerated16FieldModel.list().toString());
        assertEquals("5", htal.get(4).getString());
        htal.get(1).setString("6");
        assertEquals("5", htal.get(4).getString());
        htal.get(4).setString("7");
        htal.compact();

        assertEquals(5, htal.stringEnumerated16FieldModel.map().size());
        assertEquals("[null, 1, 6, 3, 7, null]", htal.stringEnumerated16FieldModel.list().toString());
    }
}
