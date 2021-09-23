package com.thebois.models.beings;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.IRoleAllocator;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.beings.roles.RoleType;
import com.thebois.models.inventory.ColonyInventory;
import com.thebois.models.inventory.IItem;
import com.thebois.models.inventory.ItemType;

/**
 * A Colony is a collection of Pawns that can be controlled by the player.
 */
public class Colony extends AbstractBeingGroup implements IRoleAllocator {

    private final ColonyInventory colonyInventory = new ColonyInventory();

    /**
     * Initializes with already existing beings.
     *
     * @param beings The beings that should belong to the colony
     */
    public Colony(final Collection<IBeing> beings) {
        setBeings(beings);
    }

    /**
     * Creates an instance of Colony with n number of pawns.
     *
     * @param vacantPositions Positions in the world that a Pawn can be placed on
     */
    public Colony(final Iterable<Position> vacantPositions) {
        createBeings(vacantPositions);
    }

    private void createBeings(final Iterable<Position> vacantPositions) {
        final Random random = new Random();
        for (final Position vacantPosition : vacantPositions) {
            addBeing(new Pawn(vacantPosition, vacantPosition, random));
        }
    }

    /**
     * Allows items to be stored in the shared inventory for the colony.
     *
     * @param item The item to be stored
     */
    public void addItemToColonyInventory(IItem item) {
        colonyInventory.addItem(item);
    }

    /**
     * Allows item to be retrieved from the shared colony inventory.
     *
     * @param itemType The type of item that is to be retrieved from the inventory.
     *
     * @return An item from the inventory of the requested type if it exists.
     */
    public IItem takeItemFromColonyInventory(ItemType itemType) {
        return colonyInventory.takeItem(itemType);
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
        return getBeings().stream()
                          .filter(being -> role.equals(being.getRole()))
                          .collect(Collectors.toList());
    }

}
