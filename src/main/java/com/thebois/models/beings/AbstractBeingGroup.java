package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;

import com.thebois.models.Position;

public class AbstractBeingGroup implements IBeingGroup {

    protected final Collection<IBeing> beings = new ArrayList<>();

    @Override
    public Collection<IBeing> getBeings() {
        return beings;
    }

    @Override
    public void update() {
        for (IBeing being : this.beings) {
            being.update();
        }
    }

    /**
     * Creates a BeingGroup containing 5 Beings.
     */
    public AbstractBeingGroup() {
        createBeing(5);
    }

    /**
     * Creates an AbstractBeingGroup containing n IBeings.
     *
     * @param numberOfBeings the number of IBeings to create.
     */
    public AbstractBeingGroup(int numberOfBeings) {
        createBeing(numberOfBeings);
    }

    private void createBeing(int numberOfBeings) {
        for (int i = 0; i < numberOfBeings; i++) {
            beings.add(new Pawn());
        }
    }

}
