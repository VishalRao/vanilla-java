package vanilla.java.collections.impl;

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
import vanilla.java.collections.api.HugeListIterator;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class AbstractHugeArrayList<T, TA, TE extends AbstractHugeElement<TA>> extends AbstractList<T> implements HugeArrayList<T> {
    protected final int allocationSize;
    protected final List<TA> allocations = new ArrayList<TA>();
    protected final List<TE> proxies = new ArrayList<TE>();
    protected long longSize;

    public AbstractHugeArrayList(int allocationSize) {
        this.allocationSize = allocationSize;
    }

    public void ensureCapacity(long size) {
        long blocks = (size + allocationSize - 1) / allocationSize;
        while (blocks > allocations.size()) {
            allocations.add(createAllocation());
        }
    }

    protected abstract TA createAllocation();

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
    public T get(long n) throws IndexOutOfBoundsException {
        return (T) acquireElement(n);
    }

    TE acquireElement(long n) {
        if (proxies.isEmpty())
            return createElement(n);
        TE mte = proxies.remove(proxies.size() - 1);
        mte.index(n);
        return mte;
    }

    protected abstract TE createElement(long n);

    @Override
    public HugeIterator<T> iterator() {
        return listIterator();
    }

    @Override
    public HugeListIterator<T> listIterator() {
        return new HugeListIteratorImpl<T, TA, TE>(this);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        HugeListIterator<T> iterator = listIterator();
        iterator.index(index);
        return iterator;
    }

    @Override
    public void recycle(T t) {
        proxies.add((TE) t);
    }

    @Override
    public T get(int index) {
        return get(index & 0xFFFFFFFFL);
    }

    @Override
    public T set(int index, T element) {
        return set((long) index, element);
    }

    @Override
    public T set(long n, T mutableTypes) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }

    public TA getAllocation(long index) {
        return allocations.get((int) (index / allocationSize));
    }
}
