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
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Enumerated16FieldModel<T> extends AbstractFieldModel<T> {
    private final Class<T> type;
    private final Map<T, Character> map = new LinkedHashMap<T, Character>();
    private final List<T> list = new ArrayList<T>();

    public Enumerated16FieldModel(String fieldName, int fieldNumber, Class<T> type) {
        super(fieldName, fieldNumber);
        this.type = type;
    }

    @Override
    public Object arrayOfField(int size) {
        return newArrayOfField(size);
    }

    @Override
    public Class storeType() {
        return CharBuffer.class;
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
    public Class<T> type() {
        return type;
    }

    @Override
    public BCType bcType() {
        return BCType.Reference;
    }

    @Override
    public String bcLSetType() {
        return "Ljava/lang/Object;";
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
    public static <T> boolean notEquals(T t1, T t2) {
        return t1 == null ? t2 != null : !t1.equals(t2);
    }

    @UsedFromByteCode
    public static <T> int hashCode(T elementType) {
        return elementType == null ? Integer.MIN_VALUE : elementType.hashCode();
    }
}
