package de.gurkenlabs.litiengine.environment.tilemap;

import javax.annotation.Nullable;
import java.util.List;

public interface ITileAnimation {

    @Nullable
    public List<ITileAnimationFrame> getFrames();

    public int getTotalDuration();

    public ITileAnimationFrame getCurrentFrame();
}
