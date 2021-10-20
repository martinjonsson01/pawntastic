package com.thebois.models.beings.actions;

import java.io.Serializable;

import com.thebois.models.beings.IActionPerformer;

/**
 * Makes the performer do nothing at all.
 * <p>
 * Can always be performed. This action always completes after being performed once. It is an action
 * given to a being when it asks for an action but there is nothing more to do.
 * </p>
 */
public class DoNothingAction implements IAction, Serializable {

    private boolean isDone = false;

    @Override
    public void perform(final IActionPerformer performer) {
        isDone = true;
    }

    @Override
    public boolean isCompleted(final IActionPerformer performer) {
        return isDone;
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        return o instanceof DoNothingAction;
    }

}
