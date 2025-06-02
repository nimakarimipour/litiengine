package de.gurkenlabs.litiengine.abilities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AbilityExecution implements IUpdateable {
  private final Ability ability;
  private final List<Effect> appliedEffects;
  private final Point2D castLocation;
  private final long executionTicks;
  private final Shape impactArea;

  AbilityExecution(final Ability ability) {
    this.appliedEffects = new CopyOnWriteArrayList<>();
    this.ability = ability;
    this.executionTicks = Game.time().now();
    this.impactArea = ability.calculateImpactArea();
    this.castLocation = ability.getExecutor().getCenter();
    NullabilityUtil.castToNonnull(Game.loop(), "initialized in Game.init").attach(this);
  }

  public Ability getAbility() {
    return this.ability;
  }

  public List<Effect> getAppliedEffects() {
    return this.appliedEffects;
  }

  public Point2D getCastLocation() {
    return this.castLocation;
  }

  public Shape getExecutionImpactArea() {
    return this.impactArea;
  }

  public long getExecutionTicks() {
    return this.executionTicks;
  }

  /**
   * 1. Apply all ability effects after their delay. 2. Unregister this instance after all effects
   * were applied. 3. Effects will apply their follow up effects on their own.
   */
  @Override
  public void update() {
    if (this.getAbility().getEffects().isEmpty()
        || this.getAbility().getEffects().size() == this.getAppliedEffects().size()) {
      NullabilityUtil.castToNonnull(Game.loop(), "cannot return null").detach(this);
      return;
    }

    for (final Effect effect : this.getAbility().getEffects()) {
      if (this.getAppliedEffects().contains(effect)
          || Game.time().since(this.getExecutionTicks()) < effect.getDelay()) {
        continue;
      }

      effect.apply(this.getExecutionImpactArea());
      this.getAppliedEffects().add(effect);
    }
  }
}
