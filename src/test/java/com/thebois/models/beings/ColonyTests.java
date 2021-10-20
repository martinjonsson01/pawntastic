package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.models.IPositionFinder;
import com.thebois.models.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.terrains.Grass;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ColonyTests {

    public static Stream<Arguments> getItemTypes() {
        return Stream.of(Arguments.of(ItemType.LOG), Arguments.of(ItemType.ROCK));
    }

    @Test
    public void isFullReturnsFalse() {
        // Arrange
        final Colony colony = new Colony(
            List.of(),
            mock(IPathFinder.class),
            mock(IStructureFinder.class));

        // Act
        final boolean isFull = colony.isFull();

        // Assert
        assertThat(isFull).isFalse();
    }

    @Test
    public void constructWithTilesCreatesOneBeingPerPosition() {
        // Arrange
        final int beingCount = 25;
        final List<Position> positions = new ArrayList<>(beingCount);
        for (int i = 0; i < beingCount; i++) {
            positions.add(new Position(0, 0));
        }
        final IWorld mockWorld = mock(IWorld.class);
        when(mockWorld.getTileAt(any())).thenReturn(new Grass(new Position()));

        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);

        final IStructureFinder structureFinder = Mockito.mock(IStructureFinder.class);
        final IPositionFinder positionFinder = Mockito.mock(IPositionFinder.class);

        // Act
        final Colony colony = new Colony(positions, pathFinder, structureFinder, positionFinder);

        // Assert
        assertThat(colony.getBeings().size()).isEqualTo(beingCount);
    }

    @ParameterizedTest
    @MethodSource("getItemTypes")
    public void emptyColonyInventoryIsEmpty(final ItemType itemType) {
        // Arrange
        final Colony colony = mockColony();

        // Act
        final Exception exception = assertThrows(IllegalArgumentException.class,
                                                 () -> colony.take(itemType));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Specified ItemType not in inventory");
    }

    private Colony mockColony() {
        final List<Position> positions = new ArrayList<>();
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final IStructureFinder structureFinder = Mockito.mock(IStructureFinder.class);
        return new Colony(positions, pathFinder, structureFinder);
    }

    @Test
    public void canAddAndTakeItemToInventory() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));
        final IItem item = colony.take(ItemType.LOG);

        // Assert
        assertThat(item.getType()).isEqualTo(ItemType.LOG);
    }

    @Test
    public void countIsZeroWhenInventoryIsEmpty() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        final int count = colony.numberOf(ItemType.ROCK);

        // Assert
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void countIsCorrectCountWhenInventoryGotSpecifiedItemTypeInIt() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));

        final int count = colony.numberOf(ItemType.LOG);

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void countIsCorrectValueWhenInventoryDoesNotHaveSpecifiedItemType() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));

        final int count = colony.numberOf(ItemType.ROCK);

        // Assert
        assertThat(count).isEqualTo(0);
    }

    private Colony mockColony() {
        final List<Position> positions = new ArrayList<>();
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final IStructureFinder structureFinder = Mockito.mock(IStructureFinder.class);
        final IPositionFinder positionFinder = Mockito.mock(IPositionFinder.class);
        return new Colony(positions, pathFinder, structureFinder, positionFinder);
    }

    @Test
    public void takeMultipleItemsFromColony() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));

        final ArrayList<IItem> result = colony.takeAmount(ItemType.LOG, 2);

        // Assert
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getType()).isEqualTo(ItemType.LOG);
        assertThat(result.get(1).getType()).isEqualTo(ItemType.LOG);
    }

    @Test
    public void canNotTakeMultipleItemsFromColony() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));

        final Exception exception = assertThrows(IllegalArgumentException.class,
                                                 () -> colony.takeAmount(ItemType.ROCK, 2));

        // Assert
        assertThat(exception.getMessage()).isEqualTo(
            "Not enough of the specified ItemType in the inventory");
    }

    @Test
    public void colonyHasItem() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));
        final boolean result = colony.hasItem(ItemType.LOG);

        assertThat(result).isTrue();
    }

    @Test
    public void colonyDoesNotHaveItem() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        final boolean result = colony.hasItem(ItemType.LOG);

        assertThat(result).isFalse();
    }

    @Test
    public void colonyHasMultipleOfItem() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));

        final boolean result = colony.hasItem(ItemType.LOG, 2);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void emptyInventoryDoesNotHaveItemsSuccess() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        final boolean result = colony.hasItem(ItemType.LOG, 2);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void falseWhenInventoryDoesNotHaveRequestedAmountOfItemsSuccess() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        colony.tryAdd(ItemFactory.fromType(ItemType.LOG));
        final boolean result = colony.hasItem(ItemType.LOG, 2);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void addMultipleItemsToColony() {
        // Arrange
        final Colony colony = mockColony();
        final ArrayList<IItem> items = new ArrayList<>();
        items.add(ItemFactory.fromType(ItemType.LOG));
        items.add(ItemFactory.fromType(ItemType.LOG));

        // Act
        colony.addMultiple(items);

        // Assert
        assertThat(colony.hasItem(ItemType.LOG, 2)).isTrue();
    }

    @Test
    public void getInventoryReturnsTheColonyInventory() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        final IInventory inventory = colony.getInventory();
        final boolean colonyResult = colony.hasItem(ItemType.LOG, 2);
        final boolean inventoryResult = inventory.hasItem(ItemType.LOG, 2);

        // Assert
        assertThat(colonyResult).isEqualTo(inventoryResult);
    }

    @Test
    public void ensureBeingsUpdatesWhenColonyUpdates() {
        // Arrange
        final IBeing being = Mockito.mock(IBeing.class);
        final Colony colony = mockColony();

        colony.addBeing(being);
        final float deltaTime = 0.1f;

        // Act
        colony.update(deltaTime);

        // Assert
        verify(being, times(1)).update(deltaTime);
    }

}
