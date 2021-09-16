package com.thebois.models.beings.roles;

import com.thebois.models.beings.IBeing;

/**
 * Keeps track of and allows for allocating of roles to beings.
 */
public class RoleAllocator {

    /**
     * Assigns the allocated roles to the provided beings.
     *
     * @param beings The beings to assign roles to
     */
    public void allocate(final Iterable<IBeing> beings) {
        for (IBeing being : beings) {
            being.setRole(RoleFactory.lumberjack());
        }
    }

}
