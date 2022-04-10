package de.gurkenlabs.litiengine.graphics.animation;

import javax.annotation.Nullable;
import java.util.EventListener;

public interface KeyFrameListener extends EventListener {

    public void currentFrameChanged(KeyFrame previousFrame, KeyFrame currentFrame);
}
