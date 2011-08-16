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

import vanilla.java.collections.ObjectTypes;
import vanilla.java.collections.api.HugeElement;
import vanilla.java.collections.api.HugeElementType;

import java.lang.annotation.ElementType;

public class HandTypesImpl implements HandTypes, HugeElement<HandTypes> {
    private boolean m_boolean;
    private Boolean m_boolean2;
    private byte m_byte;
    private Byte m_byte2;
    private char m_char;
    private short m_short;
    private int m_int;
    private float m_float;
    private long m_long;
    private double m_double;
    private ElementType m_elementType;
    private String m_string;
    private ObjectTypes.A m_a;

    @Override
    public void setBoolean(boolean b) {
        this.m_boolean = b;
    }

    @Override
    public boolean getBoolean() {
        return m_boolean;
    }

    @Override
    public void setBoolean2(Boolean b) {
        this.m_boolean2 = b;
    }

    @Override
    public Boolean getBoolean2() {
        return m_boolean2;
    }

    @Override
    public void setByte(byte b) {
        this.m_byte = b;
    }

    @Override
    public byte getByte() {
        return m_byte;
    }

    @Override
    public void setByte2(Byte b) {
        this.m_byte2 = b;
    }

    @Override
    public Byte getByte2() {
        return m_byte2;
    }

    @Override
    public void setChar(char ch) {
        this.m_char = ch;
    }

    @Override
    public char getChar() {
        return m_char;
    }

    @Override
    public void setShort(short s) {
        this.m_short = s;
    }

    @Override
    public short getShort() {
        return m_short;
    }

    @Override
    public void setInt(int i) {
        this.m_int = i;
    }

    @Override
    public int getInt() {
        return m_int;
    }

    @Override
    public void setFloat(float f) {
        this.m_float = f;
    }

    @Override
    public float getFloat() {
        return m_float;
    }

    @Override
    public void setLong(long l) {
        this.m_long = l;
    }

    @Override
    public long getLong() {
        return m_long;
    }

    @Override
    public void setDouble(double d) {
        this.m_double = d;
    }

    @Override
    public double getDouble() {
        return m_double;
    }

    @Override
    public void setElementType(ElementType elementType) {
        this.m_elementType = elementType;
    }

    @Override
    public ElementType getElementType() {
        return m_elementType;
    }

    @Override
    public void setString(String text) {
        this.m_string = text;
    }

    @Override
    public String getString() {
        return m_string;
    }

    public ObjectTypes.A getA() {
        return m_a;
    }

    public void setA(ObjectTypes.A a) {
        this.m_a = a;
    }

    @Override
    public void index(long n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long index() {
        return 0;
    }

    @Override
    public void copyOf(HandTypes t) {
        setBoolean(t.getBoolean());
        setBoolean2(t.getBoolean2());
        setByte(t.getByte());
        setByte2(t.getByte2());
        setChar(t.getChar());
        setDouble(t.getDouble());
        setElementType(t.getElementType());
        setFloat(t.getFloat());
        setInt(t.getInt());
        setLong(t.getLong());
        setShort(t.getShort());
        setString(t.getString());
        setA(t.getA());
    }

    @Override
    public HugeElementType hugeElementType() {
        return HugeElementType.BeanImpl;
    }
}
