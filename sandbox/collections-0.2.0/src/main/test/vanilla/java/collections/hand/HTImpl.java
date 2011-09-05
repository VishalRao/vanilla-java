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

package vanilla.java.collections.hand;

import vanilla.java.collections.api.impl.Copyable;

class HTImpl implements HT, Copyable<HT> {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof HT)) return false;

    HT ht = (HT) o;

    if (getInt() != ht.getInt()) return false;
    if (!getText().equals(ht.getText())) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return getInt() * 31 + getText().hashCode();
  }

  @Override
  public String toString() {
    return "HT{" +
               "int=" + getInt() +
               ", text='" + getText() + '\'' +
               '}';
  }
}
