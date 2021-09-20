package com.thebois.models.beings.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import com.thebois.models.beings.IBeing;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RoleAllocatorTests {

    @Test
    public void tryIncreaseAllocationSucceedsWhenNoRolesAssigned() {
        // Arrange
        final RoleType role = RoleFactory.lumberjack().getType();
        final List<IBeing> beings = mockBeings(3);
        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean success = cut.tryIncreaseAllocation(role);

        // Assert
        assertThat(success).isTrue();
        assertThat(beings).anyMatch(being -> role.equals(being.getRole().getType()));
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
        doAnswer(returnArgumentAsGetRole(being)).when(being).setRole(any());
        return being;
    }

    @SuppressWarnings("rawtypes")
    private Answer returnArgumentAsGetRole(final IBeing being) {
        return answer -> when(being.getRole()).thenReturn((AbstractRole) answer.getArguments()[0]);
    }

    @Test
    public void tryIncreaseAllocationFailsWhenAllRolesAssigned() {
        // Arrange
        final RoleType newRole = RoleFactory.lumberjack().getType();
        final RoleType existingRole = RoleFactory.farmer().getType();
        final List<IBeing> beingsWithRoles = mockBeingsWithRole(3, existingRole);
        final IRoleAllocator cut = new RoleAllocator(beingsWithRoles);

        // Act
        final boolean success = cut.tryIncreaseAllocation(newRole);

        // Assert
        assertThat(success).isFalse();
        assertThat(beingsWithRoles).noneMatch(being -> newRole.equals(being.getRole().getType()));
    }

    private List<IBeing> mockBeingsWithRole(final int beingCount, final RoleType roleType) {
        final List<IBeing> beings = mockBeings(beingCount);
        final AbstractRole role = RoleFactory.fromType(roleType);
        beings.forEach(being -> being.setRole(role));
        return beings;
    }

    @Test
    public void tryIncreaseAllocationSucceedsWhenOneIdle() {
        // Arrange
        final RoleType firstRole = RoleFactory.lumberjack().getType();
        final RoleType secondRole = RoleFactory.miner().getType();
        final RoleType newRole = RoleFactory.farmer().getType();

        final List<IBeing> beings = mockBeingsWithRole(3, firstRole);
        beings.add(mockBeing());
        beings.addAll(mockBeingsWithRole(3, secondRole));

        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean success = cut.tryIncreaseAllocation(newRole);

        // Assert
        assertThat(success).isTrue();
        assertThat(beings).filteredOn(being -> newRole.equals(being.getRole().getType()))
                          .singleElement()
                          .isNotNull();
    }

    @Test
    public void tryDecreaseAllocationFailsWhenNoRolesAssigned() {
        // Arrange
        final RoleType role = RoleFactory.lumberjack().getType();
        final List<IBeing> beings = mockBeings(3);
        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean success = cut.tryDecreaseAllocation(role);

        // Assert
        assertThat(success).isFalse();
        final Predicate<IBeing> hasRole =
            being -> being.getRole() != null && role.equals(being.getRole().getType());
        assertThat(beings).noneMatch(hasRole);
    }

    @Test
    public void tryDecreaseAllocationSucceedsWhenAllRolesAssigned() {
        // Arrange
        final RoleType existingRole = RoleFactory.farmer().getType();
        final List<IBeing> beingsWithRoles = mockBeingsWithRole(3, existingRole);
        final IRoleAllocator cut = new RoleAllocator(beingsWithRoles);

        // Act
        final boolean success = cut.tryDecreaseAllocation(existingRole);

        // Assert
        assertThat(success).isTrue();
        final Predicate<IBeing> hasRole = being -> being.getRole() != null && existingRole.equals(
            being.getRole().getType());
        assertThat(beingsWithRoles).filteredOn(hasRole).hasSize(beingsWithRoles.size() - 1);
    }

    @Test
    public void tryDecreaseAllocationSucceedsWhenOneHasRole() {
        // Arrange
        final RoleType role = RoleFactory.lumberjack().getType();

        final List<IBeing> beings = mockBeings(3);
        beings.addAll(mockBeingsWithRole(1, role));

        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean success = cut.tryDecreaseAllocation(role);

        // Assert
        assertThat(success).isTrue();
        final Predicate<IBeing> hasRole =
            being -> being.getRole() != null && role.equals(being.getRole().getType());
        assertThat(beings).noneMatch(hasRole);
    }

    @Test
    public void countBeingsWithRoleReturnsCorrectCountWhenThereAreMultipleRoles() {
        // Arrange
        final RoleType firstRole = RoleFactory.lumberjack().getType();
        final RoleType secondRole = RoleFactory.miner().getType();
        final RoleType thirdRole = RoleFactory.farmer().getType();

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
        final RoleType role = RoleFactory.lumberjack().getType();

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
        final RoleType role = RoleFactory.lumberjack().getType();

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
        final RoleType role = RoleFactory.lumberjack().getType();

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
        final RoleType role = RoleFactory.lumberjack().getType();

        final List<IBeing> beings = mockBeingsWithRole(3, role);

        final IRoleAllocator cut = new RoleAllocator(beings);

        // Act
        final boolean canIncrease = cut.canIncreaseAllocation(role);

        // Assert
        assertThat(canIncrease).isFalse();
    }

}
