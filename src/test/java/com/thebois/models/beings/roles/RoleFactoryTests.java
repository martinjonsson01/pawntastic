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
import com.thebois.abstractions.IStructureFinder;
import com.thebois.models.world.IWorld;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleFactoryTests {

    public static Stream<Arguments> getRoleTypes() {
        return Stream.of(Arguments.of(RoleType.BUILDER,
                                      new BuilderRole(mock(IStructureFinder.class),
                                                      mock(IWorld.class))),
                         Arguments.of(RoleType.FISHER,
                                      new FisherRole(mock(IResourceFinder.class),
                                                     mock(IStructureFinder.class),
                                                     mock(IWorld.class))),
                         Arguments.of(RoleType.LUMBERJACK,
                                      new LumberjackRole(mock(IResourceFinder.class),
                                                         mock(IStructureFinder.class),
                                                         mock(IWorld.class))),
                         Arguments.of(RoleType.MINER,
                                      new MinerRole(mock(IResourceFinder.class),
                                                    mock(IStructureFinder.class),
                                                    mock(IWorld.class))),
                         Arguments.of(RoleType.IDLE,
                                      new IdleRole(mock(IWorld.class))));
    }

    @BeforeEach
    public void setup() {
        RoleFactory.setWorld(mock(IWorld.class));
        RoleFactory.setResourceFinder(mock(IResourceFinder.class));
        RoleFactory.setStructureFinder(mock(IStructureFinder.class));
    }

    @AfterEach
    public void teardown() {
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
        RoleFactory.setStructureFinder(null);
    }

    @Test
    public void factoryGetAllRolesReturnsAllRoles() {
        // Act
        final Collection<AbstractRole> roles = RoleFactory.all();

        // Assert
        assertThat(roles).contains(new LumberjackRole(mock(IResourceFinder.class),
                                                      mock(IStructureFinder.class),
                                                      mock(IWorld.class)),
                                   new BuilderRole(mock(IStructureFinder.class),
                                                   mock(IWorld.class)),
                                   new FisherRole(mock(IResourceFinder.class),
                                                  mock(IStructureFinder.class),
                                                  mock(IWorld.class)),

                                   new MinerRole(mock(IResourceFinder.class),
                                                 mock(IStructureFinder.class),
                                                 mock(IWorld.class)),
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
