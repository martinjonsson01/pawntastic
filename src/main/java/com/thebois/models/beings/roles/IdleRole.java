package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;

import com.thebois.models.Position;
import com.thebois.models.beings.tasks.ITaskGenerator;
import com.thebois.models.beings.tasks.TaskFactory;
import com.thebois.models.world.IWorld;

/**
 * The builder constructs buildings.
 */
class IdleRole extends AbstractRole {

    private IWorld world;

    /**
     * Instantiates with a world to randomly move around in.
     *
     * @param world The world to move around in.
     */
    IdleRole(final IWorld world) {
        this.world = world;
    }

    /**
     * Instantiates with no random movement.
     */
    IdleRole() {

    }

    @Override
    public RoleType getType() {
        return RoleType.IDLE;
    }

    @Override
    protected Collection<ITaskGenerator> getTaskGenerators() {
        final ITaskGenerator randomMove = getRandomMove();
        return List.of(randomMove);
    }

    private ITaskGenerator getRandomMove() {
        if (world == null) {
            return () -> TaskFactory.createMoveTo(new Position(0, 0));
        }
        return () -> TaskFactory.createMoveTo(world.getRandomVacantSpot().getPosition());
    }

}
