package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;

import com.thebois.models.beings.tasks.IActionable;

/**
 * The farmer harvests crops.
 */
class FarmerRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.FARMER;
    }

    @Override
    protected Collection<IActionable> getTasks() {
        return List.of();
    }

}
