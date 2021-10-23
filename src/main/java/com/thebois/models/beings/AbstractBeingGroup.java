package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.eventbus.Subscribe;

import com.thebois.listeners.IEventBusSource;
import com.thebois.listeners.events.OnDeathEvent;

/**
 * An abstract implementation of IBeingGroup.
 *
 * @author Jacob
 */
public abstract class AbstractBeingGroup implements IBeingGroup {

    private final Collection<IBeing> beings = new ArrayList<>();
    private final Collection<IBeing> deadBeings = new ArrayList<>();
    private final Collection<IBeing> newBeings = new ArrayList<>();

    protected AbstractBeingGroup(final IEventBusSource eventBusSource) {
        eventBusSource.getEventBus().register(this);
    }

    /**
     * Adds a being to the internal collection of beings.
     *
     * @param being IBeing to be added.
     */
    protected void addBeing(final IBeing being) {
        // Use proxy list to not interrupt current update-loop.
        newBeings.add(being);
    }

    @Override
    public void update(final float deltaTime) {
        beings.removeAll(deadBeings);
        beings.addAll(newBeings);
        deadBeings.clear();
        newBeings.clear();
        beings.forEach(being -> being.update(deltaTime));
    }

    @Override
    public Collection<IBeing> getBeings() {
        final Collection<IBeing> allBeings = new ArrayList<>(beings.size());
        allBeings.addAll(beings);
        // Include new beings since update might not have been run between them being added
        // and this method being called.
        allBeings.addAll(newBeings);
        return allBeings;
    }

    /**
     * Listens to the OnDeathEvent in order to remove dead beings from the colony.
     *
     * @param event The published event.
     */
    @Subscribe
    public void onDeathEvent(final OnDeathEvent event) {
        final IBeing deadBeing = event.getDeadBeing();
        deadBeings.add(deadBeing);
    }

}
