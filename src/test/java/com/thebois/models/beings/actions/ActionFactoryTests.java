package com.thebois.models.beings.actions;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ActionFactoryTests {

    @Test
    public void createMoveToThrowsWhenPathFinderNotSet() {
        // Assert
        assertThrows(NullPointerException.class, () -> ActionFactory.createMoveTo(new Position()));
    }

}
