package com.thebois.models.beings.roles;

import java.util.Collection;

import com.thebois.models.beings.actions.IActionGenerator;

/**
 * The miner digs stone out of rocks.
 */
class MinerRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.MINER;
    }

    @Override
    protected Collection<IActionGenerator> getTaskGenerators() {
        return RoleFactory.idle().getTaskGenerators();
    }

}
