package com.thebois.models;

import org.junit.jupiter.api.Test;

import com.thebois.models.tiles.ITile;

import static org.assertj.core.api.Assertions.*;

public class WorldTests {

    @Test
    public void worldInitiated() {
        final int[][] testWorld = new int[2][2];

        final World world = new World(2);
        final ITile[][] worldMatrix = world.getWorld();
        assertThat(testWorld.length).isEqualTo(worldMatrix.length);
    }
}
