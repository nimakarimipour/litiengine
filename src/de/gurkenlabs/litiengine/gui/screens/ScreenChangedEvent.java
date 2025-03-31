package de.gurkenlabs.litiengine.gui.screens;

import java.util.EventObject;
import javax.annotation.Nullable;

public class ScreenChangedEvent extends EventObject {
  private static final long serialVersionUID = 6145911214616836674L;
  @Nullable private final transient Screen previous;
  @Nullable private final transient Screen changed;

  public ScreenChangedEvent(@Nullable Screen changed, @Nullable Screen previous) {
    super(changed);
    this.previous = previous;
    this.changed = changed;
  }

  @Nullable public Screen getPrevious() {
    return this.previous;
  }

  @Nullable public Screen getChanged() {
    return this.changed;
  }
}
