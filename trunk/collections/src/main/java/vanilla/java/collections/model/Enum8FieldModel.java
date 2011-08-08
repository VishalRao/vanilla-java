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

public class Enum8FieldModel<E extends Enum<E>> implements FieldModel<E> {
    private final String fieldName;
    private final int fieldNumber;
    private final Class<E> type;
    private final E[] values;
    private Method setter;
    private Method getter;

    public Enum8FieldModel(String fieldName, Class<E> type, int fieldNumber, E[] values) {
        this.fieldName = fieldName;
        this.fieldNumber = fieldNumber;
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

    public void set(ByteBuffer array, int index, E value) {
        array.put(index, (byte) value.ordinal());
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
    public Class<E> type() {
        return type;
    }
}
