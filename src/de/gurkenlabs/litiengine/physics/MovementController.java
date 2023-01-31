package de.gurkenlabs.litiengine.physics;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.EntityMovedEvent;
import de.gurkenlabs.litiengine.entities.IMobileEntity;
import de.gurkenlabs.litiengine.util.MathUtilities;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;
import javax.annotation.Nullable;
import de.gurkenlabs.litiengine.NullUnmarked;

public class MovementController<T extends IMobileEntity> implements IMovementController {
  private final List<Force> activeForces;
  private final T mobileEntity;
  private final List<Predicate<IMobileEntity>> movementPredicates;

  private float dx;
  private float dy;
  private double velocity;
  private double moveAngle;

  public MovementController(final T mobileEntity) {
    this.activeForces = new CopyOnWriteArrayList<>();
    this.movementPredicates = new CopyOnWriteArrayList<>();
    this.mobileEntity = mobileEntity;
  }

  @NullUnmarked @Override
  public void attach() {
    Game.loop().attach(this);
  }

  @NullUnmarked @Override
  public void detach() {
    Game.loop().detach(this);
  }

  @Override
  public void apply(final Force force) {
    if (!this.activeForces.contains(force)) {
      this.activeForces.add(force);
    }
  }

  @Override
  public List<Force> getActiveForces() {
    return this.activeForces;
  }

  @Override
  public T getEntity() {
    return this.mobileEntity;
  }

  @Override
  public float getDx() {
    return dx;
  }

  @Override
  public void setDx(float dx) {
    this.dx = dx;
  }

  @Override
  public float getDy() {
    return dy;
  }

  @Override
  public void setDy(float dy) {
    this.dy = dy;
  }

  @Override
  public void onMovementCheck(final Predicate<IMobileEntity> predicate) {
    if (!this.movementPredicates.contains(predicate)) {
      this.movementPredicates.add(predicate);
    }
  }

  @Override
  public void update() {
    this.handleForces();
    this.handleMovement();
  }

  @NullUnmarked public void handleMovement() {
    if (!this.isMovementAllowed()) {
      this.velocity = 0;
      return;
    }

    // max distance an entity can travel within one tick
    final double maxPixelsPerTick = this.getEntity().getTickVelocity();
    final double deltaTime = Game.loop().getDeltaTime() * Game.loop().getTimeScale();

    final double acceleration = this.getEntity().getAcceleration() == 0 ? maxPixelsPerTick : deltaTime / this.getEntity().getAcceleration() * maxPixelsPerTick;
    final double deceleration = this.getEntity().getDeceleration() == 0 ? this.getVelocity() : deltaTime / this.getEntity().getDeceleration() * maxPixelsPerTick;

    double dx = this.getDx();
    double dy = this.getDy();
    this.setDx(0);
    this.setDy(0);

    final double deltaVelocity = Math.min(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)), acceleration);
    if (deltaVelocity != 0) {
      double newVelocity = this.getVelocity() + deltaVelocity;
      this.setVelocity(newVelocity);
    } else {
      final double newVelocity = Math.max(0, this.getVelocity() - deceleration);
      this.setVelocity(newVelocity);
      dx = GeometricUtilities.getDeltaX(this.moveAngle);
      dy = GeometricUtilities.getDeltaY(this.moveAngle);
    }

    if (this.getVelocity() == 0) {
      this.moveAngle = 0;
      return;
    }

    // actually move entity
    this.moveEntity(dx, dy);
  }

  @Nullable @Override
  public Force getForce(String identifier) {
    if (identifier == null || identifier.isEmpty()) {
      return null;
    }

    return this.getActiveForces().stream().filter(x -> x.getIdentifier() != null && x.getIdentifier().equals(identifier)).findFirst().orElse(null);
  }

  @Override
  public double getMoveAngle() {
    return this.moveAngle;
  }
  
  @Override
  public void setVelocity(double velocity) {
    final double maxVelocity = this.getEntity().getTickVelocity();
    this.velocity = MathUtilities.clamp(velocity, -maxVelocity, maxVelocity);
  }

  @Override
  public double getVelocity() {
    return this.velocity;
  }

  protected void moveEntity(double deltaX, double deltaY) {
    this.moveAngle = Math.toDegrees(Math.atan2(deltaX, deltaY));
    Game.physics().move(this.getEntity(), this.moveAngle, this.getVelocity());
  }

  protected boolean isMovementAllowed() {
    for (final Predicate<IMobileEntity> predicate : this.movementPredicates) {
      if (!predicate.test(this.getEntity())) {
        return false;
      }
    }

    return true;
  }

  @NullUnmarked private void handleForces() {
    // clean up forces
    this.activeForces.forEach(x -> {
      if (x.hasEnded()) {
        this.activeForces.remove(x);
      }
    });

    if (this.activeForces.isEmpty()) {
      return;
    }

    // disable turn-on-move for force handling
    boolean turn = this.getEntity().turnOnMove();
    this.getEntity().setTurnOnMove(false);
    try {
      double deltaX = 0;
      double deltaY = 0;
      for (final Force force : this.activeForces) {
        if (force.cancelOnReached() && force.hasReached(this.getEntity())) {
          force.end();
          continue;
        }

        final Point2D collisionBoxCenter = this.getEntity().getCollisionBoxCenter();
        final double angle = GeometricUtilities.calcRotationAngleInDegrees(collisionBoxCenter, force.getLocation());
        final double strength = Game.loop().getDeltaTime() * 0.001f * force.getStrength() * Game.loop().getTimeScale();
        deltaX += GeometricUtilities.getDeltaX(angle, strength);
        deltaY += GeometricUtilities.getDeltaY(angle, strength);
      }

      final Point2D target = new Point2D.Double(this.getEntity().getX() + deltaX, this.getEntity().getY() + deltaY);
      final boolean success = Game.physics().move(this.getEntity(), target);
      if (!success) {
        for (final Force force : this.activeForces) {
          if (force.cancelOnCollision()) {
            force.end();
          }
        }
      }
    } finally {
      this.getEntity().setTurnOnMove(turn);
    }
  }
}
