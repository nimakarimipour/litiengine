package de.gurkenlabs.litiengine.environment.tilemap.xml;

import javax.annotation.Nullable;

import java.io.IOException;

public class TmxException extends IOException {

  private static final long serialVersionUID = -2404149074008693966L;

  public TmxException() {
  }

  public TmxException(String message) {
    super(message);
  }

  public TmxException(@Nullable String message, Throwable cause) {
    super(message, cause);
  }

  public TmxException(Throwable cause) {
    super(cause);
  }
}
