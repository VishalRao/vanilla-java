package vanilla.java.collections.api.impl;

import java.io.Flushable;
import java.lang.reflect.Method;

public interface FieldModel<T> extends Flushable {
  public void setter(Method setter);

  public void getter(Method getter);

  public void fieldNumber(int num);

  public int fieldNumber();

  public Method setter();

  public Method getter();

  public void baseDirectory(String baseDirectory);

  public String baseDirectory();

  public void clear();

  // for bytecode generation
  public String fieldName();

  public String titleFieldName();

  public String bcStoreType();

  public String bcLStoreType();

  public String bcModelType();

  public String bcLModelType();

  public String bcFieldType();

  public String bcLStoredType();

  public String bcLFieldType();

  public String bcLSetType();

  public int bcFieldSize();

  public BCType bcType();

  public boolean virtualGetSet();

  public boolean copySimpleValue();

  public boolean isCallsNotEquals();

  public boolean isCallsHashCode();

  public boolean isBufferStore();

  public boolean isCompacting();
}
