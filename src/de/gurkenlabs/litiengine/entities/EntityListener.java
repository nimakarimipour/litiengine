package de.gurkenlabs.litiengine.entities;

import java.util.EventListener;

import de.gurkenlabs.litiengine.environment.Environment;
import javax.annotation.Nullable;

public interface EntityListener extends EventListener {

  default void loaded(IEntity entity, Environment environment) {}

  default void removed(IEntity entity, Environment environment) {}
}
