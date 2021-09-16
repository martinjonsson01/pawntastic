package com.thebois.models.beings.roles;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.thebois.models.beings.IBeing;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RoleAllocatorTests {

    @Test
    public void allocateAssignsRoleToBeings() {
        // Arrange
        final RoleAllocator allocator = new RoleAllocator();
        final ArrayList<IBeing> beings = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            final IBeing being = Mockito.mock(IBeing.class);
            // Mocks the getter and setter to simulate a real implementation.
            doAnswer(answer -> when(being.getRole()).thenReturn((Role) answer.getArguments()[0]))
                .when(being)
                .setRole(any());
            beings.add(being);
        }

        // Act
        allocator.allocate(beings);

        // Assert
        assertThat(beings).allMatch(being -> being.getRole() != null);
    }

}
