package com.thebois.testutils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.thebois.models.Position;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.ITile;
import com.thebois.models.world.World;
import com.thebois.models.world.structures.IStructure;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Creates standard mocks of commonly mocked objects.
 */
public final class MockFactory {

    private MockFactory() {

    }

    public static IStructure createStructure(final Position position, final boolean completed) {
        final IStructure structure = mock(IStructure.class);
        when(structure.getPosition()).thenReturn(position);
        when(structure.isCompleted()).thenReturn(completed);
        return structure;
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

    public static void completeAllStructures(final World world) {
        for (final IStructure structure : world.getStructures()) {
            final Collection<ItemType> neededItemTypes = structure.getNeededItems();
            final List<IItem> neededItems =
                neededItemTypes.stream().map(ItemFactory::fromType).collect(Collectors.toList());
            neededItems.forEach(structure::tryDeliverItem);
        }
    }

}
