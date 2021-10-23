package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.common.eventbus.Subscribe;

import com.thebois.listeners.IEventBusSource;
import com.thebois.listeners.events.OnDeathEvent;

/**
 * An abstract implementation of IBeingGroup.
 */
public abstract class AbstractBeingGroup implements IBeingGroup {

    private final Collection<IBeing> beings = new ConcurrentLinkedQueue<>();
    private final Collection<IBeing> deadBeings = new ConcurrentLinkedQueue<>();

    protected AbstractBeingGroup(final IEventBusSource eventBusSource) {
        eventBusSource.getEventBus().register(this);
    }

    /**
     * Adds a being to the internal collection of beings.
     *
     * @param being IBeing to be added.
     */
    protected void addBeing(final IBeing being) {
        beings.add(being);
    }

    @Override
    public void update(final float deltaTime) {
        beings.removeAll(deadBeings);
        deadBeings.clear();
        beings.forEach(being -> being.update(deltaTime));
    }

    @Override
    public Collection<IBeing> getBeings() {
        final Collection<IBeing> clonedBeings = new ArrayList<>(beings.size());
        clonedBeings.addAll(beings);
        return clonedBeings;
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
