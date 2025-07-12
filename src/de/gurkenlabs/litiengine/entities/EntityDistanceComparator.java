package de.gurkenlabs.litiengine.entities;

import edu.ucr.cs.riple.annotator.util.Nullability;

public class EntityDistanceComparator extends RelativeEntityComparator {

  /**
   * Initializes a new instance of the {@code EntityDistanceComparator} class.
   *
   * @param relativeEntity The entity that is used as reference for distance comparison.
   */
  public EntityDistanceComparator(final IEntity relativeEntity) {
    super(relativeEntity);
  }

  @Override
  public int compare(final IEntity entity1, final IEntity entity2) {
    if (this.getRelativeEntity() == null
        || entity1.getLocation() == null
        || entity2.getLocation() == null) {
      return 0;
    }

    final double distance1 = entity1.getLocation().distance(this.getRelativeEntity().getLocation());
    final double distance2 =
        Nullability.castToNonnull(entity2.getLocation(), "checked for null")
            .distance(this.getRelativeEntity().getLocation());
    if (distance1 < distance2) {
      return -1;
    }
    if (distance1 > distance2) {
      return 1;
    }

    return 0;
  }
}
