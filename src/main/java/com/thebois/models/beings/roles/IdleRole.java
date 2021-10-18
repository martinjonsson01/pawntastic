package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;

import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IActionSource;
import com.thebois.models.world.IWorld;

/**
 * The builder constructs buildings.
 */
class IdleRole extends AbstractRole {

    private final IWorld world;

    /**
     * Instantiates with a world to randomly move around in.
     *
     * @param world The world to move around in.
     */
    IdleRole(final IWorld world) {
        this.world = world;
    }

    @Override
    public RoleType getType() {
        return RoleType.IDLE;
    }

    @Override
    protected Collection<IActionSource> getTaskGenerators() {
        final IActionSource randomMove = getRandomMove();
        return List.of(randomMove);
    }

    private IActionSource getRandomMove() {
        return performer -> ActionFactory.createMoveTo(world.getRandomVacantSpot().getPosition());
    }

}
