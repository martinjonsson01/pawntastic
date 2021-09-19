package com.thebois.models.beings.roles;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class RoleFactoryTests {

    @Test
    public void factoryGetAllRolesReturnsAllRoles() {
        // Act
        final Collection<AbstractRole> roles = RoleFactory.all();

        // Assert
        assertThat(roles).contains(new LumberjackRole(), new BuilderRole(), new FarmerRole(),
                                   new FisherRole(), new MinerRole(), new GuardRole());
    }

}
