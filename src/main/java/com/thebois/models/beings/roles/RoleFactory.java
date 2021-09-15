package com.thebois.models.beings.roles;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Creates requested instances of specific roles.
 */
public final class RoleFactory {

    private RoleFactory() {
    }

    /**
     * Creates an instance of every type of role.
     *
     * @return All roles
     */
    public static Collection<Role> all() {
        final ArrayList<Role> roles = new ArrayList<>();
        roles.add(lumberjack());
        roles.add(farmer());
        roles.add(guard());
        roles.add(miner());
        roles.add(fisher());
        roles.add(builder());
        return roles;
    }

    /**
     * Creates a new lumberjack role.
     *
     * @return A new lumberjack role
     */
    public static Role lumberjack() {
        return new LumberjackRole();
    }

    /**
     * Creates a new farmer role.
     *
     * @return A new farmer role
     */
    public static Role farmer() {
        return new FarmerRole();
    }

    /**
     * Creates a new guard role.
     *
     * @return A new guard role
     */
    public static Role guard() {
        return new GuardRole();
    }

    /**
     * Creates a new miner role.
     *
     * @return A new miner role
     */
    public static Role miner() {
        return new MinerRole();
    }

    /**
     * Creates a new fisher role.
     *
     * @return A new fisher role
     */
    public static Role fisher() {
        return new FisherRole();
    }

    /**
     * Creates a new builder role.
     *
     * @return A new builder role
     */
    public static Role builder() {
        return new BuilderRole();
    }

}
