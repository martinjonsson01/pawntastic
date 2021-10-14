package com.thebois.models.world.resources;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

public class ResourceFactoryTests {

    public static Stream<Arguments> getResourceTypeAndExpectedResource() {
        return Stream.of(
            Arguments.of(ResourceType.ROCK, new Rock(0, 0)),
            Arguments.of(ResourceType.WATER, new Water(0, 0)),
            Arguments.of(ResourceType.TREE, new Tree(0, 0)));
    }

    @ParameterizedTest
    @MethodSource("getResourceTypeAndExpectedResource")
    public void factoryCreatesCorrectResourceWithGivenEnum(
        final ResourceType type, final IResource expectedResource) {
        // Arrange
        final int x = 0;
        final int y = 0;

        // Act
        final IResource actualResource = ResourceFactory.createResource(type, x, y);

        // Assert
        assertThat(actualResource).isEqualTo(expectedResource);
    }

    @Test
    public void createResourceWithNullAsEnumThrowsUnsupportedOperationException() {
        // Arrange
        final int x = 0;
        final int y = 0;

        // Assert
        Assertions.assertThrows(NullPointerException.class, () -> {
            ResourceFactory.createResource(null, x, y);
        });
    }

}
