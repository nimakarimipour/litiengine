package de.gurkenlabs.litiengine.environment.tilemap.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.jspecify.annotations.NullUnmarked;

public class BooleanIntegerAdapter extends XmlAdapter<Integer, Boolean> {
  @NullUnmarked @Override
  public Boolean unmarshal(Integer s) {
    return s == null ? null : s == 1;
  }

  @NullUnmarked @Override
  public Integer marshal(Boolean c) {
    if (c == null) {
      return null;
    }

    return c ? 1 : 0;
  }
}
