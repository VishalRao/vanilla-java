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

import vanilla.java.collections.model.*;

import java.nio.*;

public class MutableTypesAllocation {
    IntBuffer m_boolean;
    IntBuffer m_boolean2;
    ByteBuffer m_byte;
    ByteBuffer m_byte2;
    CharBuffer m_char;
    ShortBuffer m_short;
    IntBuffer m_int;
    FloatBuffer m_float;
    LongBuffer m_long;
    DoubleBuffer m_double;
    ByteBuffer m_elementType;
    CharBuffer m_string;

    public MutableTypesAllocation(int allocationSize) {
        m_boolean = BooleanFieldModel.newArrayOfField(allocationSize);
        m_boolean2 = Boolean2FieldModel.newArrayOfField(allocationSize);
        m_byte = ByteFieldModel.newArrayOfField(allocationSize);
        m_byte2 = Byte2FieldModel.newArrayOfField(allocationSize);
        m_char = CharFieldModel.newArrayOfField(allocationSize);
        m_short = ShortFieldModel.newArrayOfField(allocationSize);
        m_int = IntFieldModel.newArrayOfField(allocationSize);
        m_float = FloatFieldModel.newArrayOfField(allocationSize);
        m_long = LongFieldModel.newArrayOfField(allocationSize);
        m_double = DoubleFieldModel.newArrayOfField(allocationSize);
        m_elementType = Enum8FieldModel.newArrayOfField(allocationSize);
        m_string = Enumerated16FieldModel.newArrayOfField(allocationSize);
    }
}
