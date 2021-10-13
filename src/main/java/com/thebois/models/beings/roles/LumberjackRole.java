package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IActionGenerator;

/**
 * The lumberjack chops down trees to collect wood and store it in stockpiles.
 */
class LumberjackRole extends AbstractRole {

    private final IResourceFinder finder;

    /**
     * Instantiates with a way of finding resources.
     *
     * @param finder The locator of resources.
     */
    LumberjackRole(final IResourceFinder finder) {
        this.finder = finder;
    }

    @Override
    public RoleType getType() {
        return RoleType.LUMBERJACK;
    }

    @Override
    protected Collection<IActionGenerator> getTaskGenerators() {
        return List.of(
            () -> ActionFactory.createMoveTo(new Position(2, 2)),
            () -> ActionFactory.createMoveTo(new Position(2, 2)),
            () -> ActionFactory.createMoveTo(new Position(2, 2)));
    }

}
