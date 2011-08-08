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
import java.nio.ByteOrder;
import java.nio.CharBuffer;

public class CharFieldModel implements FieldModel<Character> {
    private final String fieldName;
    private final int fieldNumber;
    private Method setter;
    private Method getter;

    public CharFieldModel(String fieldName, int fieldNumber) {
        this.fieldName = fieldName;
        this.fieldNumber = fieldNumber;
    }

    @Override
    public Object arrayOfField(int size) {
        return newArrayOfField(size);
    }

    public static CharBuffer newArrayOfField(int size) {
        return ByteBuffer.allocateDirect(size * 2).order(ByteOrder.nativeOrder()).asCharBuffer();
    }

    @Override
    public Character getAllocation(Object[] arrays, int index) {
        CharBuffer array = (CharBuffer) arrays[fieldNumber];
        return get(array, index);
    }

    public static char get(CharBuffer array, int index) {
        return array.get(index);
    }

    @Override
    public void setAllocation(Object[] arrays, int index, Character value) {
        CharBuffer array = (CharBuffer) arrays[fieldNumber];
        set(array, index, value);
    }

    public static void set(CharBuffer array, int index, char value) {
        array.put(index, value);
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
    public Class<Character> type() {
        return (Class) char.class;
    }
}
