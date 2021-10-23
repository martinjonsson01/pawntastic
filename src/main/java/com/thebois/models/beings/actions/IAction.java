package com.thebois.models.beings.actions;

import com.thebois.models.beings.IActionPerformer;

/**
 * Represents a task to be performed in a certain way.
 *
 * @author Martin
 */
public interface IAction {

    /**
     * Performs the action on a given performer.
     *
     * @param performer The entity to execute the action for.
     * @param deltaTime How much time to spend progressing the progress of the action.
     */
    void perform(IActionPerformer performer, float deltaTime);

    /**
     * Checks if the task is finished.
     *
     * @param performer The entity to check action completion for.
     *
     * @return Whether the task is finished.
     */
    boolean isCompleted(IActionPerformer performer);

    /**
     * Checks if a given performer is able to currently perform.
     *
     * @param performer The entity to check is able to perform.
     *
     * @return Whether the performer is currently able to perform.
     */
    boolean canPerform(IActionPerformer performer);

}
