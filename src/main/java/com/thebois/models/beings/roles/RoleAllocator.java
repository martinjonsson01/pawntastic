package com.thebois.models.beings.roles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.thebois.models.beings.IBeing;

/**
 * Temporary IRoleAllocator-implementation until this all can be moved to BeingGroup.
 */
public class RoleAllocator implements IRoleAllocator {

    private final List<IBeing> beings;

    /**
     * Initializes with already created beings.
     *
     * @param beings Already initialized beings
     */
    public RoleAllocator(final List<IBeing> beings) {

        this.beings = beings;
    }

    @Override
    public boolean tryIncreaseAllocation(final AbstractRole role) {
        final Optional<IBeing> idleBeing = findIdleBeing();
        if (idleBeing.isPresent()) {
            idleBeing.get().setRole(role);
            return true;
        }

        return false;
    }

    private Optional<IBeing> findIdleBeing() {
        final Stream<IBeing> idleBeings = beings.stream().filter(this::isIdle);
        return idleBeings.findAny();
    }

    private boolean isIdle(final IBeing being) {
        return being.getRole() == null;
    }

    @Override
    public boolean tryDecreaseAllocation(final AbstractRole role) {
        final Optional<IBeing> beingWithRole = findBeingWithRole(role);
        if (beingWithRole.isPresent()) {
            beingWithRole.get().setRole(null);
            return true;
        }
        return false;
    }

    private Optional<IBeing> findBeingWithRole(final AbstractRole role) {
        return beings.stream().filter(being -> role.equals(being.getRole())).findFirst();
    }

    @Override
    public int countBeingsWithRole(final AbstractRole role) {
        return (int) beings.stream().filter(being -> role.equals(being.getRole())).count();
    }

    @Override
    public boolean canDecreaseAllocation(final AbstractRole role) {
        return findBeingWithRole(role).isPresent();
    }

    @Override
    public boolean canIncreaseAllocation(final AbstractRole role) {
        return findIdleBeing().isPresent();
    }

}
