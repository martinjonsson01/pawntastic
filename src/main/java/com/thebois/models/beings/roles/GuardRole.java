package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;

import com.thebois.models.beings.tasks.IActionable;

/**
 * The guard attacks enemies.
 */
class GuardRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.GUARD;
    }

    @Override
    protected Collection<IActionable> getTasks() {
        return List.of();
    }

}
