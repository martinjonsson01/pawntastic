package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.abstractions.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.actions.IActionSource;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceType;

/**
 * Represents a role that mainly harvests items from resources.
 *
 * @author Martin
 * @author Mathias
 */
abstract class AbstractHarvesterRole extends AbstractRole {

    private final IResourceFinder resourceFinder;
    private final IStructureFinder structureFinder;
    private final IWorld world;
    private final ResourceType resourceType;
    private boolean isEmptying = false;

    /**
     * Instantiates with a way of finding resources and structures.
     *
     * @param resourceFinder  The locator of resources.
     * @param structureFinder The locator of structures.
     * @param world           The world in which the resources are located.
     * @param resourceType    The type of resource to gather.
     */
    AbstractHarvesterRole(
        final IResourceFinder resourceFinder,
        final IStructureFinder structureFinder,
        final IWorld world,
        final ResourceType resourceType) {
        this.resourceFinder = resourceFinder;
        this.structureFinder = structureFinder;
        this.world = world;
        this.resourceType = resourceType;
    }

    @Override
    protected Collection<IActionSource> getTaskGenerators() {
        if (isEmptying) {
            return List.of(this::createMoveToStockpile, this::createEmptyInventoryOfResource);
        }
        else {
            return List.of(this::createMoveToResource, this::createHarvestResource);
        }
    }

    private IAction createMoveToResource(final IActionPerformer performer) {
        final Optional<IResource> maybeResource = findNearbyResource(performer);
        if (maybeResource.isEmpty()) return ActionFactory.createDoNothing();

        final IResource resource = maybeResource.get();

        final Optional<Position> closestSpotNextToResource =
            world.getClosestNeighbourOf(resource, performer.getPosition());

        if (closestSpotNextToResource.isEmpty()) return ActionFactory.createDoNothing();

        return ActionFactory.createMoveTo(closestSpotNextToResource.get());
    }

    private IAction createHarvestResource(final IActionPerformer performer) {
        final Optional<IResource> maybeResource = findNearbyResource(performer);
        if (maybeResource.isEmpty()) return ActionFactory.createDoNothing();

        final IResource resource = maybeResource.get();
        if (!performer.canFitItem(resourceType.getItemType())) {
            isEmptying = true;
            return ActionFactory.createDoNext();
        }
        return ActionFactory.createHarvest(resource);
    }

    private IAction createMoveToStockpile(final IActionPerformer performer) {
        return ActionFactory.createMoveToStockpile(performer, structureFinder, world);
    }

    private IAction createEmptyInventoryOfResource(final IActionPerformer performer) {
        if (performer.isEmpty()) {
            isEmptying = false;
            return ActionFactory.createDoNext();
        }
        return ActionFactory.createEmptyInventoryOfItemType(
            performer,
            structureFinder,
            resourceType.getItemType());
    }

    private Optional<IResource> findNearbyResource(final IActionPerformer performer) {
        final Position position = performer.getPosition();
        return resourceFinder.getNearbyOfType(position, resourceType);
    }

}
