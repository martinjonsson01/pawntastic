package com.thebois.models.beings.actions;

import com.thebois.models.beings.ITaskPerformer;

/**
 * An internal interface representing an action to be performed in a certain way.
 */
public interface IAction {

    /**
     * Performs the task on a given performer.
     *
     * @param performer The entity to execute the task for.
     */
    void perform(ITaskPerformer performer);

    /**
     * Checks if the task is finished.
     *
     * @return Whether the task is finished.
     */
    boolean isCompleted();

}
