package com.thebois.models.beings.roles;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

public class RoleTests {

    public static Stream<Arguments> getEqualRoles() {
        final LumberjackRole sameLumberjack = new LumberjackRole();
        return Stream.of(
            Arguments.of(sameLumberjack, sameLumberjack),
            Arguments.of(new LumberjackRole(), new LumberjackRole()),
            Arguments.of(new FarmerRole(), new FarmerRole()),
            Arguments.of(new GuardRole(), new GuardRole()),
            Arguments.of(new MinerRole(), new MinerRole()),
            Arguments.of(new FisherRole(), new FisherRole()),
            Arguments.of(new BuilderRole(), new BuilderRole()),
            Arguments.of(new IdleRole(), new IdleRole()));
    }

    public static Stream<Arguments> getUnequalRoles() {
        return Stream.of(
            Arguments.of(new LumberjackRole(), new FarmerRole()),
            Arguments.of(new FarmerRole(), new LumberjackRole()),
            Arguments.of(new GuardRole(), new MinerRole()),
            Arguments.of(new MinerRole(), new GuardRole()),
            Arguments.of(new FisherRole(), new BuilderRole()),
            Arguments.of(new BuilderRole(), new FisherRole()),
            Arguments.of(new BuilderRole(), null));
    }

    public static Stream<Arguments> getRoleAndNames() {
        return Stream.of(
            Arguments.of(new LumberjackRole(), "Lumberjack"),
            Arguments.of(new FarmerRole(), "Farmer"),
            Arguments.of(new GuardRole(), "Guard"),
            Arguments.of(new MinerRole(), "Miner"));
    }

    @ParameterizedTest
    @MethodSource("getRoleAndNames")
    public void getNameIsExpectedName(final AbstractRole role, final String expectedName) {
        // Act
        final String actualName = role.getName();

        // Assert
        assertThat(actualName).isEqualTo(expectedName);
    }

    @ParameterizedTest
    @MethodSource("getEqualRoles")
    public void equalsReturnsTrueWhenEqual(final AbstractRole first, final AbstractRole second) {
        // Assert
        assertThat(first).isEqualTo(second);
    }

    @ParameterizedTest
    @MethodSource("getUnequalRoles")
    public void equalsReturnsFalseWhenUnequal(final AbstractRole first, final AbstractRole second) {
        // Assert
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void deepCloneCreatesCloneThatIsEqual() {
        // Arrange
        final AbstractRole original = RoleFactory.lumberjack();

        // Act
        final AbstractRole deepClone = original.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(original);
    }

}
