package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.ITaskPerformer;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.actions.IActionGenerator;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceType;

/**
 * The lumberjack chops down trees to collect wood and store it in stockpiles.
 */
class LumberjackRole extends AbstractRole {

    private final IResourceFinder finder;
    private final IWorld world;

    /**
     * Instantiates with a way of finding resources.
     *
     * @param finder The locator of resources.
     * @param world  The world in which the resources are located.
     */
    LumberjackRole(final IResourceFinder finder, final IWorld world) {
        this.finder = finder;
        this.world = world;
    }

    @Override
    public RoleType getType() {
        return RoleType.LUMBERJACK;
    }

    @Override
    protected Collection<IActionGenerator> getTaskGenerators() {
        return List.of(this::createMoveToTree, this::createHarvestTree);
    }

    private IAction createMoveToTree(final ITaskPerformer performer) {
        final Optional<IResource> maybeTree = findNearbyTree(performer);
        if (maybeTree.isEmpty()) return ActionFactory.createDoNothing();

        final IResource tree = maybeTree.get();

        final Position position = performer.getPosition();
        final Optional<Position> closestSpotNextToTree = world.getClosestNeighbourOf(tree,
                                                                                     position);

        if (closestSpotNextToTree.isEmpty()) return ActionFactory.createDoNothing();

        return ActionFactory.createMoveTo(closestSpotNextToTree.get());
    }

    private IAction createHarvestTree(final ITaskPerformer performer) {
        final Optional<IResource> maybeTree = findNearbyTree(performer);
        if (maybeTree.isEmpty()) return ActionFactory.createDoNothing();

        final IResource tree = maybeTree.get();

        return ActionFactory.createHarvest(tree);
    }

    private Optional<IResource> findNearbyTree(final ITaskPerformer performer) {
        final Position position = performer.getPosition();
        return finder.getNearbyOfType(position, ResourceType.TREE);
    }

}
