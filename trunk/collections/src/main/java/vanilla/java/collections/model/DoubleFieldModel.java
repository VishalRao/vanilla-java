package vanilla.java.collections.model;

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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;

public class DoubleFieldModel extends AbstractFieldModel<Double> {
    public DoubleFieldModel(String fieldName, int fieldNumber) {
        super(fieldName, fieldNumber);
    }

    @Override
    public Object arrayOfField(int size) {
        return newArrayOfField(size);
    }

    @Override
    public Class storeType() {
        return DoubleBuffer.class;
    }

    public static DoubleBuffer newArrayOfField(int size) {
        return ByteBuffer.allocateDirect(size * 8).order(ByteOrder.nativeOrder()).asDoubleBuffer();
    }

    @Override
    public Double getAllocation(Object[] arrays, int index) {
        DoubleBuffer array = (DoubleBuffer) arrays[fieldNumber];
        return get(array, index);
    }

    public static double get(DoubleBuffer array, int index) {
        return array.get(index);
    }

    @Override
    public void setAllocation(Object[] arrays, int index, Double value) {
        DoubleBuffer array = (DoubleBuffer) arrays[fieldNumber];
        set(array, index, value);
    }

    public static void set(DoubleBuffer array, int index, double value) {
        array.put(index, value);
    }

    @Override
    public Class<Double> type() {
        return (Class) double.class;
    }

    @Override
    public int bcFieldSize() {
        return 2;
    }

    @Override
    public String bcLFieldType() {
        return "D";
    }

    @Override
    public BCType bcType() {
        return BCType.Double;
    }
}
