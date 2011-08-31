package vanilla.java.collections.api.impl;

import java.lang.reflect.Method;

public interface TypeModel {
  void addClass(Class type);

  ClassLoader classLoader();

  FieldModel[] fields();

  MethodModel method(Method method);

  int recordSize(int elements);

  // for bytecode generation
  public String bcType();
}
