package de.gurkenlabs.litiengine.environment.tilemap.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import de.gurkenlabs.litiengine.environment.tilemap.ITerrain;
import javax.annotation.Nullable;

@XmlAccessorType(XmlAccessType.FIELD)
public class Terrain extends CustomPropertyProvider implements ITerrain {
  public static final int NONE = -1;

  @XmlAttribute @Nullable
  private String name;

  @XmlAttribute
  private int tile;

  @Override @Nullable
   public String getName() {
    return this.name;
  }

  @Override
  public int getTile() {
    return this.tile;
  }
}