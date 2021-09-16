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
    public boolean tryIncreaseAllocation(final Role role) {
        final Stream<IBeing> idleBeings = beings.stream().filter(this::isIdle);
        final Optional<IBeing> idleBeing = idleBeings.findAny();
        if (idleBeing.isPresent()) {
            idleBeing.get().setRole(role);
            return true;
        }

        return false;
    }

    @Override
    public boolean tryDecreaseAllocation(final Role role) {
        final Optional<IBeing>
            beingWithRole =
            beings.stream().filter(being -> role.equals(being.getRole())).findFirst();
        if (beingWithRole.isPresent()) {
            beingWithRole.get().setRole(null);
            return true;
        }
        return false;
    }

    @Override
    public int countBeingsWithRole(final Role role) {
        return (int) beings.stream().filter(being -> role.equals(being.getRole())).count();
    }

    private boolean isIdle(final IBeing being) {
        return being.getRole() == null;
    }

}
