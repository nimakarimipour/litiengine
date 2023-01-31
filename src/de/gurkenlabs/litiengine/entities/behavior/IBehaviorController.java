package de.gurkenlabs.litiengine.entities.behavior;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.IEntityController;
import de.gurkenlabs.litiengine.NullUnmarked;

public interface IBehaviorController extends IEntityController {

  @NullUnmarked public default void detach() {
    Game.loop().detach(this);
  }

  @NullUnmarked public default void attach() {
    Game.loop().attach(this);
  }
}
