package com.thebois.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class WorldTests {

    @Test
    public void worldInitiated() {
        // Arrange
        final ITile[][] testWorld = new ITile[2][2];
        final World world = new World(2);

        // Act
        final ITile[][] worldMatrix = world.getWorld();

        // Assert
        assertThat(matrixEquals(worldMatrix, testWorld)).isTrue();
    }

    @Test
    public void worldFind() {
        // Arrange
        final World world = new World(2);

        // Act
        final Object worldObject = world.find();

        // Assert
        assertThat(worldObject).isEqualTo(null);
    }

    private boolean matrixEquals(ITile[][] worldMatrix1, ITile[][]worldMatrix2) {
        for (int y = 0; y < worldMatrix2.length; y++) {
            final ITile[] row = worldMatrix2[y];
            for (int x = 0; x < row.length; x++) {
                if (worldMatrix1[y][x] != null) {
                    if (!worldMatrix1[y][x].equals(worldMatrix2[y][x])) return false;
                }
                else {
                    if (worldMatrix2[y][x] != null) return false;
                }
            }
        }
        return true;
    }
}
