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
     * @param performer The entity to check action completion for.
     *
     * @return Whether the task is finished.
     */
    boolean isCompleted(ITaskPerformer performer);

    /**
     * Checks if a given performer is able to currently perform.
     *
     * @param performer The entity to check is able to perform.
     *
     * @return Whether the performer is currently able to perform.
     */
    boolean canPerform(ITaskPerformer performer);

}
