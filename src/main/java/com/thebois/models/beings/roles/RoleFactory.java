package com.thebois.models.beings.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.IStructureFinder;
import com.thebois.models.world.IWorld;

/**
 * Creates requested instances of specific roles.
 *
 * @author Martin
 */
public final class RoleFactory {

    private static IWorld world;
    private static IResourceFinder resourceFinder;
    private static IStructureFinder structureFinder;

    private RoleFactory() {
    }

    public static void setWorld(final IWorld world) {
        RoleFactory.world = world;
    }

    public static void setResourceFinder(final IResourceFinder finder) {
        RoleFactory.resourceFinder = finder;
    }

    public static void setStructureFinder(final IStructureFinder structureFinder) {
        RoleFactory.structureFinder = structureFinder;
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
        assertDependenciesNotNull();
        return new LumberjackRole(resourceFinder, world);
    }

    /**
     * Creates a new miner role.
     *
     * @return A new miner role.
     */
    public static AbstractRole miner() {
        assertDependenciesNotNull();
        return new MinerRole(resourceFinder, world);
    }

    /**
     * Creates a new fisher role.
     *
     * @return A new fisher role.
     */
    public static AbstractRole fisher() {
        assertDependenciesNotNull();
        return new FisherRole(resourceFinder, world);
    }

    /**
     * Creates a new builder role.
     *
     * @return A new builder role.
     */
    public static AbstractRole builder() {
        return new BuilderRole(structureFinder, world);
    }

    /**
     * Creates a new idle role with random movement.
     *
     * @return A new randomly moving idle role.
     */
    public static AbstractRole idle() {
        assertDependenciesNotNull();
        return new IdleRole(world);
    }

    private static void assertDependenciesNotNull() {
        Objects.requireNonNull(world,
                               "World can not be null. Call RoleFactory.setWorld to set it.");
        Objects.requireNonNull(
            resourceFinder,
            "Resource Finder can not be null. Call RoleFactory.setResourceFinder to set it.");
        Objects.requireNonNull(
            structureFinder,
            "Structure Finder can not be null. Call RoleFactory.setStructureFinder to set it.");
    }

}
