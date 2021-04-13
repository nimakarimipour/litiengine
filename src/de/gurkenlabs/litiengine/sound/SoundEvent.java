package de.gurkenlabs.litiengine.sound;

import javax.annotation.Nullable;

import java.util.EventObject;

public class SoundEvent extends EventObject {
  private static final long serialVersionUID = -2070316328855430839L;

  private final transient Sound sound;

  SoundEvent(Object source, @Nullable Sound sound) {
    super(source);
    this.sound = sound;
  }

  /**
   * Gets the related {@code Sound} instance.
   * 
   * @return The sound object.
   */
  public Sound getSound() {
    return this.sound;
  }

  @Override
  public String toString() {
    return super.toString() + "[sound=" + this.sound.getName() + "]";
  }
}
