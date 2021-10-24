package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.thebois.abstractions.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.actions.IActionSource;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.structures.IStructure;

/**
 * The builder constructs buildings.
 *
 * @author Martin
 */
class BuilderRole extends AbstractRole {

    private final IStructureFinder finder;
    private final IWorld world;

    /**
     * Instantiates with a way of finding structures to build.
     *
     * @param finder The source of structures to construct.
     * @param world  The world in which the structures are located.
     */
    BuilderRole(
        final IStructureFinder finder, final IWorld world) {
        this.finder = finder;
        this.world = world;
    }

    @Override
    public RoleType getType() {
        return RoleType.BUILDER;
    }

    @Override
    protected Collection<IActionSource> getTaskGenerators() {
        return List.of(this::createMoveToIncompleteStructure, this::createBuildStructure);
    }

    private IAction createMoveToIncompleteStructure(final IActionPerformer performer) {
        final Optional<IStructure> maybeStructure = findNearbyIncompleteStructure(performer);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();

        final IStructure structure = maybeStructure.get();

        final Position position = performer.getPosition();
        final Optional<Position> closestSpotNextToResource = world.getClosestNeighbourOf(structure,
                                                                                         position);

        if (closestSpotNextToResource.isEmpty()) return ActionFactory.createDoNothing();

        return ActionFactory.createMoveTo(closestSpotNextToResource.get());
    }

    private IAction createBuildStructure(final IActionPerformer performer) {
        final Optional<IStructure> maybeStructure = findNearbyIncompleteStructure(performer);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();

        final IStructure structure = maybeStructure.get();

        return ActionFactory.createBuild(structure);
    }

    private Optional<IStructure> findNearbyIncompleteStructure(final IActionPerformer performer) {
        final Position position = performer.getPosition();
        return finder.getNearbyIncompleteStructure(position);
    }

}
