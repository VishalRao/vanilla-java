package vanilla.java.collections.api.impl;

import java.io.IOException;
import java.util.List;

public interface ContainerMeta {
  public void load() throws IOException;

  public void save() throws IOException;

  public List<ContainerField> containerFields();
}
