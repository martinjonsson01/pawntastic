package com.thebois.models.world.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.Log;
import com.thebois.models.inventory.items.Rock;

import static org.assertj.core.api.Assertions.*;

public class HouseTests {

    @Test
    public void getPositionSetInConstructor() {
        // Arrange
        final Position position = new Position(123, 456);
        final House structure = new House(123, 456);

        // Act
        final Position structurePosition = structure.getPosition();

        // Assert
        assertThat(structurePosition).isEqualTo(position);
    }

    @Test
    public void getTypeEqualsHouse() {
        // Arrange
        final Position position = new Position(123, 456);

        // Act
        final House structure = new House(position);

        // Assert
        assertThat(structure.getType()).isEqualTo(StructureType.HOUSE);
    }

    @Test
    public void testEqualsHouse() {
        // Arrange
        final Position position = new Position(123, 456);

        // Act
        final House structure = new House(position);

        // Assert
        assertThat(structure.getType()).isEqualTo(StructureType.HOUSE);
    }

    @Test
    public void deliverItemTest() {
        // Arrange
        final House house = new House(new Position());
        final IItem item = new Rock() {
        };

        // Act

        // Assert
        assertThat(house.deliverItem(item)).isTrue();
    }

    @Test
    public void deliverItemFailed() {
        // Arrange
        final House house = new House(new Position());
        final IItem item = new Rock() {
        };

        // Act
        for (int i = 0; i < 10; i++) {
            house.deliverItem(new Rock());
            house.deliverItem(new Log());
        }

        // Assert
        assertThat(house.deliverItem(item)).isFalse();
    }

    @Test
    public void builtStatusTest() {
        // Arrange
        final House house = new House(new Position());
        final IItem item = new Rock() {
        };

        // Act
        house.deliverItem(item);

        // Assert
        assertThat(house.builtStatus()).isEqualTo(1 / 20f);
    }

    @Test
    public void neededItemsEqualTest() {
        // Arrange
        final House house = new House(new Position());
        final IItem mockItem1 = Mockito.mock(IItem.class);
        final IItem mockItem2 = Mockito.mock(IItem.class);
        final IItem mockItem3 = Mockito.mock(IItem.class);

        final Collection<IItem> neededItems = List.of(mockItem1, mockItem2, mockItem3);
        final Collection<IItem> delivered = List.of(mockItem1);
        final Collection<IItem> expectedNeededItems = List.of(mockItem2, mockItem3);

        house.setAllNeededItems(neededItems);
        house.setDeliveredItems(delivered);

        // Act

        // Assert
        assertThat(house.neededItems()).isEqualTo(expectedNeededItems);
    }

    @Test
    public void neededItemsNotEqualTest() {
        // Arrange
        final House house = new House(new Position());
        final IItem mockItem1 = Mockito.mock(IItem.class);
        final IItem mockItem2 = Mockito.mock(IItem.class);
        final IItem mockItem3 = Mockito.mock(IItem.class);

        final Collection<IItem> neededItems = List.of(mockItem1, mockItem2, mockItem3);
        final Collection<IItem> delivered = List.of(mockItem1);
        final Collection<IItem> expectedNeededItems = List.of(mockItem1);

        house.setAllNeededItems(neededItems);
        house.setDeliveredItems(delivered);

        // Act

        // Assert
        assertThat(house.neededItems()).isNotEqualTo(expectedNeededItems);
    }

    @Test
    public void dismantleTest() {
        // Arrange
        final House house = new House(new Position());
        final IItem item = new Rock();
        final Collection<IItem> items = new ArrayList<>();
        items.add(item);

        house.setAllNeededItems(items);
        house.setDeliveredItems(items);

        // Act

        // Assert
        assertThat(house.dismantle(item)).isTrue();
    }

    @Test
    public void failToDismantleTest() {
        // Arrange
        final House house = new House(new Position());
        final IItem item = new Rock();

        // Act
        house.dismantle(item);

        // Assert
        assertThat(house.dismantle(item)).isFalse();
    }

    @Test
    public void houseHasNoNeededResourcesTest() {
        // Arrange
        final House house = new House(new Position());
        final IItem item = new Rock();
        house.setAllNeededItems(new ArrayList<>());

        // Act

        // Assert
        assertThat(house.builtStatus()).isEqualTo(1f);
    }

}
