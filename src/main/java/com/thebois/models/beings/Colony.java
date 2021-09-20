package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;

import com.thebois.models.Position;

/**
 * A Colony is a collection of Pawns that can be controlled by the player.
 */
public class Colony implements IBeingGroup {

    private final Collection<IBeing> pawns = new ArrayList<>();

    @Override
    public Collection<IBeing> getBeings() {
        return pawns;
    }

    @Override
    public void createBeing() {
        final Position spawnPosition = new Position();
        pawns.add(new Pawn(spawnPosition));
    }

    @Override
    public void createBeing(Position spawnPosition) {
        pawns.add(new Pawn(spawnPosition));
    }

    @Override
    public void createBeing(Position spawnPosition, int numberOfNewBeings) {
        for (int i = 0; i < numberOfNewBeings; i++) {
            pawns.add(new Pawn(spawnPosition));
        }
    }

    @Override
    public void update() {
        for (IBeing pawn : this.pawns) {
            pawn.update();
        }
    }

}
