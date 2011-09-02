package vanilla.java.collections.hand;

import vanilla.java.collections.api.impl.Copyable;

public class HTImpl implements HT, Copyable<HT> {
  private int m_int;
  private String m_text;

  public HTImpl() {
  }

  public HTImpl(int m_int, String m_text) {
    this.m_int = m_int;
    this.m_text = m_text;
  }

  @Override
  public int getInt() {
    return m_int;
  }

  @Override
  public void setInt(int i) {
    m_int = i;
  }

  @Override
  public void setText(String id) {
    m_text = id;
  }

  @Override
  public String getText() {
    return m_text;
  }

  @Override
  public void copyFrom(HT ht) {
    setInt(ht.getInt());
    setText(ht.getText());
  }

  @Override
  public HT copyOf() {
    return new HTImpl(m_int, m_text);
  }
}
