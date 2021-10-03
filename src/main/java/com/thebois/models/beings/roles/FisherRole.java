package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;

import com.thebois.models.beings.actions.IActionGenerator;

/**
 * The fisher catches fish out of water.
 */
class FisherRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.FISHER;
    }

    @Override
    protected Collection<IActionGenerator> getTaskGenerators() {
        return List.of();
    }

}
