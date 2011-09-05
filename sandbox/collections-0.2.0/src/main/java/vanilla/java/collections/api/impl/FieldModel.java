/*
 * Copyright (c) 2011 Peter Lawrey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vanilla.java.collections.api.impl;

import java.io.Flushable;
import java.lang.reflect.Method;

public interface FieldModel<T> extends Flushable {
  public void setter(Method setter);

  public void getter(Method getter);

  public Method setter();

  public Method getter();

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
