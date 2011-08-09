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
import vanilla.java.collections.model.Enum8FieldModel;
import vanilla.java.collections.model.Enumerated16FieldModel;

import java.lang.annotation.ElementType;

public class MutableTypesArrayList extends AbstractHugeArrayList<MutableTypes, MutableTypesAllocation, MutableTypesElement> {
    final Enum8FieldModel<ElementType> elementTypeFieldModel
            = new Enum8FieldModel<ElementType>("elementType", 10, ElementType.class, ElementType.values());
    final Enumerated16FieldModel<String> stringEnumerated16FieldModel
            = new Enumerated16FieldModel<String>("text", 11, String.class);

    public MutableTypesArrayList(int allocationSize) {
        super(allocationSize);
    }

    @Override
    protected MutableTypesAllocation createAllocation() {
        return new MutableTypesAllocation(allocationSize);
    }

    @Override
    protected MutableTypesElement createElement(long n) {
        return new MutableTypesElement(this, n);
    }
}
