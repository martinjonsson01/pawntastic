package com.thebois.models.inventory.items;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

public class ItemFactoryTests {

    public static Stream<Arguments> getItemTypes() {
        return Arrays.stream(ItemType.values()).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("getItemTypes")
    public void fromTypeReturnsItemWithCorrectType(final ItemType expected) {
        // Act
        final IItem item = ItemFactory.fromType(expected);

        // Assert
        assertThat(item.getType()).isEqualTo(expected);
    }

}
