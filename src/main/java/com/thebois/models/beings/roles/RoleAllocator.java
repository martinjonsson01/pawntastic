package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import com.thebois.models.beings.IBeing;

/**
 * Temporary IRoleAllocator-implementation until this all can be moved to BeingGroup.
 */
public class RoleAllocator implements IRoleAllocator {

    private final Collection<IBeing> beings;

    /**
     * Initializes with already created beings.
     *
     * @param beings Already initialized beings
     */
    public RoleAllocator(final Collection<IBeing> beings) {

        this.beings = beings;
    }

    @Override
    public boolean tryIncreaseAllocation(final RoleType roleType) {
        final Optional<IBeing> idleBeing = findIdleBeing();
        if (idleBeing.isPresent()) {
            final AbstractRole role = RoleFactory.fromType(roleType);
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
    public boolean tryDecreaseAllocation(final RoleType roleType) {
        final AbstractRole role = RoleFactory.fromType(roleType);
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
    public int countBeingsWithRole(final RoleType roleType) {
        final AbstractRole role = RoleFactory.fromType(roleType);
        return (int) beings.stream().filter(being -> role.equals(being.getRole())).count();
    }

    @Override
    public boolean canDecreaseAllocation(final RoleType roleType) {
        final AbstractRole role = RoleFactory.fromType(roleType);
        return findBeingWithRole(role).isPresent();
    }

    @Override
    public boolean canIncreaseAllocation(final RoleType roleType) {
        return findIdleBeing().isPresent();
    }

}
