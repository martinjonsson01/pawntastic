package com.thebois.models.world.structures;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.world.inventory.IItem;

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
    public void deliverItemTest() {
        // Arrange
        final House house = new House(new Position());
        final IItem item = new IItem() {
        };

        // Act

        // Assert
        assertThat(house.deliverItem(item)).isTrue();
    }

    @Test
    public void builtStatusTest() {
        // Arrange
        final House house = new House(new Position());
        final IItem item = new IItem() {
        };

        // Act
        house.deliverItem(item);

        // Assert
        assertThat(house.builtStatus()).isEqualTo(0.01f);
    }

    @Test
    public void dismantleTest() {
        // Arrange
        final House house = new House(new Position());
        final IItem item = new IItem() {
        };
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
        final IItem item = new IItem() {
        };
        final Collection<IItem> items = new ArrayList<>();
        items.add(item);

        // Act

        // Assert
        assertThat(house.dismantle(item)).isFalse();
    }

}
