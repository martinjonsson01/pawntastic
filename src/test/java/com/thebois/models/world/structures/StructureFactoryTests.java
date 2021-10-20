package com.thebois.models.world.structures;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.Inventory;

import static org.assertj.core.api.Assertions.*;

public class StructureFactoryTests {

    public static Stream<Arguments> getStructureTypeAndExpectedStructure() {
        return Stream.of(Arguments.of(StructureType.HOUSE, House.class),
                         Arguments.of(StructureType.STOCKPILE, Stockpile.class));
    }

    @ParameterizedTest
    @MethodSource("getStructureTypeAndExpectedStructure")
    public void factoryCreatesCorrectStructureWithGivenEnum(
        final StructureType type, final Class<IStructure> expectedStructure) {
        // Arrange
        final int x = 0;
        final int y = 0;

        // Act
        final IStructure actualStructure = StructureFactory.createStructure(type, x, y);

        // Assert
        assertThat(actualStructure).isInstanceOf(expectedStructure);
    }

    @Test
    public void stockpileInventoryIsTheInventoryProvidedInTheSetter(){
        // Arrange
        final Inventory factoryInventory = new Inventory();

        // Act
        StructureFactory.setInventory(factoryInventory);
        final Stockpile structure = (Stockpile) StructureFactory.createStructure(StructureType.STOCKPILE,0,0);
        final IInventory structureInventory = structure.getInventory();

        // Assert
        assertThat(structureInventory).isEqualTo(factoryInventory);
    }

}
