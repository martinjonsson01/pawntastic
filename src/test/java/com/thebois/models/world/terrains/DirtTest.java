package com.thebois.models.world.terrains;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

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

    @Test
    public void getCostReturnsZero() {
        // Arrange
        final Dirt dirt = new Dirt(0, 0);
        final float expectedCost = 0;
        final float actualCost;

        // Act
        actualCost = dirt.getCost();

        // Assert
        assertThat(actualCost).isEqualTo(expectedCost);
    }

    @Test
    public void deepCloneableReturnsCopyThatIsEqualToOriginal() {
        // Arrange
        final Position position = new Position(0, 0);
        final Dirt dirt = new Dirt(position);
        final Dirt deepClone;

        // Act
        deepClone = dirt.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(dirt);
    }

}
