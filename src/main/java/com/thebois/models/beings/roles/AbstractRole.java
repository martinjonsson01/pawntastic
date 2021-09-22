package com.thebois.models.beings.roles;

import com.thebois.models.IDeepClonable;

/**
 * Represents an assignment to a specific work task.
 */
public abstract class AbstractRole implements IDeepClonable<AbstractRole> {

    @Override
    public int hashCode() {
        return super.hashCode();
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
            return this.getName().equals(other.getName()) && this.getType().equals(other.getType());
        }
        return false;
    }

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

    /**
     * Gets the occupation type.
     *
     * @return The occupation type.
     */
    public abstract RoleType getType();

    @Override
    public AbstractRole deepClone() {
        return RoleFactory.fromType(getType());
    }

}
