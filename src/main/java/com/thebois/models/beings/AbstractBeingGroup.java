package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;

import com.thebois.listeners.IEventListener;
import com.thebois.listeners.IEventSource;
import com.thebois.listeners.events.OnDeathEvent;

/**
 * An abstract implementation of IBeingGroup.
 */
public abstract class AbstractBeingGroup implements IBeingGroup, IEventSource<OnDeathEvent> {

    private Collection<IBeing> beings = new ArrayList<>();
    private final Collection<IEventListener<OnDeathEvent>> onDeathEventListeners =
        new ArrayList<>();

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
        final Collection<IBeing> deadBeings = new ArrayList<>();
        beings.forEach(being -> {
            if (being.getHealthRatio() == 0) {
                deadBeings.add(being);
                notifyOnDeathListeners(being);
            }
            else {
                being.update(deltaTime);
            }
        });
        beings.removeAll(deadBeings);
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

    @Override
    public void registerListener(final IEventListener<OnDeathEvent> listener) {
        onDeathEventListeners.add(listener);
    }

    @Override
    public void removeListener(final IEventListener<OnDeathEvent> listener) {
        onDeathEventListeners.add(listener);
    }

    private void notifyOnDeathListeners(final IBeing deadBeing) {
        final OnDeathEvent event = new OnDeathEvent(deadBeing);
        onDeathEventListeners.forEach(listener -> listener.onEvent(event));
    }

}
