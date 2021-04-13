package de.gurkenlabs.litiengine.resources;

import javax.annotation.Nullable;

public interface ResourcesContainerListener<T> extends ResourcesContainerClearedListener {

  /**
   * This method gets called after the {@code ResourcesContainer.add} method was executed.
   * 
   * @param resourceName
   *          The name by which the added resource is identified.
   * @param resource
   *          The added resource.
   * @see ResourcesContainer#add(String, Object)
   */
  default void added(String resourceName, @Nullable T resource) {
  }

  /**
   * This method gets called after the {@code ResourcesContainer.remove} method was executed.
   * 
   * @param resourceName
   *          The name by which the removed resource was identified.
   * @param resource
   *          The removed resource.
   * @see ResourcesContainer#remove(String)
   */
  default void removed(String resourceName, T resource) {
  }

  default void cleared() {
  }
}
