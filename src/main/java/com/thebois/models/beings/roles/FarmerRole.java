package com.thebois.models.beings.roles;

import java.util.Collection;

import com.thebois.models.beings.actions.IActionSource;

/**
 * The farmer harvests crops.
 */
class FarmerRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.FARMER;
    }

    @Override
    protected Collection<IActionSource> getTaskGenerators() {
        return RoleFactory.idle().getTaskGenerators();
    }

}
