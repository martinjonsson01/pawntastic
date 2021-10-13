package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IActionGenerator;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceType;

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
        return List.of(createMoveToTree(),
                       performer -> ActionFactory.createMoveTo(new Position(2, 2)),
                       performer -> ActionFactory.createMoveTo(new Position(2, 2)));
    }

    private IActionGenerator createMoveToTree() {
        return performer -> {
            final Optional<IResource> maybeTree = finder.getNearbyOfType(performer.getPosition(),
                                                                         ResourceType.TREE);
            if (maybeTree.isEmpty()) return ActionFactory.createDoNothing();

            return ActionFactory.createMoveTo(maybeTree.get().getPosition());
        };
    }

}
