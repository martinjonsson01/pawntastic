package com.thebois.models.beings.roles;

/**
 * Represents an assignment to a specific work task.
 */
public class Role {

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        else if (obj == null) {
            return false;
        }
        else {
            return this.getClass().equals(obj.getClass());
        }
    }

}
