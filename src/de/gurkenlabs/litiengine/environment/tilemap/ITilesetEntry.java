package de.gurkenlabs.litiengine.environment.tilemap;

import java.awt.image.BufferedImage;
import javax.annotation.Nullable;

public interface ITilesetEntry extends ICustomPropertyProvider {

  public int getId();

  @Nullable public ITerrain[] getTerrain();

  @Nullable public ITileAnimation getAnimation();

  /**
   * Gets the current image for this tileset entry.
   * @return The current image for this tileset entry, accounting for animation.
   */
  public BufferedImage getImage();

  /**
   * Gets the "standard" image for this tileset entry, without applying any animations.
   * @return The standard image for this tileset entry
   */
  @Nullable public BufferedImage getBasicImage();

  /**
   * Gets the tileset that this entry belongs to.
   * @return The tileset for this entry
   */
  public ITileset getTileset();

  @Nullable public String getType();

  @Nullable public IMapObjectLayer getCollisionInfo();
}
