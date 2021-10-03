package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;

import com.thebois.models.beings.actions.IActionGenerator;

/**
 * The builder constructs buildings.
 */
class BuilderRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.BUILDER;
    }

    @Override
    protected Collection<IActionGenerator> getTaskGenerators() {
        return List.of();
    }

}
