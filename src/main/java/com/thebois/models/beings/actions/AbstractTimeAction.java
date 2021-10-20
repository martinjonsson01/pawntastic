package com.thebois.models.beings.actions;

import java.io.Serializable;

import com.thebois.models.beings.IActionPerformer;

/**
 * An action that takes time to perform.
 */
public abstract class AbstractTimeAction implements IAction, Serializable {

    private final float completionTime;
    private float timeSpentPerforming = 0f;

    /**
     * Instantiates with a completion time.
     *
     * @param completionTime How many seconds it takes to perform.
     */
    protected AbstractTimeAction(final float completionTime) {
        this.completionTime = completionTime;
    }

    @Override
    public void perform(final IActionPerformer performer, final float deltaTime) {
        timeSpentPerforming += deltaTime;
        if (isCompleted(performer)) {
            onPerformCompleted(performer);
        }
    }

    @Override
    public boolean isCompleted(final IActionPerformer performer) {
        return timeSpentPerforming >= completionTime;
    }

    /**
     * Called when enough time has been spent performing that the action is completed.
     *
     * @param performer The performer which performed most recently.
     */
    protected abstract void onPerformCompleted(IActionPerformer performer);

    /**
     * Gets how much time has been spent performing, so far.
     *
     * @return The time in seconds.
     */
    protected float getTimeSpentPerforming() {
        return timeSpentPerforming;
    }

}
