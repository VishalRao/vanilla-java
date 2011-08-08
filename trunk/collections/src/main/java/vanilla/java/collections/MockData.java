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

public interface MockData {
    public MockData setBoolean(boolean b);

    public boolean getBoolean();

    public MockData setByte(byte b);

    public byte getByte();

    public MockData setShort(short s);

    public short getShort();

    public MockData setChar(char ch);

    public char getChar();

    public MockData setInt(int i);

    public int getInt();

    public MockData setFloat(float f);

    public float getFloat();

    public MockData setLong(long l);

    public long getLong();

    public MockData setDouble(double d);

    public double getDouble();
}
