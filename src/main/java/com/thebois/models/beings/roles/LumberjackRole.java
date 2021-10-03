package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;

import com.thebois.models.beings.tasks.ITaskGenerator;

/**
 * The lumberjack chops down trees to collect wood and store it in stockpiles.
 */
class LumberjackRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.LUMBERJACK;
    }

    @Override
    protected Collection<ITaskGenerator> getTaskGenerators() {
        return List.of();
    }

}
