package com.thebois.models.beings;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.beings.roles.RoleType;

import static org.assertj.core.api.Assertions.*;

public class BeingTests {

    public static Stream<Arguments> getEqualBeings() {
        final AbstractBeing beingA = createBeing(0, 0, RoleType.BUILDER);
        final AbstractBeing beingB = createBeing(0, 0, RoleType.BUILDER);
        final AbstractBeing beingC = createBeing(0, 0, RoleType.BUILDER);
        return Stream.of(Arguments.of(createBeing(0, 0, RoleType.FARMER),
                                      createBeing(0, 0, RoleType.FARMER)),
                         Arguments.of(createBeing(123, 456, RoleType.FISHER),
                                      createBeing(123, 456, RoleType.FISHER)),
                         Arguments.of(beingA, beingA),
                         Arguments.of(beingA, beingB),
                         Arguments.of(beingB, beingA),
                         Arguments.of(beingB, beingC),
                         Arguments.of(beingA, beingC));
    }

    private static AbstractBeing createBeing(
        final float startX, final float startY, final RoleType roleType) {
        final Position currentPosition = new Position(startX, startY);
        final AbstractRole role = RoleFactory.fromType(roleType);
        final AbstractBeing being = new Pawn(currentPosition.deepClone());
        being.setRole(role);
        return being;
    }

    public static Stream<Arguments> getNotEqualBeings() {
        return Stream.of(Arguments.of(createBeing(0, 0, RoleType.FARMER),
                                      createBeing(0, 0, RoleType.FISHER)),
                         Arguments.of(createBeing(0, 0, RoleType.LUMBERJACK),
                                      createBeing(1, 0, RoleType.LUMBERJACK)));
    }

    @Test
    public void setRoleWithNullThrowsException() {
        // Arrange
        final IBeing being = new Pawn(new Position());

        // Assert
        assertThatThrownBy(() -> being.setRole(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void hashCodeReturnsSameIfEqual() {
        // Arrange
        final Position currentPosition = new Position(0, 0);
        final AbstractRole role = RoleFactory.farmer();
        final IBeing first = new Pawn(currentPosition.deepClone());
        first.setRole(role);
        final IBeing second = new Pawn(currentPosition.deepClone());
        second.setRole(role);

        // Act
        final int firstHashCode = first.hashCode();
        final int secondHashCode = second.hashCode();

        // Assert
        assertThat(firstHashCode).isEqualTo(secondHashCode);
    }

    @Test
    public void hashCodeReturnDifferentIfNotEqual() {
        // Arrange
        final AbstractRole role = RoleFactory.farmer();
        final IBeing first = new Pawn(new Position(0, 0));
        first.setRole(role);
        final IBeing second = new Pawn(new Position(123, 123));

        // Act
        final int firstHashCode = first.hashCode();
        final int secondHashCode = second.hashCode();

        // Assert
        assertThat(firstHashCode).isNotEqualTo(secondHashCode);
    }

    @Test
    public void equalsReturnsFalseForOtherType() {
        // Arrange
        final IBeing being = new Pawn(new Position(0, 0));
        being.setRole(RoleFactory.farmer());

        // Assert
        // noinspection AssertBetweenInconvertibleTypes
        assertThat(being).isNotEqualTo(new Position());
    }

    @ParameterizedTest
    @MethodSource("getEqualBeings")
    public void equalsReturnsTrueForEqualBeings(
        final AbstractBeing first, final AbstractBeing second) {
        // Assert
        assertThat(first).isEqualTo(second);
    }

    @ParameterizedTest
    @MethodSource("getNotEqualBeings")
    public void equalsReturnsFalseForNotEqualBeings(
        final AbstractBeing first, final AbstractBeing second) {
        // Assert
        assertThat(first).isNotEqualTo(second);
    }

}
