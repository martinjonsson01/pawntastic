package com.thebois.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GameInputProcessorTests {

    @Test
    public void leftClickIsPressedTest() {
        final GameInputProcessor gip = new GameInputProcessor();
        final Boolean isLeftClickPressed = gip.touchDown(0, 0, 0, 0);
        assertThat(isLeftClickPressed).as("Left click is pressed").isEqualTo(true);
    }

    @Test
    public void leftClickIsNotPressedTest() {
        final GameInputProcessor gip = new GameInputProcessor();
        final Boolean isLeftClickPressed = gip.touchDown(0, 0, 0, 1);
        assertThat(isLeftClickPressed).as("Left click is not pressed").isEqualTo(false);
    }
}
