package com.thebois.models.beings.actions;

import com.thebois.models.beings.ITaskPerformer;

/**
 * Makes the performer do nothing at all.
 * <p>
 * This action never completes by itself. It is an action given to a being when it asks for an
 * action but there is nothing more to do.
 * </p>
 */
public class DoNothingAction implements IAction {

    @Override
    public void perform(final ITaskPerformer performer) {

    }

    @Override
    public boolean isCompleted(final ITaskPerformer performer) {
        return false;
    }

}
