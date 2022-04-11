package de.gurkenlabs.litiengine.environment.tilemap;

import javax.annotation.Nullable;
import java.awt.Color;

public interface IImageLayer extends ICustomPropertyProvider, ILayer {

    /**
     * Gets the image.
     *
     * @return the image
     */
    @Nullable
    public IMapImage getImage();

    /**
     * Gets the transparent color.
     *
     * @return the transparent color
     */
    @Nullable
    public Color getTransparentColor();
}
