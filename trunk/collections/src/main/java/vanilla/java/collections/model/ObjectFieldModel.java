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

import java.lang.reflect.Array;
import java.lang.reflect.Method;

public class ObjectFieldModel<T> implements FieldModel<T> {
    private final String fieldName;
    private final Class<T> type;
    private final int fieldNumber;
    private Method setter;
    private Method getter;

    public ObjectFieldModel(String fieldName, Class<T> type, int fieldNumber) {
        this.fieldName = fieldName;
        this.type = type;
        this.fieldNumber = fieldNumber;
    }

    @Override
    public Object arrayOfField(int size) {
        return newArrayOfField(type, size);
    }

    public static <T> T[] newArrayOfField(Class<T> type, int size) {
        return (T[]) Array.newInstance(type, size);
    }

    @Override
    public T getAllocation(Object[] arrays, int index) {
        T[] array = (T[]) arrays[fieldNumber];
        return get(array, index);
    }

    public static <T> T get(T[] array, int index) {
        return array[index];
    }

    @Override
    public void setAllocation(Object[] arrays, int index, T value) {
        T[] array = (T[]) arrays[fieldNumber];
        set(array, index, value);
    }

    public static <T> void set(T[] array, int index, T value) {
        array[index] = value;
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
