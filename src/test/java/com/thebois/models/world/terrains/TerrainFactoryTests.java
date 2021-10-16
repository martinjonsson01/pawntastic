package com.thebois.models.world.terrains;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

public class TerrainFactoryTests {

    public static Stream<Arguments> getTerrainTypeAndExpectedTerrain() {
        return Stream.of(
            Arguments.of(TerrainType.GRASS, new Grass(0, 0)),
            Arguments.of(TerrainType.SAND, new Sand(0, 0)),
            Arguments.of(TerrainType.DIRT, new Dirt(0, 0)));
    }

    @ParameterizedTest
    @MethodSource("getTerrainTypeAndExpectedTerrain")
    public void factoryCreatesCorrectTerrainWithGivenEnum(
        final TerrainType type, final ITerrain expectedResource) {
        // Arrange
        final int x = 0;
        final int y = 0;

        // Act
        final ITerrain actualResource = TerrainFactory.createTerrain(type, x, y);

        // Assert
        assertThat(actualResource).isEqualTo(expectedResource);
    }

    @Test
    public void createTerrainWithNullAsEnumThrowsUnsupportedOperationException() {
        // Arrange
        final int x = 0;
        final int y = 0;

        // Assert
        Assertions.assertThrows(NullPointerException.class, () -> {
            TerrainFactory.createTerrain(null, x, y);
        });
    }

}
