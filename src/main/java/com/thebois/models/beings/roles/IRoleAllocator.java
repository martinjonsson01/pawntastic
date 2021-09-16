package com.thebois.models.beings.roles;

/**
 * Keeps track of and allows for allocating of roles to beings.
 */
public interface IRoleAllocator {

    /**
     * Requests the amount of beings in a role to be increased.
     *
     * @param role The role to add more of
     *
     * @return Whether the allocation could be increased
     */
    boolean tryIncreaseAllocation(Role role);

    /**
     * Requests the amount of beings assigned to a role to be decreased.
     *
     * @param role The role to add more of
     *
     * @return Whether the allocation could be decreased
     */
    boolean tryDecreaseAllocation(Role role);

    /**
     * Counts the amount of beings with a role.
     *
     * @param role The role to count
     *
     * @return The amount of beings with the role
     */
    int countBeingsWithRole(Role role);

    /**
     * Checks if the allocation of a role can be decreased.
     *
     * @param role The role to check
     *
     * @return Whether the allocation for the role can be decreased
     */
    boolean canDecreaseAllocation(Role role);

    /**
     * Checks if the allocation of a role can be increased.
     *
     * @param role The role to check
     *
     * @return Whether the allocation for the role can be increased
     */
    boolean canIncreaseAllocation(Role role);

}
