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

public class Enum8FieldModel<E extends Enum<E>> extends AbstractFieldModel<E> {
    private final Class<E> type;
    private final E[] values;

    public Enum8FieldModel(String fieldName, int fieldNumber, Class<E> type, E[] values) {
        super(fieldName, fieldNumber);
        this.type = type;
        this.values = values;
    }

    @Override
    public Object arrayOfField(int size) {
        return newArrayOfField(size);
    }

    public static ByteBuffer newArrayOfField(int size) {
        return ByteBuffer.allocateDirect(size);
    }

    @Override
    public Class storeType() {
        return ByteBuffer.class;
    }

    @Override
    public E getAllocation(Object[] arrays, int index) {
        ByteBuffer array = (ByteBuffer) arrays[fieldNumber];
        return get(array, index);
    }

    public E get(ByteBuffer array, int index) {
        return values[array.get(index)];
    }

    @Override
    public void setAllocation(Object[] arrays, int index, E value) {
        ByteBuffer array = (ByteBuffer) arrays[fieldNumber];
        set(array, index, value);
    }

    // mv.visitMethodInsn(INVOKEVIRTUAL, collections + "model/Enum8FieldModel", "set", "(Ljava/nio/ByteBuffer;ILjava/lang/Enum;)V");
    public void set(ByteBuffer array, int index, E value) {
        array.put(index, (byte) value.ordinal());
    }

    @Override
    public String bcLSetType() {
        return "Ljava/lang/Enum;";
    }

    @Override
    public Class<E> type() {
        return type;
    }

    @Override
    public BCType bcType() {
        return BCType.Reference;
    }


    @Override
    public boolean virtualGetSet() {
        return true;
    }

    @Override
    public boolean isCallsNotEquals() {
        return true;
    }

    @UsedFromByteCode
    public static int hashCode(Enum elementType) {
        return elementType == null ? Integer.MIN_VALUE : elementType.ordinal();
    }
}
