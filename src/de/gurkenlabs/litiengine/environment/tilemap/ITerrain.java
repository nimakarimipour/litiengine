package de.gurkenlabs.litiengine.environment.tilemap;

import javax.annotation.Nullable;

public interface ITerrain extends ICustomPropertyProvider {

    @Nullable()
    public String getName();

    public int getTile();
}
