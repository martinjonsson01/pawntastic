package com.thebois.models.beings.tasks;

import com.thebois.models.beings.ITaskPerformer;

/**
 * An internal interface representing an action to be performed in a certain way.
 */
interface ITask {

    /**
     * Performs the task on a given performer.
     *
     * @param performer The entity to execute the task for.
     */
    void perform(ITaskPerformer performer);

}
