package com.thebois.models.beings.roles;

import java.util.Objects;

import com.thebois.models.IDeepClonable;
import com.thebois.models.beings.tasks.ITask;

/**
 * Represents an assignment to a specific work task.
 */
public abstract class AbstractRole implements IDeepClonable<AbstractRole> {

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass().equals(obj.getClass())) {
            final AbstractRole other = (AbstractRole) obj;
            return this.getType().equals(other.getType());
        }
        return false;
    }

    /**
     * Gets the occupation type.
     *
     * @return The occupation type.
     */
    public abstract RoleType getType();

    /**
     * Gets the title of the role occupation.
     *
     * @return The title of the role occupation.
     */
    public String getName() {
        final String className = this.getClass().getSimpleName();
        final int roleSuffixLength = 4;
        return className.substring(0, className.length() - roleSuffixLength);
    }

    @Override
    public AbstractRole deepClone() {
        return RoleFactory.fromType(getType());
    }

    /**
     * Gets the task to perform at the moment.
     */
    public ITask getCurrentTask() {
        return null;
    }

}
