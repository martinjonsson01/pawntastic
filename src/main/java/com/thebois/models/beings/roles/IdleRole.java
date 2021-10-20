package com.thebois.models.beings.roles;

/**
 * The builder constructs buildings.
 *
 * @author Martin
 */
class IdleRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.IDLE;
    }

}
