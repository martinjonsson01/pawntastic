package com.thebois.models.inventory.items;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

public class ItemTests {

    public static Stream<Arguments> getItemTypes() {
        return Stream.of(
            Arguments.of(ItemType.LOG, new Log()),
            Arguments.of(ItemType.ROCK, new Rock()));
    }

    @ParameterizedTest
    @MethodSource("getItemTypes")
    public void fromTypeReturnsCorrectItemType(final ItemType itemType,
                                           final IItem expectedType) {
        // Assert
        assertThat(itemType).isEqualTo(expectedType.getType());
    }

}
