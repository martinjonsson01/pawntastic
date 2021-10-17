package com.thebois.models.world.structures;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class StructureFactoryTests {

    public static Stream<Arguments> getStructureTypeAndExpectedStructure() {
        return Stream.of(Arguments.of(StructureType.HOUSE, House.class));
    }

    @ParameterizedTest
    @MethodSource("getStructureTypeAndExpectedStructure")
    public void factoryCreatesCorrectStructureWithGivenEnum(
        final StructureType type, final Class<IStructure> expectedStructure) {
        // Arrange
        final int x = 0;
        final int y = 0;

        // Act
        final IStructure actualStructure = StructureFactory.createStructure(type, x, y);

        // Assert
        assertThat(actualStructure.getClass()).isEqualTo(expectedStructure);
    }

}
