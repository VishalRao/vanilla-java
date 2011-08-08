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

import vanilla.java.collections.api.HugeArrayList;
import vanilla.java.collections.impl.ColumnHugeArrayList;
import vanilla.java.collections.model.TypeModel;

import java.lang.reflect.ParameterizedType;

public class HugeArrayBuilder<T> {
    public static final int MIN_ALLOCATION_SIZE = 32 * 1024;
    private final Class<T> type;
    protected int allocationSize = -1;
    protected boolean fixedSize;
    protected boolean entryBased;
    protected long capacity = -1;
    private final TypeModel<T> typeModel;

    protected HugeArrayBuilder() {
        type = (Class) ((ParameterizedType) this.getClass().
                getGenericSuperclass()).getActualTypeArguments()[0];
        typeModel = new TypeModel<T>(type);
    }

    public HugeArrayBuilder(Class<T> type) {
        this.type = type;
        typeModel = new TypeModel<T>(type);
    }

    public HugeArrayBuilder<T> allocationSize(int allocationSize) {
        this.allocationSize = allocationSize;
        return this;
    }

    public int allocationSize() {
        return allocationSize;
    }

    public HugeArrayBuilder capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public long capacity() {
        return Math.max(Math.max(allocationSize, capacity), MIN_ALLOCATION_SIZE);
    }

    public HugeArrayBuilder<T> fixedSize(boolean fixedSize) {
        this.fixedSize = fixedSize;
        return this;
    }

    public boolean fixedSize() {
        return fixedSize;
    }

    public HugeArrayBuilder<T> entryBased(boolean entryBased) {
        this.entryBased = entryBased;
        return this;
    }

    public boolean entryBased() {
        return entryBased;
    }

    public HugeArrayList<T> create() {
        if (capacity < 1) capacity = 1;
        if (allocationSize < MIN_ALLOCATION_SIZE) {
            allocationSize = MIN_ALLOCATION_SIZE;
            while (128 * allocationSize < capacity && allocationSize < 64 * 1024 * 1024)
                allocationSize <<= 1;
        }
        return new ColumnHugeArrayList<T>(typeModel, allocationSize, capacity);
    }
}
