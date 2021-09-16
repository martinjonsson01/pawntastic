package com.thebois.models.beings.roles;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.thebois.models.beings.IBeing;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RoleAllocatorTests {

    @Test
    public void tryIncreaseAllocationSucceedsWhenNoRolesAssigned() {
        // Arrange
        final Role role = RoleFactory.lumberjack();
        final List<IBeing> beings = mockBeings(3);
        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean success = cut.tryIncreaseAllocation(role);

        // Assert
        assertThat(success).isTrue();
        assertThat(beings).anyMatch(being -> role.equals(being.getRole()));
    }

    private List<IBeing> mockBeings(final int beingCount) {
        final ArrayList<IBeing> beings = new ArrayList<>(beingCount);
        for (int i = 0; i < beingCount; i++) {
            beings.add(mockBeing());
        }
        return beings;
    }

    private IBeing mockBeing() {
        final IBeing being = Mockito.mock(IBeing.class);
        // Mocks the getter and setter to simulate a real implementation.
        doAnswer(answer -> when(being.getRole()).thenReturn((Role) answer.getArguments()[0])).when(
            being).setRole(any());
        return being;
    }

    @Test
    public void tryIncreaseAllocationFailsWhenAllRolesAssigned() {
        // Arrange
        final Role newRole = RoleFactory.builder();
        final Role existingRole = RoleFactory.farmer();
        final List<IBeing> beingsWithRoles = mockBeingsWithRole(3, existingRole);
        final IRoleAllocator cut = new RoleAllocator(beingsWithRoles);

        // Act
        final boolean success = cut.tryIncreaseAllocation(newRole);

        // Assert
        assertThat(success).isFalse();
        assertThat(beingsWithRoles).noneMatch(being -> newRole.equals(being.getRole()));
    }

    private List<IBeing> mockBeingsWithRole(final int beingCount, final Role role) {
        final List<IBeing> beings = mockBeings(beingCount);
        beings.forEach(being -> being.setRole(role));
        return beings;
    }

    @Test
    public void tryIncreaseAllocationSucceedsWhenOneIdle() {
        // Arrange
        final Role firstRole = RoleFactory.lumberjack();
        final Role secondRole = RoleFactory.miner();
        final Role newRole = RoleFactory.farmer();

        final List<IBeing> beings = mockBeingsWithRole(3, firstRole);
        beings.add(mockBeing());
        beings.addAll(mockBeingsWithRole(3, secondRole));

        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean success = cut.tryIncreaseAllocation(newRole);

        // Assert
        assertThat(success).isTrue();
        assertThat(beings)
            .filteredOn(being -> newRole.equals(being.getRole()))
            .singleElement()
            .isNotNull();
    }

    @Test
    public void tryDecreaseAllocationFailsWhenNoRolesAssigned() {
        // Arrange
        final Role role = RoleFactory.lumberjack();
        final List<IBeing> beings = mockBeings(3);
        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean success = cut.tryDecreaseAllocation(role);

        // Assert
        assertThat(success).isFalse();
        assertThat(beings).noneMatch(being -> role.equals(being.getRole()));
    }

    @Test
    public void tryDecreaseAllocationSucceedsWhenAllRolesAssigned() {
        // Arrange
        final Role existingRole = RoleFactory.farmer();
        final List<IBeing> beingsWithRoles = mockBeingsWithRole(3, existingRole);
        final IRoleAllocator cut = new RoleAllocator(beingsWithRoles);

        // Act
        final boolean success = cut.tryDecreaseAllocation(existingRole);

        // Assert
        assertThat(success).isTrue();
        assertThat(beingsWithRoles)
            .filteredOn(being -> existingRole.equals(being.getRole()))
            .hasSize(beingsWithRoles.size() - 1);
    }

    @Test
    public void tryDecreaseAllocationSucceedsWhenOneHasRole() {
        // Arrange
        final Role role = RoleFactory.lumberjack();

        final List<IBeing> beings = mockBeings(3);
        beings.addAll(mockBeingsWithRole(1, role));

        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean success = cut.tryDecreaseAllocation(role);

        // Assert
        assertThat(success).isTrue();
        assertThat(beings).noneMatch(being -> role.equals(being.getRole()));
    }

    @Test
    public void countBeingsWithRoleReturnsCorrectCountWhenThereAreMultipleRoles() {
        // Arrange
        final Role firstRole = RoleFactory.lumberjack();
        final Role secondRole = RoleFactory.miner();
        final Role thirdRole = RoleFactory.farmer();

        final int expectedFirstCount = 3;
        final int expectedSecondCount = 5;
        final int expectedThirdCount = 8;
        final List<IBeing> beings = new ArrayList<>();
        beings.addAll(mockBeingsWithRole(expectedFirstCount, firstRole));
        beings.addAll(mockBeingsWithRole(expectedSecondCount, secondRole));
        beings.addAll(mockBeingsWithRole(expectedThirdCount, thirdRole));

        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final int firstCount = cut.countBeingsWithRole(firstRole);
        final int secondCount = cut.countBeingsWithRole(secondRole);
        final int thirdCount = cut.countBeingsWithRole(thirdRole);

        // Assert
        assertThat(firstCount).isEqualTo(expectedFirstCount);
        assertThat(secondCount).isEqualTo(expectedSecondCount);
        assertThat(thirdCount).isEqualTo(expectedThirdCount);
    }

    @Test
    public void canDecreaseAllocationReturnsTrueWhenOneHasRole() {
        // Arrange
        final Role role = RoleFactory.lumberjack();

        final List<IBeing> beings = mockBeings(3);
        beings.addAll(mockBeingsWithRole(1, role));

        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean canDecrease = cut.canDecreaseAllocation(role);

        // Assert
        assertThat(canDecrease).isTrue();
    }

    @Test
    public void canDecreaseAllocationReturnsFalseWhenNoneHaveRole() {
        // Arrange
        final Role role = RoleFactory.lumberjack();

        final List<IBeing> beings = mockBeings(3);

        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean canDecrease = cut.canDecreaseAllocation(role);

        // Assert
        assertThat(canDecrease).isFalse();
    }

    @Test
    public void canIncreaseAllocationReturnsTrueWhenNoneHaveRole() {
        // Arrange
        final Role role = RoleFactory.lumberjack();

        final List<IBeing> beings = mockBeings(3);

        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean canIncrease = cut.canIncreaseAllocation(role);

        // Assert
        assertThat(canIncrease).isTrue();
    }

    @Test
    public void canIncreaseAllocationReturnsFalseWhenAllHaveRole() {
        // Arrange
        final Role role = RoleFactory.lumberjack();

        final List<IBeing> beings = mockBeingsWithRole(3, role);

        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean canIncrease = cut.canIncreaseAllocation(role);

        // Assert
        assertThat(canIncrease).isFalse();
    }

}
