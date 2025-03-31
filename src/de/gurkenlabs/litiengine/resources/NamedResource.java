package de.gurkenlabs.litiengine.resources;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

public abstract class NamedResource implements Resource {
  @Nullable @XmlAttribute private String name;

  @Nullable
  @XmlTransient
  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(@Nullable final String n) {
    this.name = n;
  }
}
