package com.thebois.models.world.terrains;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class TerrainTests {

    @Test
    public void getPositionIsEqualToConstructorPosition() {
        // Arrange
        final Position position = new Position(1, 1);
        final ITerrain terrain1 = TerrainFactory.createTerrain(TerrainType.GRASS,1,1);

        // Act
        final Position getPosition = terrain1.getPosition();

        // Assert
        assertThat(getPosition).isEqualTo(position);
    }

    // Hash Code Tests
    @Test
    public void terrainHashCodeIsSameForItSelf() {
        // Arrange
        final Position position = new Position(1, 1);
        final AbstractTerrain terrain = new Grass(position);

        // Act
        final boolean isEqual = terrain.hashCode() == terrain.hashCode();

        // Assert
        assertThat(isEqual).isTrue();
    }

    @Test
    public void terrainHashCodeIsSameForIdenticalTerrain() {
        // Arrange
        final Position position1 = new Position(1, 1);
        final Position position2 = new Position(1, 1);
        final AbstractTerrain terrain1 = new Grass(position1);
        final AbstractTerrain terrain2 = new Grass(position2);

        // Act
        final boolean isEqual = terrain1.hashCode() == terrain2.hashCode();

        // Assert
        assertThat(isEqual).isTrue();
    }

    @Test
    public void terrainHashCodeIsNotSameForDifferentTerrain() {
        // Arrange
        final Position position1 = new Position(1, 1);
        final Position position2 = new Position(2, 2);
        final AbstractTerrain terrain1 = new Grass(position1);
        final AbstractTerrain terrain2 = new Grass(position2);

        // Act

        final boolean isEqual = terrain1.hashCode() == terrain2.hashCode();

        // Assert
        assertThat(isEqual).isFalse();
    }

    // Equal Tests

    @Test
    public void terrainIsEqualToItSelf() {
        // Arrange
        final ITerrain terrain = TerrainFactory.createTerrain(TerrainType.GRASS,1,1);

        // Act
        final boolean isEqual = terrain.equals(terrain);

        // Assert
        assertThat(isEqual).isTrue();
    }

    @Test
    public void terrainIsNotEqualToNull() {
        // Arrange
        final ITerrain terrain = TerrainFactory.createTerrain(TerrainType.GRASS,1,1);

        // Act
        final boolean isEqual = terrain.equals(null);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void terrainIsNotEqualToOtherObject() {
        // Arrange
        final ITerrain terrain = TerrainFactory.createTerrain(TerrainType.GRASS,1,1);
        final Object otherObject = new Position(1, 1);

        // Act
        final boolean isEqual = terrain.equals(otherObject);
        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void terrainIsNotEqualToTerrainWithOtherPosition() {
        // Arrange
        final ITerrain terrain1 = TerrainFactory.createTerrain(TerrainType.GRASS,1,1);
        final ITerrain terrain2 = TerrainFactory.createTerrain(TerrainType.GRASS,2,2);

        // Act
        final boolean isEqual = terrain1.equals(terrain2);
        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void terrainIsEqualToIdenticalTerrain() {
        // Arrange
        final Position position = new Position(1, 1);
        final AbstractTerrain terrain1 = new Grass(position);
        final AbstractTerrain terrain2 = new Grass(position);

        // Act
        final boolean isEqual = terrain1.equals(terrain2);
        // Assert
        assertThat(isEqual).isTrue();
    }

}
