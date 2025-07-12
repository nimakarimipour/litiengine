package de.gurkenlabs.litiengine.entities;

import edu.ucr.cs.riple.annotator.util.Nullability;
import java.util.Comparator;

/**
 * This {@code Comparator} implementation sorts entities by the max y-coordinate of their collision
 * box (if its a {@code ICollisionEntity}) or of their bounding box.
 *
 * @see ICollisionEntity#getCollisionBox()
 * @see IEntity#getBoundingBox()
 * @see Double#compareTo(Double)
 */
public class EntityYComparator implements Comparator<IEntity> {

  @Override
  public int compare(final IEntity m1, final IEntity m2) {
    ICollisionEntity coll1 = null;
    ICollisionEntity coll2 = null;
    Rectangle2D coll1Box = null;
    Rectangle2D coll2Box = null;

    if (m1 instanceof ICollisionEntity) {
      coll1 = (ICollisionEntity) m1;
      coll1Box = coll1.getCollisionBox();
    }

    if (m2 instanceof ICollisionEntity) {
      coll2 = (ICollisionEntity) m2;
      coll2Box = coll2.getCollisionBox();
    }

    final double m1MaxY =
        coll1Box != null
            ? Nullability.castToNonnull(coll1.getCollisionBox(), "instance check done").getMaxY()
            : m1.getBoundingBox().getMaxY();
    final double m2MaxY = coll2Box != null ? coll2Box.getMaxY() : m2.getBoundingBox().getMaxY();

    return Double.valueOf(m1MaxY).compareTo(m2MaxY);
  }
}
