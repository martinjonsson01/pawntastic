package com.thebois.models.inventory.items;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

public class ItemTests {

    public static Stream<Arguments> getItemTypesAndItemClass() {
        return Stream.of(Arguments.of(ItemType.LOG, ItemFactory.fromType(ItemType.LOG)),
                         Arguments.of(ItemType.ROCK, ItemFactory.fromType(ItemType.ROCK)));
    }

    @ParameterizedTest
    @MethodSource("getItemTypesAndItemClass")
    public void getTypeReturnsCorrectItemType(final ItemType itemType, final IItem item) {
        // Assert
        assertThat(itemType).isEqualTo(item.getType());
    }

    public static Stream<Arguments> getItemTypesToTestIfEqualAndExpectedResult() {
        return Stream.of(
            Arguments.of(ItemFactory.fromType(ItemType.LOG), ItemFactory.fromType(ItemType.LOG), true),
            Arguments.of(ItemFactory.fromType(ItemType.LOG), ItemFactory.fromType(ItemType.ROCK), false),
            Arguments.of(ItemFactory.fromType(ItemType.ROCK), ItemFactory.fromType(ItemType.ROCK), true),
            Arguments.of(ItemFactory.fromType(ItemType.ROCK), null, false));
    }

    @ParameterizedTest
    @MethodSource("getItemTypesToTestIfEqualAndExpectedResult")
    public void isEqual(final IItem itemA, final IItem itemB, final boolean expectedResult) {

        // Assert
        assertThat(itemA.equals(itemB)).isEqualTo(expectedResult);
    }

}
