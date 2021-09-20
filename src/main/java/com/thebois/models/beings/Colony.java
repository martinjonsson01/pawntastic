package com.thebois.models.beings;

/**
 * A Colony is a collection of Pawns that can be controlled by the player.
 */
public class Colony extends AbstractBeingGroup {

    public Colony() {
        super();
    }

    public Colony(int numberOfPawns) {
        super(numberOfPawns);
    }
}
