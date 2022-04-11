package de.gurkenlabs.litiengine.environment.tilemap.xml;

import javax.annotation.Nullable;
import java.awt.Color;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import de.gurkenlabs.litiengine.util.ColorHelper;

public class ColorAdapter extends XmlAdapter<String, Color> {

    @Override
    @Nullable
    public Color unmarshal(String v) {
        return ColorHelper.decode(v);
    }

    @Override
    @Nullable
    public String marshal(Color v) {
        return ColorHelper.encode(v);
    }
}
