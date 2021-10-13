package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.world.IWorld;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleFactoryTests {

    public static Stream<Arguments> getRoleTypes() {
        return Stream.of(
            Arguments.of(RoleType.BUILDER, new BuilderRole()),
            Arguments.of(RoleType.FARMER, new FarmerRole()),
            Arguments.of(RoleType.FISHER, new FisherRole()),
            Arguments.of(RoleType.GUARD, new GuardRole()),
            Arguments.of(RoleType.LUMBERJACK, new LumberjackRole(mock(IResourceFinder.class))),
            Arguments.of(RoleType.MINER, new MinerRole()),
            Arguments.of(RoleType.IDLE, new IdleRole(mock(IWorld.class))));
    }

    @BeforeEach
    public void setup() {
        RoleFactory.setWorld(mock(IWorld.class));
        RoleFactory.setResourceFinder(mock(IResourceFinder.class));
    }

    @AfterEach
    public void teardown() {
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
    }

    @Test
    public void factoryGetAllRolesReturnsAllRoles() {
        // Act
        final Collection<AbstractRole> roles = RoleFactory.all();

        // Assert
        assertThat(roles).contains(
            new LumberjackRole(mock(IResourceFinder.class)),
            new BuilderRole(),
            new FarmerRole(),
            new FisherRole(),
            new MinerRole(),
            new GuardRole(),
            new IdleRole(mock(IWorld.class)));
    }

    @ParameterizedTest
    @MethodSource("getRoleTypes")
    public void fromTypeReturnsCorrectRole(final RoleType type, final AbstractRole expectedRole) {
        // Act
        final AbstractRole actualRole = RoleFactory.fromType(type);

        // Assert
        assertThat(actualRole).isEqualTo(expectedRole);
    }

}
