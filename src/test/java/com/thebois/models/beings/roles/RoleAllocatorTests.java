package com.thebois.models.beings.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.thebois.abstractions.IPositionFinder;
import com.thebois.abstractions.IResourceFinder;
import com.thebois.abstractions.IStructureFinder;
import com.thebois.models.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.Colony;
import com.thebois.models.beings.IBeing;
import com.thebois.models.beings.Pawn;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.world.IWorld;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleAllocatorTests {

    @Test
    public void tryIncreaseAllocationSucceedsWhenNoRolesAssigned() {
        // Arrange
        final RoleType role = RoleFactory.lumberjack().getType();
        final Colony cut = mockColonyWithBeings(3);

        // Act
        final boolean success = cut.tryIncreaseAllocation(role);

        // Assert
        assertThat(success).isTrue();
        assertThat(cut.getBeings()).anyMatch(being -> role.equals(being.getRole().getType()));
    }

//    private Colony mockColonyWithBeings(final int beingCount) {
//        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
//        final IStructureFinder structureFinder = Mockito.mock(IStructureFinder.class);
//        final IPositionFinder positionFinder = Mockito.mock(IPositionFinder.class);
//
//        final Colony colony = new Colony(pathFinder, structureFinder, positionFinder);
//
//        for (int i = 0; i < beingCount; i++) {
//            colony.addBeing(new Pawn(
//                new Position(),
//                new Position(),
//                new Random(),
//                pathFinder,
//                structureFinder));
//        }
//        return colony;
//    }

    private Colony mockColonyWithBeings(final int beingCount) {
        final List<Position> vacantPositions = new ArrayList<>();
        for (int i = 0; i < beingCount; i++) {
            vacantPositions.add(new Position(0, 0));
        }
        return new Colony(vacantPositions);
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
    public void tryIncreaseAllocationFailsWhenAllRolesAssigned() {
        // Arrange
        final RoleType existingRole = RoleFactory.lumberjack().getType();
        final RoleType newRole = RoleFactory.farmer().getType();
        final Colony cut = mockColonyWithBeings(3);
        cut.tryIncreaseAllocation(existingRole, 3);

        // Act
        final boolean success = cut.tryIncreaseAllocation(newRole);

        // Assert
        assertThat(success).isFalse();
        final Predicate<IBeing> hasNewRole = being -> newRole.equals(being.getRole().getType());
        assertThat(cut.getBeings()).noneMatch(hasNewRole);
    }

    @Test
    public void tryIncreaseAllocationSucceedsWhenOneIdle() {
        // Arrange
        final RoleType firstRole = RoleFactory.lumberjack().getType();
        final RoleType secondRole = RoleFactory.miner().getType();
        final RoleType newRole = RoleFactory.farmer().getType();

        final Colony cut = mockColonyWithBeings(7);

        cut.tryIncreaseAllocation(firstRole, 3);
        cut.tryIncreaseAllocation(secondRole, 3);

        // Act
        final boolean success = cut.tryIncreaseAllocation(newRole);

        // Assert
        assertThat(success).isTrue();
        assertThat(cut.getBeings())
            .filteredOn(being -> newRole.equals(being.getRole().getType()))
            .singleElement()
            .isNotNull();
    }

    @Test
    public void tryIncreaseAllocationAllocatesNoRolesIfNotEnoughIdleBeings() {
        // Arrange
        final RoleType role = RoleFactory.lumberjack().getType();

        final Colony cut = mockColonyWithBeings(4);

        cut.tryIncreaseAllocation(role, 3);

        // Act
        final boolean success = cut.tryIncreaseAllocation(role, 2);

        // Assert
        assertThat(success).isFalse();
        assertThat(cut.getBeings())
            .filteredOn(being -> role.equals(being.getRole().getType()))
            .hasSize(3);
    }

    @Test
    public void tryDecreaseAllocationFailsWhenNoRolesAssigned() {
        // Arrange
        final RoleType role = RoleFactory.lumberjack().getType();
        final Colony cut = mockColonyWithBeings(3);

        // Act
        final boolean success = cut.tryDecreaseAllocation(role);

        // Assert
        assertThat(success).isFalse();
        final Predicate<IBeing> hasRole = being -> role.equals(being.getRole().getType());
        assertThat(cut.getBeings()).noneMatch(hasRole);
    }

    @Test
    public void tryDecreaseAllocationFailsWhenNotEnoughRolesAssigned() {
        // Arrange
        final RoleType role = RoleFactory.lumberjack().getType();
        final Colony cut = mockColonyWithBeings(3);
        cut.tryIncreaseAllocation(role, 1);

        // Act
        final boolean success = cut.tryDecreaseAllocation(role, 2);

        // Assert
        assertThat(success).isFalse();
        final Predicate<IBeing> hasRole = being -> role.equals(being.getRole().getType());
        assertThat(cut.getBeings()).filteredOn(hasRole).hasSize(1);
    }

    @Test
    public void tryDecreaseAllocationSucceedsWhenAllRolesAssigned() {
        // Arrange
        final RoleType existingRole = RoleFactory.farmer().getType();
        final Colony cut = mockColonyWithBeings(3);
        cut.tryIncreaseAllocation(existingRole, 3);

        // Act
        final boolean success = cut.tryDecreaseAllocation(existingRole);

        // Assert
        assertThat(success).isTrue();
        final Predicate<IBeing> hasRole = being -> existingRole.equals(being.getRole().getType());
        assertThat(cut.getBeings()).filteredOn(hasRole).hasSize(2);
    }

    @Test
    public void tryDecreaseAllocationSucceedsWhenEnoughRolesAssigned() {
        // Arrange
        final RoleType existingRole = RoleFactory.farmer().getType();
        final Colony cut = mockColonyWithBeings(3);
        cut.tryIncreaseAllocation(existingRole, 3);

        // Act
        final boolean success = cut.tryDecreaseAllocation(existingRole, 2);

        // Assert
        assertThat(success).isTrue();
        final Predicate<IBeing> hasRole = being -> existingRole.equals(being.getRole().getType());
        assertThat(cut.getBeings()).filteredOn(hasRole).hasSize(1);
    }

    @Test
    public void tryDecreaseAllocationSucceedsWhenOneHasRole() {
        // Arrange
        final RoleType role = RoleFactory.lumberjack().getType();
        final Colony cut = mockColonyWithBeings(4);
        cut.tryIncreaseAllocation(role);

        // Act
        final boolean success = cut.tryDecreaseAllocation(role);

        // Assert
        assertThat(success).isTrue();
        final Predicate<IBeing> isIdle = being -> being.getRole().getType().equals(RoleType.IDLE);
        assertThat(cut.getBeings()).allMatch(isIdle);
    }

    @Test
    public void countBeingsWithRoleReturnsCorrectCountWhenThereAreMultipleRoles() {
        // Arrange
        final RoleType firstRole = RoleFactory.lumberjack().getType();
        final RoleType secondRole = RoleFactory.miner().getType();
        final RoleType thirdRole = RoleFactory.farmer().getType();

        final Colony cut = mockColonyWithBeings(16);

        final int expectedFirstCount = 3;
        final int expectedSecondCount = 5;
        final int expectedThirdCount = 8;
        cut.tryIncreaseAllocation(firstRole, expectedFirstCount);
        cut.tryIncreaseAllocation(secondRole, expectedSecondCount);
        cut.tryIncreaseAllocation(thirdRole, expectedThirdCount);

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

        final Colony cut = mockColonyWithBeings(3);
        cut.tryIncreaseAllocation(role);

        // Act
        final boolean canDecrease = cut.canDecreaseAllocation(role);

        // Assert
        assertThat(canDecrease).isTrue();
    }

    @Test
    public void canDecreaseAllocationReturnsFalseWhenNoneHaveRole() {
        // Arrange
        final RoleType role = RoleFactory.lumberjack().getType();

        final Colony cut = mockColonyWithBeings(3);

        // Act
        final boolean canDecrease = cut.canDecreaseAllocation(role);

        // Assert
        assertThat(canDecrease).isFalse();
    }

    @Test
    public void canIncreaseAllocationReturnsTrueWhenNoneHaveRole() {
        // Arrange
        final Colony cut = mockColonyWithBeings(3);

        // Act
        final boolean canIncrease = cut.canIncreaseAllocation();

        // Assert
        assertThat(canIncrease).isTrue();
    }

    @Test
    public void canIncreaseAllocationReturnsFalseWhenAllHaveRole() {
        // Arrange
        final RoleType role = RoleFactory.lumberjack().getType();

        final Colony cut = mockColonyWithBeings(3);
        cut.tryIncreaseAllocation(role, 3);

        // Act
        final boolean canIncrease = cut.canIncreaseAllocation();

        // Assert
        assertThat(canIncrease).isFalse();
    }

}
