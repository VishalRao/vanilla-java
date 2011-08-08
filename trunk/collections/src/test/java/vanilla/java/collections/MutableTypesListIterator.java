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

import vanilla.java.collections.api.HugeIterator;

public class MutableTypesListIterator implements HugeIterator<MutableTypes> {
    private final MutableTypeArrayList list;
    private final MutableTypesElement mte;

    public MutableTypesListIterator(MutableTypeArrayList list) {
        this.list = list;
        mte = list.acquireElement(-1);
    }

    @Override
    public boolean hasNext() {
        return mte.index < list.longSize;
    }

    @Override
    public MutableTypes next() {
        mte.next();
        return mte;
    }

    @Override
    public boolean hasPrevious() {
        return mte.index > 0;
    }

    @Override
    public MutableTypes previous() {
        return mte.previous();
    }

    @Override
    public int nextIndex() {
        return (int) Math.min(nextLongIndex(), Integer.MAX_VALUE);
    }

    @Override
    public int previousIndex() {
        return (int) Math.min(previousLongIndex(), Integer.MAX_VALUE);
    }

    @Override
    public void remove() {
    }

    @Override
    public void set(MutableTypes mutableTypes) {
    }

    @Override
    public void add(MutableTypes mutableTypes) {
    }

    @Override
    public HugeIterator<MutableTypes> toStart() {
        mte.index(-1);
        return this;
    }

    @Override
    public HugeIterator<MutableTypes> toEnd() {
        mte.index(list.longSize);
        return this;
    }

    @Override
    public HugeIterator<MutableTypes> index(long n) {
        mte.index(n);
        return this;
    }

    @Override
    public long previousLongIndex() {
        return mte.index - 1;
    }

    @Override
    public long nextLongIndex() {
        return mte.index + 1;
    }

    @Override
    public void recycle() {
    }
}
