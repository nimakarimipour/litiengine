package de.gurkenlabs.litiengine.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.gurkenlabs.litiengine.util.io.FileUtilities;
import de.gurkenlabs.litiengine.util.io.XmlUtilities;
import javax.annotation.Nullable;
import org.jspecify.annotations.NullUnmarked;

@XmlRootElement(name = "TextureAtlas")
public class TextureAtlas {
  private static final Logger log = Logger.getLogger(TextureAtlas.class.getName());

  @Nullable @XmlAttribute(name = "imagePath")
  private String rawImagePath;

  @XmlAttribute
  private int width;

  @XmlAttribute
  private int height;

  @Nullable @XmlElement(name = "sprite")
  private List<Sprite> sprites;

  @Nullable private String absoluteImagePath;

  TextureAtlas() {
    // keep for serialization
  }

  @Nullable public static TextureAtlas read(String textureAtlasFile) {
    try {
      TextureAtlas atlas = XmlUtilities.read(TextureAtlas.class, Resources.getLocation(textureAtlasFile));
      if (atlas == null) {
        return null;
      }

      String directory = FileUtilities.getParentDirPath(textureAtlasFile);
      atlas.absoluteImagePath = FileUtilities.combine(directory, atlas.rawImagePath);
      return atlas;
    } catch (JAXBException e) {
      log.log(Level.SEVERE, "TextureAtlas " + textureAtlasFile + " could not be read.", e);
      return null;
    }
  }

  @Nullable @XmlTransient
  public String getAbsoluteImagePath() {
    return this.absoluteImagePath;
  }

  @XmlTransient
  public int getWidth() {
    return this.width;
  }

  @XmlTransient
  public int getHeight() {
    return this.height;
  }

  @XmlTransient
  public List<Sprite> getSprites() {
    if (this.sprites == null) {
      this.sprites = new ArrayList<>();
    }

    return this.sprites;
  }

  @NullUnmarked @Nullable public Sprite getSprite(String name) {
    if (name == null || name.isEmpty()) {
      return null;
    }

    return this.getSprites().stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
  }

  public void setImagePath(String imagePath) {
    this.rawImagePath = imagePath;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setSprites(List<Sprite> sprites) {
    this.sprites = sprites;
  }

  @XmlRootElement(name = "sprite")
  public static class Sprite {
    @Nullable @XmlAttribute(name = "n")
    private String name;

    @XmlAttribute()
    private int x;

    @XmlAttribute()
    private int y;

    @XmlAttribute(name = "w")
    private int width;

    @XmlAttribute(name = "h")
    private int height;

    @XmlAttribute(name = "oX")
    private int offsetX;

    @XmlAttribute(name = "oY")
    private int offsetY;

    @Nullable @XmlAttribute(name = "r")
    @XmlJavaTypeAdapter(CustomBooleanAdapter.class)
    private Boolean rotated;

    Sprite() {
      // keep for serialization
    }

    @Nullable @XmlTransient
    public String getName() {
      return this.name;
    }

    @XmlTransient
    public int getX() {
      return this.x;
    }

    @XmlTransient
    public int getY() {
      return this.y;
    }

    @XmlTransient
    public int getWidth() {
      return this.width;
    }

    @XmlTransient
    public int getHeight() {
      return this.height;
    }

    @XmlTransient
    public int getOffsetX() {
      return this.offsetX;
    }

    @XmlTransient
    public int getOffsetY() {
      return this.offsetY;
    }

    @XmlTransient
    public boolean isRotated() {
      return this.rotated != null && this.rotated;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setX(int x) {
      this.x = x;
    }

    public void setY(int y) {
      this.y = y;
    }

    public void setWidth(int width) {
      this.width = width;
    }

    public void setHeight(int height) {
      this.height = height;
    }

    public void setOffsetX(int offsetX) {
      this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
      this.offsetY = offsetY;
    }

    public void setRotated(boolean rotated) {
      this.rotated = rotated;
    }
  }

  public static class CustomBooleanAdapter extends XmlAdapter<String, Boolean> {

    @Override
    public Boolean unmarshal(String v) throws Exception {
      if (v == null || v.isEmpty()) {
        return false;
      }

      return v.equalsIgnoreCase("y") || v.equalsIgnoreCase("yes") || v.equals("1") || v.equalsIgnoreCase("true");
    }

    @Override
    public String marshal(Boolean v) throws Exception {
      return v.booleanValue() ? "y" : "n";
    }
  }
}
