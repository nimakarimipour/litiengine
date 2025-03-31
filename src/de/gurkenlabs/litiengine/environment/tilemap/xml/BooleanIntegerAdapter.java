package de.gurkenlabs.litiengine.environment.tilemap.xml;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BooleanIntegerAdapter extends XmlAdapter<Integer, Boolean> {
  @Nullable
  @Override
  public Boolean unmarshal(Integer s) {
    return s == null ? null : s == 1;
  }

  @Nullable
  @Override
  public Integer marshal(Boolean c) {
    if (c == null) {
      return null;
    }

    return c ? 1 : 0;
  }
}
