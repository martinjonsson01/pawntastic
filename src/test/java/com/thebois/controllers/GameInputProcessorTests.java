package com.thebois.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GameInputProcessorTests {

    @Test
    public void leftClickTest() {
        final GameInputProcessor gip = new GameInputProcessor();
        final Boolean isLeftClickPressed = gip.touchDown(0, 0, 0, 0);
        assertThat(isLeftClickPressed).as("Left click is pressed").isEqualTo(true);
    }

}
