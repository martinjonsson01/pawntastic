package com.thebois.models.beings.roles;

import java.util.Collection;

import com.thebois.models.beings.actions.IActionGenerator;

/**
 * The guard attacks enemies.
 */
class GuardRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.GUARD;
    }

    @Override
    protected Collection<IActionGenerator> getTaskGenerators() {
        return RoleFactory.idle().getTaskGenerators();
    }

}
