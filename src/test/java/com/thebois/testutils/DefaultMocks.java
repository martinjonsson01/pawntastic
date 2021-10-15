package com.thebois.testutils;

import com.thebois.models.beings.actions.IAction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public final class DefaultMocks {

    private DefaultMocks() {

    }

    public static IAction createAction(final boolean isCompleted, final boolean canPerform) {
        final IAction action = mock(IAction.class);
        when(action.isCompleted(any())).thenReturn(isCompleted);
        when(action.canPerform(any())).thenReturn(canPerform);
        return action;
    }

}
