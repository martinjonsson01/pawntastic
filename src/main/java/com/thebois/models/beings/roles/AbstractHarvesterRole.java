package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.actions.IActionSource;
import com.thebois.models.inventory.IStoreable;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceType;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.structures.StructureType;

/**
 * Represents a role that mainly harvests items from resources.
 */
abstract class AbstractHarvesterRole extends AbstractRole {

    private final IResourceFinder finder;
    private final IStructureFinder structureFinder;
    private final IWorld world;
    private final ResourceType resourceType;

    /**
     * Instantiates with a way of finding resources.
     *
     * @param finder          The locator of resources.
     * @param structureFinder The locator of structures.
     * @param world           The world in which the resources are located.
     * @param resourceType    The type of resource to gather.
     */
    AbstractHarvesterRole(
        final IResourceFinder finder,
        final IStructureFinder structureFinder,
        final IWorld world,
        final ResourceType resourceType) {
        this.finder = finder;
        this.structureFinder = structureFinder;
        this.world = world;
        this.resourceType = resourceType;
    }

    @Override
    protected Collection<IActionSource> getTaskGenerators() {
        return List.of(this::createMoveToResource,
                       this::createHarvestResource,
                       this::createMoveToStockpile,
                       this::createEmptyInventoryOfResource);
    }

    private IAction createMoveToResource(final IActionPerformer performer) {
        final Optional<IResource> maybeResource = findNearbyResource(performer);
        if (maybeResource.isEmpty()) return ActionFactory.createDoNothing();

        final IResource resource = maybeResource.get();

        final Position position = performer.getPosition();
        final Optional<Position> closestSpotNextToResource = world.getClosestNeighbourOf(resource,
                                                                                         position);

        if (closestSpotNextToResource.isEmpty()) return ActionFactory.createDoNothing();

        return ActionFactory.createMoveTo(closestSpotNextToResource.get());
    }

    private IAction createHarvestResource(final IActionPerformer performer) {
        final Optional<IResource> maybeResource = findNearbyResource(performer);
        if (maybeResource.isEmpty()) return ActionFactory.createDoNothing();

        final IResource resource = maybeResource.get();

        return ActionFactory.createHarvest(resource);
    }

    private IAction createMoveToStockpile(final IActionPerformer performer) {
        final Position position = performer.getPosition();
        final Optional<IStructure> maybeStructure = structureFinder.getNearbyStructureOfType(
            position,
            StructureType.STOCKPILE);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();

        final IStructure structure = maybeStructure.get();

        final Optional<Position> closestSpotNextToStructure = world.getClosestNeighbourOf(structure,
                                                                                          position);

        if (closestSpotNextToStructure.isEmpty()) return ActionFactory.createDoNothing();

        return ActionFactory.createMoveTo(closestSpotNextToStructure.get());
    }

    private IAction createEmptyInventoryOfResource(final IActionPerformer performer) {
        final Position position = performer.getPosition();
        final Optional<IStructure> maybeStructure = structureFinder.getNearbyStructureOfType(
            position,
            StructureType.STOCKPILE);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();
        final IStructure structure = maybeStructure.get();
        final IStoreable storeable;
        if (structure instanceof IStoreable) {
            storeable = (IStoreable) structure;
        }
        else {
            return ActionFactory.createDoNothing();
        }

        return ActionFactory.createGiveItem(storeable,
                                            resourceType.getItemType(),
                                            structure.getPosition());
    }

    private Optional<IResource> findNearbyResource(final IActionPerformer performer) {
        final Position position = performer.getPosition();
        return finder.getNearbyOfType(position, resourceType);
    }

}
