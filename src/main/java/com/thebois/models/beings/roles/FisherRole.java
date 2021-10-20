package com.thebois.models.beings.roles;

/**
 * The fisher catches fish out of water.
 *
 * @author Martin
 */
class FisherRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.FISHER;
    }

}
