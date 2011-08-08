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
import vanilla.java.collections.api.HugeIterator;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MutableTypeArrayList extends AbstractList<MutableTypes> implements HugeArrayList<MutableTypes> {
    final int allocationSize;
    final List<MutableTypesAllocation> allocations = new ArrayList<MutableTypesAllocation>();
    private final List<MutableTypesElement> proxies = new ArrayList<MutableTypesElement>();
    long longSize;

    public MutableTypeArrayList(int allocationSize) {
        this.allocationSize = allocationSize;
    }

    public void ensureCapacity(long size) {
        long blocks = (size + allocationSize - 1) / allocationSize;
        while (blocks > allocations.size()) {
            allocations.add(new MutableTypesAllocation(allocationSize));
        }
    }

    @Override
    public int size() {
        return longSize < Integer.MAX_VALUE ? (int) longSize : Integer.MAX_VALUE;
    }

    @Override
    public long longSize() {
        return longSize;
    }

    @Override
    public void setSize(long length) {
        ensureCapacity(length);
        longSize = length;
    }

    @Override
    public MutableTypes get(long n) throws IndexOutOfBoundsException {
        return acquireElement(n);
    }

    MutableTypesElement acquireElement(long n) {
        if (proxies.isEmpty())
            return new MutableTypesElement(this, n);
        MutableTypesElement mte = proxies.remove(proxies.size() - 1);
        mte.index(n);
        return mte;
    }

    @Override
    public HugeIterator<MutableTypes> iterator() {
        return listIterator();
    }

    @Override
    public HugeIterator<MutableTypes> listIterator() {
        return new MutableTypesListIterator(this);
    }

    @Override
    public ListIterator<MutableTypes> listIterator(int index) {
        HugeIterator<MutableTypes> iterator = listIterator();
        iterator.index(index);
        return iterator;
    }

    @Override
    public void recycle(MutableTypes t) {
        if (t instanceof MutableTypesElement)
            proxies.add((MutableTypesElement) t);
    }

    @Override
    public MutableTypes get(int index) {
        return get(index & 0xFFFFFFFFL);
    }

    @Override
    public MutableTypes set(int index, MutableTypes element) {
        return set((long) index, element);
    }

    @Override
    public MutableTypes set(long n, MutableTypes mutableTypes) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }
}
