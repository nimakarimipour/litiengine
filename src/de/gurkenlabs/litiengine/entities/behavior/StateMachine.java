package de.gurkenlabs.litiengine.entities.behavior;

import java.util.Collections;
import java.util.List;

import de.gurkenlabs.litiengine.IUpdateable;
import javax.annotation.Nullable;
import de.gurkenlabs.litiengine.NullUnmarked;

public class StateMachine implements IUpdateable {
  @Nullable private State currentState;

  protected StateMachine() {
  }

  @Nullable public State getCurrentState() {
    return this.currentState;
  }

  public void setState(final State newState) {
    if (this.currentState != null) {
      this.currentState.exit();
    }

    this.currentState = newState;
    this.currentState.enter();
  }

  @NullUnmarked @Override
  public void update() {
    if (this.currentState == null) {
      return;
    }

    this.currentState.perform();
    final List<Transition> transitions = this.currentState.getTransitions();
    Collections.sort(transitions);

    for (final Transition transition : transitions) {
      if (transition.conditionsFullfilled()) {
        this.currentState.exit();
        this.currentState = transition.getNextState();
        this.currentState.enter();
        return;
      }
    }
  }
}
