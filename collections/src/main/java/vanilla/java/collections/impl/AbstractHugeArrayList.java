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

import vanilla.java.collections.api.*;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class AbstractHugeArrayList<T, TA extends HugeAllocation, TE extends AbstractHugeElement<T, TA>> extends AbstractList<T> implements HugeArrayList<T> {
    protected final int allocationSize;
    protected final boolean setRemoveReturnsNull;
    protected final List<TA> allocations = new ArrayList<TA>();
    protected final List<TE> elements = new ArrayList<TE>();
    protected final List<T> impls = new ArrayList<T>();
    protected long longSize;

    public AbstractHugeArrayList(int allocationSize, boolean setRemoveReturnsNull) {
        this.allocationSize = allocationSize;
        this.setRemoveReturnsNull = setRemoveReturnsNull;
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
        if (elements.isEmpty())
            return createElement(n);
        TE mte = elements.remove(elements.size() - 1);
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
        if (t == null) return;
        switch (((HugeElement) t).hugeElementType()) {
            case Element:
                if (elements.size() < allocationSize)
                    elements.add((TE) t);
                break;
            case BeanImpl:
                if (impls.size() < allocationSize)
                    impls.add(t);
                break;
        }
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
    public T set(long index, T element) throws IndexOutOfBoundsException {
        if (index > longSize) throw new IndexOutOfBoundsException();
        if (index == longSize) longSize++;
        ensureCapacity(longSize);
        if (setRemoveReturnsNull) {
            final T t = get(index);
            ((HugeElement<T>) t).copyOf(element);
            return null;
        }
        final T t0 = acquireImpl();
        final T t = get(index);
        ((HugeElement<T>) t0).copyOf(t);
        ((HugeElement<T>) t).copyOf(element);
        recycle(t);
        return t0;
    }

    public TA getAllocation(long index) {
        return allocations.get((int) (index / allocationSize));
    }

    @Override
    public boolean add(T t) {
        set(longSize(), t);
        return true;
    }

    @Override
    public void add(int index, T element) {
        add((long) index, element);
    }

    public void add(long index, T element) {
        if (index != size())
            throw new UnsupportedOperationException();
        set(index, element);
    }

    @Override
    public void clear() {
        for (TA allocation : allocations) {
            allocation.clear();
        }
        longSize = 0;
    }

    @Override
    public T remove(int index) {
        return remove((long) index);
    }

    @Override
    public T remove(long index) {
        if (index > longSize) throw new IndexOutOfBoundsException();
        if (setRemoveReturnsNull) {
            final T t = get(index);
            if (index < longSize - 1) {
                final T t2 = get(index);
                ((HugeElement) t).copyOf(t2);
                recycle(t2);
            }
            recycle(t);
            longSize--;
            return null;
        }
        T impl = acquireImpl();
        final T t = get(index);
        ((HugeElement<T>) impl).copyOf(t);
        if (index < longSize - 1) {
            final T t2 = get(index);
            ((HugeElement) t).copyOf(t2);
            recycle(t2);
        }
        recycle(t);
        longSize--;
        return impl;
    }

    protected T acquireImpl() {
        if (impls.isEmpty())
            return createImpl();
        return impls.remove(impls.size() - 1);
    }

    protected abstract T createImpl();
}
