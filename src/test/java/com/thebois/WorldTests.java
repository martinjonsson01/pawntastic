package com.thebois;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import com.thebois.models.World;

public class WorldTests {

    @Test
    public void worldInitiated() {

        final int[][] testWorld = new int[2][2];

        final World world = new World(2);
        final int[][] worldMatrix = world.getWorld();
        assertThat(testWorld.length).isEqualTo(worldMatrix.length);
    }
}
