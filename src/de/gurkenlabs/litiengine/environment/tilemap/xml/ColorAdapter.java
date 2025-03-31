package de.gurkenlabs.litiengine.environment.tilemap.xml;

import de.gurkenlabs.litiengine.util.ColorHelper;
import java.awt.Color;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ColorAdapter extends XmlAdapter<String, Color> {

  @Nullable
  @Override
  public Color unmarshal(String v) {
    return ColorHelper.decode(v);
  }

  @Nullable
  @Override
  public String marshal(Color v) {
    return ColorHelper.encode(v);
  }
}
