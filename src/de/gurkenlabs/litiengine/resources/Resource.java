package de.gurkenlabs.litiengine.resources;

import javax.annotation.Nullable;
import de.gurkenlabs.litiengine.util.AlphanumComparator;

public interface Resource extends Comparable<Resource> {

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Nullable
    String getName();

    void setName(@Nullable String name);

    @Override
    default int compareTo(Resource obj) {
        return AlphanumComparator.compareTo(this.getName(), obj.getName());
    }
}
