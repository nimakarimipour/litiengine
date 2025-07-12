package de.gurkenlabs.litiengine.entities.behavior;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.IEntityController;
import edu.ucr.cs.riple.annotator.util.Nullability;

public interface IBehaviorController extends IEntityController {

  public void detach() {
    Nullability.castToNonnull(Game.loop(), "properly initialized before usage").detach(this);
  }

  public default void attach() {
    Game.loop().attach(this);
  }
}
