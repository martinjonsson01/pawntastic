package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;

import com.thebois.models.beings.tasks.IActionable;

/**
 * The fisher catches fish out of water.
 */
class FisherRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.FISHER;
    }

    @Override
    protected Collection<IActionable> getTasks() {
        return List.of();
    }

}
