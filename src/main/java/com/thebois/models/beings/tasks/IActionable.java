package com.thebois.models.beings.tasks;

/**
 * Represents a generator of actions.
 * <p>
 * Using this interface instead of storing actions directly means that they can be generated on the
 * fly depending on current conditions.
 * </p>
 */
public interface IActionable {

    /**
     * Gets the action.
     *
     * @return The generated action.
     */
    ITask get();

}
