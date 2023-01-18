package de.gurkenlabs.litiengine.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.annotation.Nullable;

public abstract class NamedResource implements Resource {
  @Nullable @XmlAttribute
  private String name;

  @XmlTransient
  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(final String n) {
    this.name = n;
  }
}
