package com.thebois.models.beings.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.thebois.models.world.IWorld;

/**
 * Creates requested instances of specific roles.
 */
public final class RoleFactory {

    private static IWorld world;

    private RoleFactory() {
    }

    public static void setWorld(final IWorld world) {
        RoleFactory.world = world;
    }

    /**
     * Gets a role using its type.
     *
     * @param roleType The type of role to get.
     *
     * @return The role with the specified type.
     *
     * @throws java.util.NoSuchElementException If there is no such role.
     */
    public static AbstractRole fromType(final RoleType roleType) {
        return all()
            .stream()
            .filter(role -> role.getType().equals(roleType))
            .findFirst()
            .orElseThrow();
    }

    /**
     * Creates an instance of every type of role.
     *
     * @return All roles.
     */
    public static Collection<AbstractRole> all() {
        final ArrayList<AbstractRole> roles = new ArrayList<>();
        roles.add(lumberjack());
        roles.add(farmer());
        roles.add(guard());
        roles.add(miner());
        roles.add(fisher());
        roles.add(builder());
        roles.add(idle());
        return roles;
    }

    /**
     * Creates a new lumberjack role.
     *
     * @return A new lumberjack role.
     */
    public static AbstractRole lumberjack() {
        return new LumberjackRole();
    }

    /**
     * Creates a new farmer role.
     *
     * @return A new farmer role.
     */
    public static AbstractRole farmer() {
        return new FarmerRole();
    }

    /**
     * Creates a new guard role.
     *
     * @return A new guard role.
     */
    public static AbstractRole guard() {
        return new GuardRole();
    }

    /**
     * Creates a new miner role.
     *
     * @return A new miner role.
     */
    public static AbstractRole miner() {
        return new MinerRole();
    }

    /**
     * Creates a new fisher role.
     *
     * @return A new fisher role.
     */
    public static AbstractRole fisher() {
        return new FisherRole();
    }

    /**
     * Creates a new builder role.
     *
     * @return A new builder role.
     */
    public static AbstractRole builder() {
        return new BuilderRole();
    }

    /**
     * Creates a new idle role with random movement.
     *
     * @return A new randomly moving idle role.
     */
    public static AbstractRole idle() {
        Objects.requireNonNull(
            world,
            "World can not be null. Call RoleFactory.setWorld to set it.");
        return new IdleRole(world);
    }

}
