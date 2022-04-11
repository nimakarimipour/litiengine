package de.gurkenlabs.litiengine.environment.tilemap;

import javax.annotation.Nullable;
import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;

public interface IMapImage extends ICustomPropertyProvider {

    // XXX merge with getSource
    @Nullable
    public URL getAbsoluteSourcePath();

    /**
     * Gets the width.
     *
     * @return the width
     */
    public int getWidth();

    /**
     * Gets the height.
     *
     * @return the height
     */
    public int getHeight();

    /**
     * Gets the dimension.
     *
     * @return the dimension
     */
    public Dimension getDimension();

    /**
     * Gets the source.
     *
     * @return the source
     */
    @Nullable
    public String getSource();

    /**
     * Gets the transparent color.
     *
     * @return the transparent color
     */
    @Nullable
    public Color getTransparentColor();

    /**
     * Tests for equality between two map images. Two map images
     * are <i>equal</i> if they have the same absolute source path
     * and the same transparent color.
     *
     * @param anObject
     *          The map image to test for equality with
     * @return Whether this map image is equal to the provided map
     *         image, or {@code false} if {@code anObject} is not a map
     *         image
     */
    public boolean equals(Object anObject);

    public void setTransparentColor(Color color);

    // TODO change this to a URL
    public void setSource(String source);

    // XXX merge with setSource
    public void setAbsoluteSourcePath(URL absolutePath);

    public void setWidth(int width);

    public void setHeight(int height);
}
