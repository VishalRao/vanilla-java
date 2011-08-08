package vanilla.java.collections;

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

import vanilla.java.collections.model.*;

import java.lang.annotation.ElementType;

public class MutableTypesElement implements MutableTypes {
    private static final Enum8FieldModel<ElementType> ELEMENT_TYPE_FIELD_MODEL
            = new Enum8FieldModel<ElementType>("elementType", ElementType.class, 10, ElementType.values());
    private static final Enumerated16FieldModel<String> STRING_ENUMERATED_16_FIELD_MODEL
            = new Enumerated16FieldModel<String>("text", String.class, 11);
    private final MutableTypeArrayList list;
    MutableTypesAllocation allocation = null;
    long index;
    int offset;

    public MutableTypesElement(MutableTypeArrayList list, long n) {
        this.list = list;
        index = n;
        updateAllocation();
    }

    @Override
    public void setBoolean(boolean b) {
        BooleanFieldModel.set(allocation.m_boolean, offset, b);
    }

    @Override
    public boolean getBoolean() {
        return BooleanFieldModel.get(allocation.m_boolean, offset);
    }

    @Override
    public void setBoolean2(Boolean b) {
        Boolean2FieldModel.set(allocation.m_boolean2, offset, b);
    }

    @Override
    public Boolean getBoolean2() {
        return Boolean2FieldModel.get(allocation.m_boolean2, offset);
    }

    @Override
    public void setByte(byte b) {
        ByteFieldModel.set(allocation.m_byte, offset, b);
    }

    @Override
    public byte getByte() {
        return ByteFieldModel.get(allocation.m_byte, offset);
    }

    @Override
    public void setByte2(Byte b) {
        Byte2FieldModel.set(allocation.m_byte2, offset, b);
    }

    @Override
    public Byte getByte2() {
        return Byte2FieldModel.get(allocation.m_byte2, offset);
    }

    @Override
    public void setChar(char ch) {
        CharFieldModel.set(allocation.m_char, offset, ch);
    }

    @Override
    public char getChar() {
        return CharFieldModel.get(allocation.m_char, offset);
    }

    @Override
    public void setShort(short s) {
        ShortFieldModel.set(allocation.m_short, offset, s);
    }

    @Override
    public short getShort() {
        return ShortFieldModel.get(allocation.m_short, offset);
    }

    @Override
    public void setInt(int i) {
        IntFieldModel.set(allocation.m_int, offset, i);
    }

    @Override
    public int getInt() {
        return IntFieldModel.get(allocation.m_int, offset);
    }

    @Override
    public void setFloat(float f) {
        FloatFieldModel.set(allocation.m_float, offset, f);
    }

    @Override
    public float getFloat() {
        return FloatFieldModel.get(allocation.m_float, offset);
    }

    @Override
    public void setLong(long l) {
        LongFieldModel.set(allocation.m_long, offset, l);
    }

    @Override
    public long getLong() {
        return LongFieldModel.get(allocation.m_long, offset);
    }

    @Override
    public void setDouble(double d) {
        DoubleFieldModel.set(allocation.m_double, offset, d);
    }

    @Override
    public double getDouble() {
        return DoubleFieldModel.get(allocation.m_double, offset);
    }

    @Override
    public void setElementType(ElementType elementType) {
        ELEMENT_TYPE_FIELD_MODEL.set(allocation.m_elementType, offset, elementType);
    }

    @Override
    public ElementType getElementType() {
        return ELEMENT_TYPE_FIELD_MODEL.get(allocation.m_elementType, offset);
    }

    @Override
    public void setString(String text) {
        STRING_ENUMERATED_16_FIELD_MODEL.set(allocation.m_string, offset, text);
    }

    @Override
    public String getString() {
        return STRING_ENUMERATED_16_FIELD_MODEL.get(allocation.m_string, offset);
    }

    public void index(long n) {
        index = n;
        offset = (int) (index % list.allocationSize);
        if (offset < 0) offset += list.allocationSize;
    }

    void next() {
        if (index >= list.longSize)
            list.ensureCapacity(index);
        index++;
        if (++offset >= list.allocationSize)
            updateAllocation();
    }

    public MutableTypes previous() {
        index--;
        if (offset > 0) {
            offset--;
        } else {
            updateAllocation();
        }
        return this;
    }

    private void updateAllocation() {
        int allocationSize = list.allocationSize;
        if (index >= 0)
            allocation = list.allocations.get((int) (index / allocationSize));
        offset = (int) (index % allocationSize);
        if (offset < 0) offset += allocationSize;
    }
}
