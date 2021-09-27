package com.thebois.models.world.terrains;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class DirtTest {

    @Test
    public void getTypeReturnsDirtType() {
        // Arrange
        final Dirt dirt = new Dirt(0f, 0f);

        // Act
        final TerrainType type = dirt.getType();

        // Assert
        assertThat(type).isEqualTo(TerrainType.DIRT);
    }

}
