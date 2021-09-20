package com.thebois.models.world;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class WorldTests {

    @Test
    public void worldInitiated() {
        // Arrange
        final Collection<ITerrain> expectedTerrainTiles = mockTerrainTiles();
        final World world = new World(2);

        // Act
        final Collection<ITerrain> terrainTiles = world.getTerrainTiles();

        // Assert
        assertThat(terrainTiles).containsAll(expectedTerrainTiles);
    }

    private Collection<ITerrain> mockTerrainTiles() {
        final ArrayList<ITerrain> terrainTiles = new ArrayList<>();
        terrainTiles.add(new Grass(0, 0));
        terrainTiles.add(new Grass(0, 1));
        terrainTiles.add(new Grass(1, 0));
        terrainTiles.add(new Grass(1, 1));
        return terrainTiles;
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

    @Test
    public void getStructureReturnsStructuresAddedToWorld() {

    }

    @Test
    public void createStructureTest() {
        // Arrange
        final World world = new World(25);

        // Act
        final Boolean isEmpty = world.getStructures().isEmpty();
        final Boolean isStructureBuilt = world.createStructure(new Position(0, 0));
        final int numberOfStructures = world.getStructures().size();

        // Assert
        assertThat(isEmpty).isTrue();
        assertThat(isStructureBuilt).isTrue();
        assertThat(numberOfStructures).isEqualTo(1);
    }

    private boolean matrixEquals(ITile[][] worldMatrix1, ITile[][] worldMatrix2) {
        for (int y = 0; y < worldMatrix2.length; y++) {
            final ITile[] row = worldMatrix2[y];
            for (int x = 0; x < row.length; x++) {
                if (worldMatrix1[y][x] != null) {
                    if (!worldMatrix1[y][x].equals(worldMatrix2[y][x])) {
                        return false;
                    }
                }
                else {
                    if (worldMatrix2[y][x] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
