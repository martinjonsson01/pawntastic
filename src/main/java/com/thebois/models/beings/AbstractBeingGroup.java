package com.thebois.models.beings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

import com.google.common.eventbus.Subscribe;

import com.thebois.listeners.IEventBusSource;
import com.thebois.listeners.events.OnDeathEvent;

/**
 * An abstract implementation of IBeingGroup.
 */
public abstract class AbstractBeingGroup implements IBeingGroup {

    private final IEventBusSource eventBusSource;
    private Collection<IBeing> beings = new ArrayList<>();
    private Collection<IBeing> deadBeings = new ArrayList<>();

    protected AbstractBeingGroup(final IEventBusSource eventBusSource) {
        this.eventBusSource = eventBusSource;
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
        deadBeings = new ArrayList<>();
        beings.forEach(being -> being.update(deltaTime));
    }

    @Override
    public Collection<IBeing> getBeings() {
        final Collection<IBeing> clonedBeings = new ArrayList<>(beings.size());
        clonedBeings.addAll(beings);
        return clonedBeings;
    }

    protected void setBeings(final Collection<IBeing> beings) {
        this.beings = beings;
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

    @Serial
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Registers every time on deserialization because it might be registered to an old instance
        // of the event bus.
        // (caused by saving/loading).
        eventBusSource.getEventBus().register(this);
    }

}
