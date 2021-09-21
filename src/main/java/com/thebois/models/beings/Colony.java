package com.thebois.models.beings;

import com.thebois.models.Position;

/**
 * A Colony is a collection of Pawns that can be controlled by the player.
 */
public class Colony extends AbstractBeingGroup {

    /**
     * Creates an instance of Colony with no pawns.
     */
    public Colony() {
    }

    /**
     * Creates an instance of Colony with n number of pawns.
     *
     * @param numberOfPawns number of pawns to instantiate.
     */
    public Colony(final int numberOfPawns) {
        createBeings(numberOfPawns);
    }

    private void createBeings(final int numberOfBeings) {
        for (int i = 0; i < numberOfBeings; i++) {
            final Position originPosition = new Position(0, 0);
            addBeing(new Pawn(originPosition, originPosition));
        }
    }

    @Override
    public IBeingGroup deepClone() {
        final Colony clone = new Colony();
        clone.setBeings(getBeings());
        return clone;
    }

}
