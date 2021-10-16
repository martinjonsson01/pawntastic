package com.thebois.testutils;

import com.thebois.models.Position;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.world.ITile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Creates standard mocks of commonly mocked objects.
 */
public final class MockFactory {

    private MockFactory() {

    }

    public static ITile createTile(final int x, final int y) {
        final ITile tile = mock(ITile.class);
        when(tile.getPosition()).thenReturn(new Position(x, y));
        return tile;
    }

    public static IAction createAction(final boolean isCompleted, final boolean canPerform) {
        final IAction action = mock(IAction.class);
        when(action.isCompleted(any())).thenReturn(isCompleted);
        when(action.canPerform(any())).thenReturn(canPerform);
        return action;
    }

}
