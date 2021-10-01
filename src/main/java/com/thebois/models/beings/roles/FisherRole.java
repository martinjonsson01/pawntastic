package com.thebois.models.beings.roles;

/**
 * The fisher catches fish out of water.
 */
class FisherRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.FISHER;
    }

}
