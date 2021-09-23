package com.thebois.models.world;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.world.structures.IStructure;

import static org.assertj.core.api.Assertions.*;

public class WorldTests {

    @Test
    public void worldInitiated() {
        // Arrange
        final Collection<ITerrain> expectedTerrainTiles = mockTerrainTiles();
        final World world = new World(2, 5);

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
        final World world = new World(2, 5);

        // Act
        final Object worldObject = world.find();

        // Assert
        assertThat(worldObject).isEqualTo(null);
    }

    @Test
    public void createWorldWithNoStructures() {
        // Arrange
        final World world = new World(2, 0);

        // Act
        final Collection<IStructure> structures = world.getStructures();

        // Assert
        assertThat(structures.size()).isEqualTo(0);
    }

    @Test
    public void numberOfStructuresIncreasesIfStructureSuccessfullyPlaced() {
        // Arrange
        final World world = new World(2, 0);
        final Position position = new Position(1, 1);
        final Collection<IStructure> structures;

        // Act
        world.createStructure(position);
        structures = world.getStructures();

        // Assert
        assertThat(structures.size()).isEqualTo(1);
    }

    @Test
    public void numberOfStructuresDoesNotChangeIfStructuresUnsuccessfullyPlaced() {
        // Arrange
        final World world = new World(2, 0);
        final Position position = new Position(1, 1);
        final Collection<IStructure> structures;

        // Act
        world.createStructure(position);
        world.createStructure(position);
        world.createStructure(position);
        structures = world.getStructures();

        // Assert
        assertThat(structures.size()).isEqualTo(1);
    }

    @Test
    public void numberOfStructuresDoesNotChangeIfStructuresPlacedOutsideWorld() {
        // Arrange
        final World world = new World(2, 0);
        final Position position1 = new Position(2, 2);
        final Position position2 = new Position(-1, -1);
        final Collection<IStructure> structures;

        // Act
        world.createStructure(position1);
        world.createStructure(position2);
        structures = world.getStructures();

        // Assert
        assertThat(structures.size()).isEqualTo(0);
    }

    private boolean matrixEquals(final ITile[][] worldMatrix1, final ITile[][] worldMatrix2) {
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
