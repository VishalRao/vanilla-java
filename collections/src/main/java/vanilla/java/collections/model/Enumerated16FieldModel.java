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
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Enumerated16FieldModel<T> implements FieldModel<T> {
    private final String fieldName;
    private final Class<T> type;
    private final Map<T, Character> map = new LinkedHashMap<T, Character>();
    private final List<T> list = new ArrayList<T>();
    private final int fieldNumber;
    private Method setter;
    private Method getter;

    public Enumerated16FieldModel(String fieldName, Class<T> type, int fieldNumber) {
        this.fieldName = fieldName;
        this.type = type;
        this.fieldNumber = fieldNumber;
    }

    @Override
    public Object arrayOfField(int size) {
        return newArrayOfField(size);
    }

    public static CharBuffer newArrayOfField(int size) {
        return ByteBuffer.allocateDirect(size * 2).asCharBuffer();
    }

    @Override
    public T getAllocation(Object[] arrays, int index) {
        CharBuffer array = (CharBuffer) arrays[fieldNumber];
        return get(array, index);
    }

    public T get(CharBuffer array, int index) {
        return list.get(array.get(index));
    }

    @Override
    public void setAllocation(Object[] arrays, int index, T value) {
        CharBuffer array = (CharBuffer) arrays[fieldNumber];
        set(array, index, value);
    }

    public void set(CharBuffer array, int index, T value) {
        Character ordinal = map.get(value);
        if (ordinal == null) {
            ordinal = (char) map.size();
            map.put(value, ordinal);
            list.add(ordinal, value);
        }
        array.put(index, ordinal);
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
    public Class<T> type() {
        return type;
    }
}
