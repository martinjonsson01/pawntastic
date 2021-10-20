package com.thebois.models.beings.roles;

/**
 * The guard attacks enemies.
 *
 * @author Martin
 */
class GuardRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.GUARD;
    }

}
