package vanilla.java.collections.hand;

// need an example of each type rather than every possible type.
public interface HT {
  // primitive type
  void setInt(int i);

  int getInt();

  // mapped type
  void setText(String id);

  String getText();

  // Object type - not supported.
  // void setA(A a);
  // A getA();
}
