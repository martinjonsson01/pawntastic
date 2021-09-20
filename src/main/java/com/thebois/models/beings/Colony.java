package com.thebois.models.beings;

/**
 * A Colony is a collection of Pawns that can be controlled by the player.
 */
public class Colony extends AbstractBeingGroup {

    /**
     * Creates an instance of Colony with two pawns.
     */
    public Colony() {
        this.addBeing(new Pawn());
        this.addBeing(new Pawn());
    }

    /**
     * Creates an instance of Colony with n number of pawns.
     *
     * @param numberOfPawns number of pawns to instantiate.
     */
    public Colony(int numberOfPawns) {
        super(numberOfPawns);
    }

}
