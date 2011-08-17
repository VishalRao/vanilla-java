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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class ShortFieldModel extends AbstractFieldModel<Short> {
    public ShortFieldModel(String fieldName, int fieldNumber) {
        super(fieldName, fieldNumber);
    }

    @Override
    public Object arrayOfField(int size) {
        return newArrayOfField(size);
    }

    @Override
    public Class storeType() {
        return ShortBuffer.class;
    }

    public static ShortBuffer newArrayOfField(int size) {
        return ByteBuffer.allocateDirect(size * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
    }

    @Override
    public Short getAllocation(Object[] arrays, int index) {
        ShortBuffer array = (ShortBuffer) arrays[fieldNumber];
        return get(array, index);
    }

    public static short get(ShortBuffer array, int index) {
        return array.get(index);
    }

    @Override
    public void setAllocation(Object[] arrays, int index, Short value) {
        ShortBuffer array = (ShortBuffer) arrays[fieldNumber];
        set(array, index, value);
    }

    public static void set(ShortBuffer array, int index, short value) {
        array.put(index, value);
    }

    @Override
    public Class<Short> type() {
        return (Class) short.class;
    }

    @Override
    public String bcLFieldType() {
        return "S";
    }

    @Override
    public short equalsPreference() {
        return 16;
    }

}
