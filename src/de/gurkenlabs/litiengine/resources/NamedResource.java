package de.gurkenlabs.litiengine.resources;

import de.gurkenlabs.litiengine.Initializer;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

public abstract class NamedResource implements Resource {

    @XmlAttribute
    private String name;

    @XmlTransient
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    @Initializer
    public void setName(final String n) {
        this.name = n;
    }
}
