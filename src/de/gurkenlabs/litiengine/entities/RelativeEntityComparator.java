package de.gurkenlabs.litiengine.entities;

import javax.annotation.Nullable;
import de.gurkenlabs.litiengine.Initializer;
import java.util.Comparator;

public abstract class RelativeEntityComparator implements Comparator<IEntity> {

    @Nullable
    private IEntity relativeEntity;

    protected RelativeEntityComparator() {
    }

    @Initializer
    protected RelativeEntityComparator(final IEntity relativeEntity) {
        this.relativeEntity = relativeEntity;
    }

    @Nullable
    public IEntity getRelativeEntity() {
        return this.relativeEntity;
    }

    public void setRelativeEntity(final IEntity relativeEntity) {
        this.relativeEntity = relativeEntity;
    }
}
