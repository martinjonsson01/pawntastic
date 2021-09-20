package com.thebois.models.beings.roles;

/**
 * Keeps track of and allows for allocating of roles to beings.
 */
public interface IRoleAllocator {

    /**
     * Requests the amount of beings in a role to be increased.
     *
     * @param roleType The role to add more of
     *
     * @return Whether the allocation could be increased
     */
    boolean tryIncreaseAllocation(RoleType roleType);

    /**
     * Requests the amount of beings assigned to a role to be decreased.
     *
     * @param roleType The role to add more of
     *
     * @return Whether the allocation could be decreased
     */
    boolean tryDecreaseAllocation(RoleType roleType);

    /**
     * Counts the amount of beings with a role.
     *
     * @param roleType The role to count
     *
     * @return The amount of beings with the role
     */
    int countBeingsWithRole(RoleType roleType);

    /**
     * Checks if the allocation of a role can be decreased.
     *
     * @param roleType The role to check
     *
     * @return Whether the allocation for the role can be decreased
     */
    boolean canDecreaseAllocation(RoleType roleType);

    /**
     * Checks if the allocation of a role can be increased.
     *
     * @param roleType The role to check
     *
     * @return Whether the allocation for the role can be increased
     */
    boolean canIncreaseAllocation(RoleType roleType);

}
