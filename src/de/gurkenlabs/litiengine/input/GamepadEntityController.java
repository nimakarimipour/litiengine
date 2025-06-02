package de.gurkenlabs.litiengine.input;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.IMobileEntity;
import de.gurkenlabs.litiengine.physics.MovementController;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;
import java.awt.geom.Point2D;

public class GamepadEntityController<T extends IMobileEntity> extends MovementController<T> {
  private int gamepadId = -1;
  private double gamepadDeadzone = Game.config().input().getGamepadStickDeadzone();
  private double gamepadRightStick = Game.config().input().getGamepadStickDeadzone();
  private boolean rotateWithRightStick = false;

  public GamepadEntityController(final T entity, boolean rotateWithRightStick) {
    super(entity);
    GamepadManager gamepadManager = Input.gamepads();

    if (gamepadManager != null && gamepadManager.current() != null) {
      this.gamepadId = gamepadManager.current().getId();
    } else {
      this.gamepadId = -1; // or any other default value you'd like
    }

    this.rotateWithRightStick = rotateWithRightStick;

    if (gamepadManager != null) {
      gamepadManager.onAdded(
          pad -> {
            if (this.gamepadId == -1) {
              this.gamepadId = pad.getId();
            }
          });

      gamepadManager.onRemoved(
          pad -> {
            if (this.gamepadId == pad.getId()) {
              this.gamepadId = -1;
              Gamepad newGamePad = gamepadManager.current();
              if (newGamePad != null) {
                this.gamepadId = newGamePad.getId();
              }
            }
          });
    }
  }

  @Override
  public void update() {

    this.retrieveGamepadValues();
    super.update();
  }

  public double getGamepadDeadzone() {
    return this.gamepadDeadzone;
  }

  public double getGamepadRightStick() {
    return gamepadRightStick;
  }

  public boolean isRotateWithRightStick() {
    return this.rotateWithRightStick;
  }

  public void setRightStickDeadzone(double gamePadRightStick) {
    this.gamepadRightStick = gamePadRightStick;
  }

  public void setLeftStickDeadzone(double gamePadDeadzone) {
    this.gamepadDeadzone = gamePadDeadzone;
  }

  public void setRotateWithRightStick(boolean rotateWithRightStick) {
    this.rotateWithRightStick = rotateWithRightStick;
  }

  private void retrieveGamepadValues() {
    if (this.gamepadId == -1
        || this.gamepadId != -1 && Input.gamepads().getById(this.gamepadId) == null) {
      return;
    }

    GamepadManager gamepadManager = Input.gamepads();
    if (gamepadManager == null) {
      return;
    }

    final float x = gamepadManager.get(this.gamepadId).getPollData(Gamepad.Axis.X);
    final float y = gamepadManager.get(this.gamepadId).getPollData(Gamepad.Axis.Y);

    if (Math.abs(x) > this.gamepadDeadzone) {
      this.setDx(x);
    }

    if (Math.abs(y) > this.gamepadDeadzone) {
      this.setDy(y);
    }

    if (this.isRotateWithRightStick()) {
      final float rightX = gamepadManager.get(this.gamepadId).getPollData(Gamepad.Axis.RX);
      final float rightY = gamepadManager.get(this.gamepadId).getPollData(Gamepad.Axis.RY);
      float targetX = 0;
      float targetY = 0;
      if (Math.abs(rightX) > this.gamepadRightStick) {
        targetX = rightX;
      }
      if (Math.abs(rightY) > this.gamepadRightStick) {
        targetY = rightY;
      }

      if (targetX != 0 || targetY != 0) {
        final Point2D target =
            new Point2D.Double(
                this.getEntity().getCenter().getX() + targetX,
                this.getEntity().getCenter().getY() + targetY);
        final double angle =
            GeometricUtilities.calcRotationAngleInDegrees(this.getEntity().getCenter(), target);
        this.getEntity().setAngle((float) angle);
      }
    }
  }
}
