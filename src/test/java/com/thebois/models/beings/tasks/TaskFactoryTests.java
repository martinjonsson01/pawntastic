package com.thebois.models.beings.tasks;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskFactoryTests {

    @Test
    public void createMoveToThrowsWhenPathFinderNotSet() {
        // Assert
        assertThrows(NullPointerException.class, () -> TaskFactory.createMoveTo(new Position()));
    }

}
