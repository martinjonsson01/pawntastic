package com.thebois.models.beings.roles;

/**
 * The lumberjack chops down trees to collect wood and store it in stockpiles.
 *
 * @author Martin
 */
class LumberjackRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.LUMBERJACK;
    }

}
