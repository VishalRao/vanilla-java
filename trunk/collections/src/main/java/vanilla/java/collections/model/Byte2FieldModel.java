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

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class Byte2FieldModel implements FieldModel<Byte> {
    private final String fieldName;
    private final int fieldNumber;
    private Method setter;
    private Method getter;

    public Byte2FieldModel(String fieldName, int fieldNumber) {
        this.fieldName = fieldName;
        this.fieldNumber = fieldNumber;
    }

    @Override
    public Object arrayOfField(int size) {
        return newArrayOfField(size);
    }

    public static ByteBuffer newArrayOfField(int size) {
        return ByteBuffer.allocateDirect(size * 9 / 8);
    }

    @Override
    public Byte getAllocation(Object[] arrays, int index) {
        ByteBuffer array = (ByteBuffer) arrays[fieldNumber];
        return get(array, index);
    }

    public static Byte get(ByteBuffer array, int index) {
        int maskSize = array.capacity() / 9;
        boolean isNotNull = ((array.get(index >>> 3) >> (index & 7)) & 1) != 0;
        return isNotNull ? array.get(index + maskSize) : null;
    }

    @Override
    public void setAllocation(Object[] arrays, int index, Byte value) {
        ByteBuffer array = (ByteBuffer) arrays[fieldNumber];
        set(array, index, value);
    }

    public static void set(ByteBuffer array, int index, Byte value) {
        int maskSize = array.capacity() / 9;
        int index2 = index >>> 3;
        int mask = 1 << (index & 7);
        if (value == null) {
            // clear.
            array.put(index2, (byte) (array.get(index2) & ~mask));
        } else {
            array.put(index2, (byte) (array.get(index2) | mask));
            array.put(index + maskSize, value);
        }
    }

    @Override
    public void setter(Method setter) {
        this.setter = setter;
    }

    @Override
    public void getter(Method getter) {
        this.getter = getter;
    }

    @Override
    public int fieldNumber() {
        return fieldNumber;
    }

    @Override
    public Method setter() {
        return setter;
    }

    @Override
    public Method getter() {
        return getter;
    }

    @Override
    public Class<Byte> type() {
        return (Class) byte.class;
    }
}
