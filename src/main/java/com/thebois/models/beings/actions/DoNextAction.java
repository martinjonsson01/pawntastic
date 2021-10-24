package com.thebois.models.beings.actions;

import java.io.Serializable;

import com.thebois.models.beings.IActionPerformer;

/**
 * Makes the performer skip an action and move onto next action.
 *
 * <p>
 * Be careful when using this action as it may create an infinite loop. As this action is always
 * completed.
 * </p>
 *
 * <p>
 * It is an action given to a being when it asks for an action but has to skip the action and move
 * on to next action.
 * </p>
 *
 * @author Mathias
 */
public class DoNextAction implements IAction, Serializable {

    @Override
    public void perform(final IActionPerformer performer, final float deltaTime) {
    }

    @Override
    public boolean isCompleted(final IActionPerformer performer) {
        return true;
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        return other instanceof DoNextAction;
    }

}
