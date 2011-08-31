package vanilla.java.collections.api.impl;

import java.lang.reflect.Method;

public interface MethodModel<T> {
  public MethodType methodType();

  public FieldModel<T> fieldModel();

  public Method method();
}
