package de.gurkenlabs.litiengine.environment.tilemap.xml;

import de.gurkenlabs.litiengine.environment.tilemap.IMapImage;
import java.awt.Color;
import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class MapImage extends CustomPropertyProvider implements IMapImage {

  @Nullable @XmlAttribute private String source;

  @Nullable
  @XmlAttribute(name = "trans")
  @XmlJavaTypeAdapter(ColorAdapter.class)
  private Color transparentcolor;

  @XmlAttribute private int width;

  @XmlAttribute private int height;

  @Nullable @XmlTransient private URL absolutePath;

  /** Instantiates a new {@code MapImage} instance. */
  public MapImage() {
    super();
  }

  /**
   * Instantiates a new {@code MapImage} instance by copying the specified original.
   *
   * @param original the original we want to copy
   */
  public MapImage(MapImage original) {
    super(original);

    if (original == null) {
      return;
    }

    this.source = original.getSource();
    if (original.getTransparentColor() != null) {
      this.transparentcolor =
          new Color(
              original.getTransparentColor().getRed(),
              original.getTransparentColor().getGreen(),
              original.getTransparentColor().getBlue(),
              original.getTransparentColor().getAlpha());
    }
    this.width = original.getWidth();
    this.height = original.getHeight();
    this.absolutePath = original.getAbsoluteSourcePath();
  }

  @Nullable
  @Override
  public URL getAbsoluteSourcePath() {
    return this.absolutePath;
  }

  @Override
  public Dimension getDimension() {
    return new Dimension(this.getWidth(), this.getHeight());
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Nullable
  @Override
  public String getSource() {
    return this.source;
  }

  @Nullable
  @Override
  public Color getTransparentColor() {
    return this.transparentcolor;
  }

  @Override
  public void setTransparentColor(Color color) {
    this.transparentcolor = color;
  }

  @Override
  public void setSource(String source) {
    this.source = source;
  }

  @Override
  void finish(@Nullable URL location) throws TmxException {
    super.finish(location);
    try {
      this.absolutePath = new URL(location, this.source);
    } catch (MalformedURLException e) {
      throw new MissingImageException(e);
    }
  }

  @Override
  public void setAbsoluteSourcePath(URL absolutePath) {
    this.absolutePath = absolutePath;
  }

  @Override
  public void setWidth(int width) {
    this.width = width;
  }

  @Override
  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public boolean equals(Object anObject) {
    if (!(anObject instanceof IMapImage)) {
      return false;
    }

    if (this == anObject) {
      return true;
    }

    IMapImage other = (IMapImage) anObject;
    return Objects.equals(this.getTransparentColor(), other.getTransparentColor())
        && Objects.equals(this.getAbsoluteSourcePath(), other.getAbsoluteSourcePath());
  }

  /**
   * Computes a hash code for this map image. The hash code for a map image is equal to the hash
   * code of its absolute source path xor the hash code of its transparent color.
   *
   * @return The hash code for this map image
   */
  @Override
  public int hashCode() {
    return (this.getAbsoluteSourcePath() == null ? 1 : this.getAbsoluteSourcePath().hashCode())
        ^ (this.getTransparentColor() == null ? 1 : this.getTransparentColor().hashCode());
  }

  @Override
  public String toString() {
    return this.getAbsoluteSourcePath() == null
        ? "null"
        : this.getAbsoluteSourcePath().toExternalForm();
  }
}
