package de.gurkenlabs.litiengine.graphics;

import javax.annotation.Nullable;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import de.gurkenlabs.litiengine.util.Imaging;

public class RotationImageEffect extends ImageEffect {
  private final float angle;

  /**
   * Initializes a new instance of the {@code RotationImageEffect}.
   *
   * @param ttl
   *          The time to live of this effect.
   * @param angle
   *          The angle by which this effect rotates the base image.
   */
  public RotationImageEffect(final int ttl, final float angle) {
    super(ttl, "RotationImageEffect");
    this.angle = angle;
  }

  @Override@Nullable
  public BufferedImage apply(final BufferedImage image) {
    if (image == null) {
      return null;
    }

    final int size = Math.max(image.getWidth(), image.getHeight()) * 2;
    final BufferedImage img = Imaging.getCompatibleImage(size, size);
    final Graphics2D g = img.createGraphics();
    ImageRenderer.renderRotated(g, image, new Point2D.Double(0, 0), this.getAngle());
    g.dispose();

    return img;
  }

  public double getAngle() {
    return this.angle;
  }
}
