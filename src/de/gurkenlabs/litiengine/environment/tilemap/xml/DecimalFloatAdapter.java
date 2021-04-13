package de.gurkenlabs.litiengine.environment.tilemap.xml;

import javax.annotation.Nullable;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DecimalFloatAdapter extends XmlAdapter<String, Float> {

  @Override
  public Float unmarshal(String v) throws Exception {
    return Float.parseFloat(v);
  }

  @Override@Nullable
  public String marshal(Float v) throws Exception {
    if (v == null) {
      return null;
    }

    if (v.floatValue() % 1 == 0) {
      return Integer.toString(v.intValue());
    }

    return v.toString();
  }
}
