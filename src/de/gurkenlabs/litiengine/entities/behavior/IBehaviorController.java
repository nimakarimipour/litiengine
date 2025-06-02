package de.gurkenlabs.litiengine.entities.behavior;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.IEntityController;

public interface IBehaviorController extends IEntityController {

  public default void detach() {
    NullabilityUtil.castToNonnull(Game.loop(), "initialized before use").detach(this);
  }

  public default void attach() {
    NullabilityUtil.castToNonnull(Game.loop(), "initialized in init method").attach(this);
  }
}
