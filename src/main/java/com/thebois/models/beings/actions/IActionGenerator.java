package com.thebois.models.beings.actions;

import com.thebois.models.beings.ITaskPerformer;

/**
 * Represents a generator of actions.
 * <p>
 * Using this interface instead of storing actions directly means that they can be generated on the
 * fly depending on current conditions.
 * </p>
 */
public interface IActionGenerator {

    /**
     * Generates an action.
     *
     * @param performer The entity who will perform the generated action.
     *
     * @return The generated action.
     */
    IAction generate(ITaskPerformer performer);

}