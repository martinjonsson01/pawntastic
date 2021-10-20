package com.thebois.models.beings.roles;

/**
 * The miner digs stone out of rocks.
 *
 * @author Martin
 */
class MinerRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.MINER;
    }

}
