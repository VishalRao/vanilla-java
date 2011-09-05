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
