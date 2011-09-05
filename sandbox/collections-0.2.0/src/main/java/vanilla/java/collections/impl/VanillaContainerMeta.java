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

package vanilla.java.collections.impl;

import vanilla.java.collections.api.impl.ContainerField;
import vanilla.java.collections.api.impl.ContainerMeta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class VanillaContainerMeta implements ContainerMeta {
  private final List<ContainerField> containerFields = new ArrayList<ContainerField>();
  private final File baseDirectory;

  public VanillaContainerMeta(File baseDirectory) {
    this.baseDirectory = baseDirectory;
  }

  @Override
  public void load() throws IOException {
    Properties prop = new Properties();
    prop.load(new FileInputStream(new File(baseDirectory, "container.state")));
    containerFields.clear();
    String[] fields = prop.getProperty("fields").split(", ");
    for (String field : fields)
      containerFields.add(new ContainerField(field));
  }

  @Override
  public void save() throws IOException {
    Properties prop = new Properties();
    StringBuilder sb = new StringBuilder();
    String sep = "";
    for (ContainerField cf : containerFields) {
      sb.append(sep).append(cf);
      sep = ", ";
    }
    prop.put("fields", sb.toString());
  }

  @Override
  public List<ContainerField> containerFields() {
    return containerFields;
  }
}
