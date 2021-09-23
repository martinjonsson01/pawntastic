package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

public class RoleFactoryTests {

    public static Stream<Arguments> getRoleTypes() {
        return Stream.of(
            Arguments.of(RoleType.BUILDER, new BuilderRole()),
            Arguments.of(RoleType.FARMER, new FarmerRole()),
            Arguments.of(RoleType.FISHER, new FisherRole()),
            Arguments.of(RoleType.GUARD, new GuardRole()),
            Arguments.of(RoleType.LUMBERJACK, new LumberjackRole()),
            Arguments.of(RoleType.MINER, new MinerRole()),
            Arguments.of(RoleType.IDLE, new IdleRole()));
    }

    @Test
    public void factoryGetAllRolesReturnsAllRoles() {
        // Act
        final Collection<AbstractRole> roles = RoleFactory.all();

        // Assert
        assertThat(roles).contains(
            new LumberjackRole(),
            new BuilderRole(),
            new FarmerRole(),
            new FisherRole(),
            new MinerRole(),
            new GuardRole(),
            new IdleRole());
    }

    @ParameterizedTest
    @MethodSource("getRoleTypes")
    public void fromTypeReturnsCorrectRole(final RoleType roleType,
                                           final AbstractRole expectedRole) {
        // Act
        final AbstractRole actualRole = RoleFactory.fromType(roleType);

        // Assert
        assertThat(actualRole).isEqualTo(expectedRole);
    }

}
