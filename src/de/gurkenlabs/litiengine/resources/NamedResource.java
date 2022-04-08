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
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String n) {
        this.name = n;
    }
}
