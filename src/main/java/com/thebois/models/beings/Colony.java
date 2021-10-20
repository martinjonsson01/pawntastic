package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.thebois.models.Position;
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

    /**
     * Creates a colony and fills it with pawns in the provided open positions.
     *
     * @param vacantPositions Positions in the world where pawns can be created.
     */
    public Colony(final Iterable<Position> vacantPositions) {
        final Collection<IBeing> pawns = new ArrayList<>();
        for (final Position vacantPosition : vacantPositions) {
            final AbstractRole role = RoleFactory.idle();
            pawns.add(new Pawn(vacantPosition, this, role));
        }
        setBeings(pawns);
    }

    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public boolean tryIncreaseAllocation(final RoleType roleType) {
        final Optional<IBeing> idleBeing = findIdleBeings().stream()
                                                           .findAny();
        if (idleBeing.isPresent()) {
            final AbstractRole role = RoleFactory.fromType(roleType);
            idleBeing.get()
                     .setRole(role);
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
        final Optional<IBeing> beingWithRole = findBeingsWithRole(role).stream()
                                                                       .findFirst();
        if (beingWithRole.isPresent()) {
            beingWithRole.get()
                         .setRole(RoleFactory.idle());
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
        return (int) getBeings().stream()
                                .filter(being -> role.equals(being.getRole()))
                                .count();
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
        final Stream<IBeing> idleBeings = getBeings().stream()
                                                     .filter(this::isIdle);
        return idleBeings.collect(Collectors.toList());
    }

    private boolean isIdle(final IBeing being) {
        return being.getRole()
                    .getType()
                    .equals(RoleType.IDLE);
    }

    private Collection<IBeing> findBeingsWithRole(final AbstractRole role) {
        return getBeings().stream()
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

}
