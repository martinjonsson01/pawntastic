package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;

import com.thebois.models.beings.tasks.ITaskGenerator;

/**
 * The builder constructs buildings.
 */
class BuilderRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.BUILDER;
    }

    @Override
    protected Collection<ITaskGenerator> getTaskGenerators() {
        return List.of();
    }

}
