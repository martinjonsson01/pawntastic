package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.eventbus.Subscribe;

import com.thebois.Pawntastic;
import com.thebois.listeners.events.SpawnPawnsEvent;
import com.thebois.models.IPositionFinder;
import com.thebois.models.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.IRoleAllocator;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.beings.roles.RoleType;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.Inventory;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * A Colony is a collection of Pawns that can be controlled by the player.
 */
public class Colony extends AbstractBeingGroup implements IRoleAllocator, IInventory {

    private final IInventory inventory = new Inventory();
    private final IPathFinder pathFinder;
    private final IStructureFinder structureFinder;
    private final IPositionFinder positionFinder;

    /**
     * Creates a colony and fills it with pawns in the provided open positions.
     *
     * @param vacantPositions Positions in the world where pawns can be created.
     * @param pathFinder      The pathfinder that the pawns will use.
     * @param structureFinder          Used to find structures in the world.
     * @param positionFinder          Used to find positions in the world.
     */
    public Colony(
        final Iterable<Position> vacantPositions,
        final IPathFinder pathFinder,
        final IStructureFinder structureFinder, final IPositionFinder positionFinder) {
        this.pathFinder = pathFinder;
        this.structureFinder = structureFinder;
        this.positionFinder = positionFinder;
        final Collection<IBeing> pawns = new ConcurrentLinkedQueue<>();
        final Random random = new Random();
        for (final Position vacantPosition : vacantPositions) {
            super.addBeing(new Pawn(vacantPosition, vacantPosition, random, pathFinder, structureFinder));
        }
        //        setBeings(pawns);
        Pawntastic.getEventBus().register(this);
    }

    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public boolean tryIncreaseAllocation(final RoleType roleType) {
        final Optional<IBeing> idleBeing = findIdleBeings().stream().findAny();
        if (idleBeing.isPresent()) {
            final AbstractRole role = RoleFactory.fromType(roleType);
            idleBeing.get().setRole(role);
            return true;
        }

        return false;
    }

    @Override
    public boolean tryIncreaseAllocation(final RoleType roleType, final int amount) {
        if (!canIncreaseAllocation(amount)) return false;
        for (int i = 0; i < amount; i++) {
            tryIncreaseAllocation(roleType);
        }
        return true;
    }

    @Override
    public boolean tryDecreaseAllocation(final RoleType roleType) {
        final AbstractRole role = RoleFactory.fromType(roleType);
        final Optional<IBeing> beingWithRole = findBeingsWithRole(role).stream().findFirst();
        if (beingWithRole.isPresent()) {
            beingWithRole.get().setRole(RoleFactory.idle());
            return true;
        }
        return false;
    }

    @Override
    public boolean tryDecreaseAllocation(final RoleType roleType, final int amount) {
        if (!canDecreaseAllocation(roleType, amount)) return false;
        for (int i = 0; i < amount; i++) {
            tryDecreaseAllocation(roleType);
        }
        return true;
    }

    @Override
    public int countBeingsWithRole(final RoleType roleType) {
        final AbstractRole role = RoleFactory.fromType(roleType);
        return (int) getBeings().stream().filter(being -> role.equals(being.getRole())).count();
    }

    @Override
    public boolean canDecreaseAllocation(final RoleType roleType) {
        return canDecreaseAllocation(roleType, 1);
    }

    @Override
    public boolean canDecreaseAllocation(final RoleType roleType, final int amount) {
        final AbstractRole role = RoleFactory.fromType(roleType);
        return findBeingsWithRole(role).size() >= amount;
    }

    @Override
    public boolean canIncreaseAllocation() {
        return canIncreaseAllocation(1);
    }

    @Override
    public boolean canIncreaseAllocation(final int amount) {
        return findIdleBeings().size() >= amount;
    }

    private Collection<IBeing> findIdleBeings() {
        final Stream<IBeing> idleBeings = getBeings().stream().filter(this::isIdle);
        return idleBeings.collect(Collectors.toList());
    }

    private boolean isIdle(final IBeing being) {
        return being.getRole().getType().equals(RoleType.IDLE);
    }

    private Collection<IBeing> findBeingsWithRole(final AbstractRole role) {
        return getBeings()
            .stream()
            .filter(being -> role.equals(being.getRole()))
            .collect(Collectors.toList());
    }

    @Override
    public void add(final IItem item) {
        inventory.add(item);
    }

    @Override
    public void addMultiple(final List<IItem> stack) {
        inventory.addMultiple(stack);
    }

    @Override
    public IItem take(final ItemType itemType) {
        return inventory.take(itemType);
    }

    @Override
    public ArrayList<IItem> takeAmount(final ItemType itemType, final int amount) {
        return inventory.takeAmount(itemType, amount);
    }

    @Override
    public boolean hasItem(final ItemType itemType) {
        return inventory.hasItem(itemType);
    }

    @Override
    public boolean hasItem(final ItemType itemType, final int amount) {
        return inventory.hasItem(itemType, amount);
    }

    @Override
    public int numberOf(final ItemType itemType) {
        return inventory.numberOf(itemType);
    }

    /**
     * Listens to the spawnPawnsEvent in order to know when to add pawns to the colony.
     *
     * @param event The published event.
     */
    @Subscribe
    public void onSpawnPawnsEvent(final SpawnPawnsEvent event) {
        final Iterable<Position> vacantPositions = positionFinder.findEmptyPositions(10);
        final Random random = new Random();
        for (final Position vacantPosition : vacantPositions) {
            addBeing(new Pawn(vacantPosition, vacantPosition, random, pathFinder, structureFinder));
        }
    }

}
