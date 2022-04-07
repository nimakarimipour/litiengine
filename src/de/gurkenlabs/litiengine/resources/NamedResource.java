package de.gurkenlabs.litiengine.resources;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

public abstract class NamedResource implements Resource {

    @XmlAttribute
    @Nullable
    private String name;

    @XmlTransient
    @Override
    @Nullable
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(@Nullable final String n) {
        this.name = n;
    }
}
