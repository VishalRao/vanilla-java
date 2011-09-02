package vanilla.java.collections.impl;

import vanilla.java.collections.api.impl.BCType;
import vanilla.java.collections.api.impl.FieldModel;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Enumerated16FieldModel<T> implements FieldModel<T> {
  private final Map<T, Character> map = new LinkedHashMap<T, Character>();
  private final List<T> list = new ArrayList<T>();

  {
    map.put(null, (char) 0);
    list.add(null);
  }

  @Override
  public String baseDirectory() {
    throw new Error("Not implemented");
  }

  @Override
  public void setter(Method setter) {
    throw new Error("Not implemented");
  }

  @Override
  public void getter(Method getter) {
    throw new Error("Not implemented");
  }

  @Override
  public void fieldNumber(int num) {
    throw new Error("Not implemented");
  }

  @Override
  public int fieldNumber() {
    throw new Error("Not implemented");
  }

  @Override
  public Method setter() {
    throw new Error("Not implemented");
  }

  @Override
  public Method getter() {
    throw new Error("Not implemented");
  }

  @Override
  public void baseDirectory(String baseDirectory) {
    throw new Error("Not implemented");
  }

  @Override
  public void clear() {
    throw new Error("Not implemented");
  }

  @Override
  public String fieldName() {
    throw new Error("Not implemented");
  }

  @Override
  public String titleFieldName() {
    throw new Error("Not implemented");
  }

  @Override
  public String bcStoreType() {
    throw new Error("Not implemented");
  }

  @Override
  public String bcLStoreType() {
    throw new Error("Not implemented");
  }

  @Override
  public String bcModelType() {
    throw new Error("Not implemented");
  }

  @Override
  public String bcLModelType() {
    throw new Error("Not implemented");
  }

  @Override
  public String bcFieldType() {
    throw new Error("Not implemented");
  }

  @Override
  public String bcLStoredType() {
    throw new Error("Not implemented");
  }

  @Override
  public String bcLFieldType() {
    throw new Error("Not implemented");
  }

  @Override
  public String bcLSetType() {
    throw new Error("Not implemented");
  }

  @Override
  public int bcFieldSize() {
    throw new Error("Not implemented");
  }

  @Override
  public BCType bcType() {
    throw new Error("Not implemented");
  }

  @Override
  public boolean virtualGetSet() {
    throw new Error("Not implemented");
  }

  @Override
  public boolean copySimpleValue() {
    throw new Error("Not implemented");
  }

  @Override
  public boolean isCallsNotEquals() {
    throw new Error("Not implemented");
  }

  @Override
  public boolean isCallsHashCode() {
    throw new Error("Not implemented");
  }

  @Override
  public boolean isBufferStore() {
    throw new Error("Not implemented");
  }

  @Override
  public boolean isCompacting() {
    throw new Error("Not implemented");
  }

  @Override
  public void flush() throws IOException {
    throw new Error("Not implemented");
  }

  public T get(CharBuffer buffer, int offset) {
    char ch = buffer.get(offset);
    try {
      return list.get(ch);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalStateException("Object id " + (int) ch + " is not valid, must be less than " + list.size(), e);
    }
  }

  public void set(CharBuffer buffer, int offset, T id) {
    Character ch = map.get(id);
    if (ch == null) {
      final int size = list.size();
      if (size >= Character.MAX_VALUE)
        throw new IllegalStateException("Cannot enumerate more than " + Character.MAX_VALUE + " values in a partition.");
      list.add(id);
      ch = (char) size;
      map.put(id, ch);
    }
    buffer.put(offset, ch);
  }
}
