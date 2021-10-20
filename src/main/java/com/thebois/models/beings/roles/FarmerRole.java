package com.thebois.models.beings.roles;

/**
 * The farmer harvests crops.
 *
 * @author Martin
 */
class FarmerRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.FARMER;
    }

}
