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

public class ContainerField {
  public String name;
  public int size;
  public String type;

  public ContainerField(String field) {
    String[] parts = field.split(":");
    name = parts[0];
    size = Integer.parseInt(parts[1]);
    type = parts[2];
  }

  public String toString() {
    return name + ':' + size + ':' + type;
  }
}
