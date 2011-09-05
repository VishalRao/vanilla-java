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
