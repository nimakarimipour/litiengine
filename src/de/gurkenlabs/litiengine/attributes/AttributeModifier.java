package de.gurkenlabs.litiengine.attributes;
import javax.annotation.Nullable;

/**
 * An attribute modifier allows to modify attributes by the
 * specified Modification and modify value.
 *
 * @param <T>
 *          the generic type
 *
 * @see Attribute#addModifier(AttributeModifier)
 * @see Attribute#modifyBaseValue(AttributeModifier)
 */
public class AttributeModifier<T extends Number> implements Comparable<AttributeModifier<T>> {
  private final Modification modification;
  private double modifyValue;
  private boolean active;

  /**
   * Initializes a new instance of the {@code AttributeModifier} class.
   *
   * @param mod
   *          The modification type.
   * @param modifyValue
   *          The modification value to be applied by this instance.
   */
  public AttributeModifier(final Modification mod, final double modifyValue) {
    this.modification = mod;
    this.modifyValue = modifyValue;
    this.active = true;
  }

  @Override
  public int compareTo(final AttributeModifier<T> otherModifier) {
    return Integer.compare(this.getModification().getApplyOrder(), otherModifier.getModification().getApplyOrder());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof AttributeModifier<?>) {
      AttributeModifier<?> attr = (AttributeModifier<?>) obj;
      return this.isActive() == attr.isActive() && this.getModification() == attr.getModification() && this.getModifyValue() == attr.getModifyValue();
    }

    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Gets the modification type applied by this modifier.
   * 
   * @return The modification type applied by this modifier.
   */
  public Modification getModification() {
    return this.modification;
  }

  /**
   * Gets the value that is used to modify an attribute.
   * 
   * @return The value that is used to modify an attribute.
   */
  public double getModifyValue() {
    return this.modifyValue;
  }

  public boolean isActive() {
    return active;
  }

  public T modify(final T modvalue) {
    if (modvalue == null || !this.isActive()) {
      return modvalue;
    }

    T result = null;

    switch (this.getModification()) {
      case ADD:
        result = this.ensureType(Double.valueOf(modvalue.doubleValue() + this.getModifyValue()), modvalue);
        break;
      case SUBTRACT:
        result = this.ensureType(Double.valueOf(modvalue.doubleValue() - this.getModifyValue()), modvalue);
        break;
      case MULTIPLY:
        result = this.ensureType(Double.valueOf(modvalue.doubleValue() * this.getModifyValue()), modvalue);
        break;
      case DIVIDE:
        // Prevent dividing by zero
        double divisor = this.getModifyValue();
        if (divisor != 0) {
          result = this.ensureType(Double.valueOf(modvalue.doubleValue() / divisor), modvalue);
        }
        break;
      case ADDPERCENT:
        result = this.ensureType(Double.valueOf(modvalue.doubleValue() + modvalue.doubleValue() / 100 * this.getModifyValue()), modvalue);
        break;
      case SUBTRACTPERCENT:
        result = this.ensureType(Double.valueOf(modvalue.doubleValue() - modvalue.doubleValue() / 100 * this.getModifyValue()), modvalue);
        break;
      case SET:
        result = this.ensureType(Double.valueOf(this.getModifyValue()), modvalue);
        break;
      case UNKNOWN:
      default:
        return modvalue;
    }

    // Return result if not null, otherwise return the original modvalue
    return result != null ? result : modvalue;
  }

  public void setModifyValue(double value) {
    this.modifyValue = value;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @SuppressWarnings("unchecked")
  @Nullable private T ensureType(final Double modValue, final T originalValue) {
    if (originalValue instanceof Double) {
      return (T) modValue;
    } else if (originalValue instanceof Float) {
      return (T) Float.valueOf(modValue.floatValue());
    } else if (originalValue instanceof Long) {
      return (T) Long.valueOf(modValue.longValue());
    } else if (originalValue instanceof Byte) {
      return (T) Byte.valueOf(modValue.byteValue());
    } else if (originalValue instanceof Short) {
      return (T) Short.valueOf(modValue.shortValue());
    } else if (originalValue instanceof Integer) {
      return (T) Integer.valueOf(modValue.intValue());
    }
    return null;
  }
}
