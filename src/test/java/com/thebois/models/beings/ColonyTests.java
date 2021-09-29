package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.inventory.items.Log;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ColonyTests {

    public static Stream<Arguments> getItemTypes() {
        return Stream.of(Arguments.of(ItemType.LOG), Arguments.of(ItemType.ROCK));
    }

    @Test
    public void constructWithPositionsCreatesOneBeingPerPosition() {
        // Arrange
        final int beingCount = 25;
        final List<Position> positions = new ArrayList<>(beingCount);
        for (int i = 0; i < beingCount; i++) {
            positions.add(new Position());
        }

        // Act
        final Colony colony = new Colony(positions);

        // Assert
        assertThat(colony.getBeings().size()).isEqualTo(beingCount);
    }

    @Test
    public void updateCallsUpdateOnAllBeings() {
        // Arrange
        final int beingCount = 25;
        final Collection<IBeing> pawns = new ArrayList<>(beingCount);
        for (int i = 0; i < beingCount; i++) {
            pawns.add(Mockito.mock(IBeing.class));
        }
        final Colony colony = new Colony(pawns);

        // Act
        colony.update();

        // Assert
        for (final IBeing pawn : pawns) {
            verify(pawn, times(1)).update();
        }
    }

    @ParameterizedTest
    @MethodSource("getItemTypes")
    public void emptyColonyInventoryIsEmpty(final ItemType itemType) {
        // Arrange
        final Colony colony = mockColony();

        // Act
        final Optional<IItem> result = colony.takeItem(itemType);

        // Assert
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void canAddAndTakeItemToInventory() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        colony.addItem(new Log());
        final Optional<IItem> item = colony.takeItem(ItemType.LOG);

        // Assert
        assertThat(item.isPresent()).isTrue();
        assertThat(item.get().getType()).isEqualTo(ItemType.LOG);
    }

    @Test
    public void countIsZeroWhenInventoryIsEmpty() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        final int count = colony.getItemCount(ItemType.ROCK);

        // Assert
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void countIsCorrectCountWhenInventoryGotSpecifiedItemTypeInIt() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        colony.addItem(new Log());
        colony.addItem(new Log());

        final int count = colony.getItemCount(ItemType.LOG);

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void countIsCorrectValueWhenInventoryDoesNotHaveSpecifiedItemType() {
        // Arrange
        final Colony colony = mockColony();

        // Act
        colony.addItem(new Log());
        colony.addItem(new Log());

        final int count = colony.getItemCount(ItemType.ROCK);

        // Assert
        assertThat(count).isEqualTo(0);
    }

    private Colony mockColony() {
        final Collection<IBeing> pawns = new ArrayList<>(0);
        return new Colony(pawns);
    }

}
