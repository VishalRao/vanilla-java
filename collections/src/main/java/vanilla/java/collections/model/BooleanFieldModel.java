package vanilla.java.collections.model;

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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class BooleanFieldModel extends AbstractFieldModel<Boolean> {
    public BooleanFieldModel(String fieldName, int fieldNumber) {
        super(fieldName, fieldNumber);
    }

    @Override
    public Object arrayOfField(int size) {
        return newArrayOfField(size);
    }

    public static IntBuffer newArrayOfField(int size) {
        return ByteBuffer.allocateDirect(size / 8).order(ByteOrder.nativeOrder()).asIntBuffer();
    }

    @Override
    public Class storeType() {
        return IntBuffer.class;
    }

    @Override
    public Boolean getAllocation(Object[] arrays, int index) {
        IntBuffer array = (IntBuffer) arrays[fieldNumber];
        return get(array, index);
    }

    public static boolean get(IntBuffer array, int index) {
        return ((array.get(index >>> 5) >> index) & 1) != 0;
    }

    @Override
    public void setAllocation(Object[] arrays, int index, Boolean value) {
        IntBuffer array = (IntBuffer) arrays[fieldNumber];
        set(array, index, value);
    }

    public static void set(IntBuffer array, int index, boolean value) {
        int index2 = index >>> 5;
        if (value)
            array.put(index2, (array.get(index2) | (1 << index)));
        else
            array.put(index2, (array.get(index2) & ~(1 << index)));
    }

    public static void write(ObjectOutput oo, boolean b) throws IOException {
        oo.writeByte(b ? 1 : 0);
    }

    public static boolean read(ObjectInput oi) throws IOException {
        byte b = oi.readByte();
        return (b > 0);
    }

    @Override
    public Class<Boolean> type() {
        return (Class) boolean.class;
    }

    @Override
    public String bcLFieldType() {
        return "Z";
    }

    @Override
    public boolean isCallsHashCode() {
        return true;
    }

    public static int hashCode(boolean b) {
        return b ? 1 : 0;
    }


    @Override
    public boolean copySimpleValue() {
        return false;
    }

    @Override
    public short equalsPreference() {
        return 1;
    }
}
