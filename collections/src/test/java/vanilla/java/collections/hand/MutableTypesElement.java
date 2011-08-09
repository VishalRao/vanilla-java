package vanilla.java.collections.hand;

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

import vanilla.java.collections.MutableTypes;
import vanilla.java.collections.impl.AbstractHugeArrayList;
import vanilla.java.collections.impl.AbstractHugeElement;
import vanilla.java.collections.model.*;

import java.lang.annotation.ElementType;

public class MutableTypesElement extends AbstractHugeElement<MutableTypesAllocation> implements MutableTypes {
    MutableTypesAllocation allocation = null;

    public MutableTypesElement(AbstractHugeArrayList<MutableTypes, MutableTypesAllocation, MutableTypesElement> list, long n) {
        super(list, n);
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
        ((MutableTypesArrayList) list).elementTypeFieldModel.set(allocation.m_elementType, offset, elementType);
    }

    @Override
    public ElementType getElementType() {
        return ((MutableTypesArrayList) list).elementTypeFieldModel.get(allocation.m_elementType, offset);
    }

    @Override
    public void setString(String text) {
        ((MutableTypesArrayList) list).stringEnumerated16FieldModel.set(allocation.m_string, offset, text);
    }

    @Override
    public String getString() {
        return ((MutableTypesArrayList) list).stringEnumerated16FieldModel.get(allocation.m_string, offset);
    }

    @Override
    protected void updateAllocation0(int allocationSize) {
        allocation = list.getAllocation(index);
    }
}
