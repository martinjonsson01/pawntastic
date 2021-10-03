package com.thebois.models.beings.roles;

import java.util.Collection;

import com.thebois.models.beings.actions.IActionGenerator;

/**
 * The farmer harvests crops.
 */
class FarmerRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.FARMER;
    }

    @Override
    protected Collection<IActionGenerator> getTaskGenerators() {
        return RoleFactory.idle().getTaskGenerators();
    }

}
