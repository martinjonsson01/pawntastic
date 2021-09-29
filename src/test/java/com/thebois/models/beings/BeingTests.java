package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.models.IFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.beings.roles.RoleType;
import com.thebois.models.world.World;
import com.thebois.models.world.structures.House;
import com.thebois.models.world.structures.IStructure;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BeingTests {

    public static Stream<Arguments> getPositionsAndDestinations() {
        return Stream.of(Arguments.of(new Position(0, 0), new Position(0, 0)),
                         Arguments.of(new Position(0, 0), new Position(1, 1)),
                         Arguments.of(new Position(0, 0), new Position(1, 0)),
                         Arguments.of(new Position(0, 0), new Position(99, 99)),
                         Arguments.of(new Position(0, 0), new Position(-1, -1)),
                         Arguments.of(new Position(0, 0), new Position(-99, -99)),
                         Arguments.of(new Position(123, 456), new Position(0, 0)),
                         Arguments.of(new Position(456, 789), new Position(0, 0)));
    }

    public static Stream<Arguments> getEqualBeings() {
        final AbstractBeing beingA = createBeing(0, 0, 0, 0, RoleType.BUILDER);
        final AbstractBeing beingB = createBeing(0, 0, 0, 0, RoleType.BUILDER);
        final AbstractBeing beingC = createBeing(0, 0, 0, 0, RoleType.BUILDER);
        return Stream.of(
            Arguments.of(createBeing(0, 0, 0, 0, RoleType.FARMER),
                         createBeing(0, 0, 0, 0, RoleType.FARMER)),
            Arguments.of(createBeing(123, 456, 789, 1011, RoleType.FISHER),
                         createBeing(123, 456, 789, 1011, RoleType.FISHER)),
            /* Reflexiveness test */
            Arguments.of(beingA, beingA),
            /* Symmetry test */
            Arguments.of(beingA, beingB),
            Arguments.of(beingB, beingA),
            /* Transitivity test */
            Arguments.of(beingB, beingC),
            Arguments.of(beingA, beingC));
    }

    private static AbstractBeing createBeing(final float startX,
                                             final float startY,
                                             final float destinationX,
                                             final float destinationY,
                                             final RoleType roleType) {
        final Position currentPosition = new Position(startX, startY);
        final Position destination = new Position(destinationX, destinationY);
        final AbstractRole role = RoleFactory.fromType(roleType);
        final AbstractBeing being = new Pawn(currentPosition.deepClone(),
                                             destination.deepClone(),
                                             new Random(),
                                             new World(10, 0));
        being.setRole(role);
        return being;
    }

    public static Stream<Arguments> getNotEqualBeings() {
        return Stream.of(
            Arguments.of(createBeing(0, 0, 0, 0, RoleType.FARMER),
                         createBeing(0, 0, 0, 0, RoleType.FISHER)),
            Arguments.of(createBeing(0, 0, 0, 0, RoleType.LUMBERJACK),
                         createBeing(1, 0, 0, 0, RoleType.LUMBERJACK)),
            Arguments.of(createBeing(0, 0, 0, 0, RoleType.LUMBERJACK),
                         createBeing(0, 0, 1, 0, RoleType.LUMBERJACK)));
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void updateMovesTowardsDestination(final Position startPosition,
                                              final Position endPosition) {
        // Arrange
        final float distanceToDestination = startPosition.distanceTo(endPosition);
        final Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);
        final AbstractBeing being = new Pawn(
            startPosition,
            endPosition,
            mockRandom,
            new World(10, 0));

        // Act
        being.update();

        // Assert
        final float distanceAfterUpdate = being.getPosition().distanceTo(being.getDestination());
        assertThat(distanceAfterUpdate).isLessThanOrEqualTo(distanceToDestination);
    }

    @Test
    public void hashCodeReturnsSameIfEqual() {
        // Arrange
        final Position currentPosition = new Position(0, 0);
        final Position destination = new Position(1, 1);
        final AbstractRole role = RoleFactory.farmer();
        final IBeing first = new Pawn(currentPosition.deepClone(),
                                      destination.deepClone(),
                                      new Random(),
                                      new World(10, 0));
        first.setRole(role);
        final IBeing second = new Pawn(currentPosition.deepClone(),
                                       destination.deepClone(),
                                       new Random(),
                                       new World(10, 0));
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
        final IBeing first = new Pawn(
            new Position(0, 0),
            new Position(1, 1),
            new Random(),
            new World(10, 0));
        first.setRole(role);
        final IBeing second = new Pawn(new Position(123, 123),
                                       new Position(983, 1235),
                                       new Random(),
                                       new World(10, 0));

        // Act
        final int firstHashCode = first.hashCode();
        final int secondHashCode = second.hashCode();

        // Assert
        assertThat(firstHashCode).isNotEqualTo(secondHashCode);
    }

    @Test
    public void equalsReturnsFalseForOtherType() {
        // Arrange
        final IBeing being = new Pawn(
            new Position(0, 0),
            new Position(1, 1),
            new Random(),
            new World(10, 0));
        being.setRole(RoleFactory.farmer());

        // Assert
        // noinspection AssertBetweenInconvertibleTypes
        assertThat(being).isNotEqualTo(new Position());
    }

    @ParameterizedTest
    @MethodSource("getEqualBeings")
    public void equalsReturnsTrueForEqualBeings(final AbstractBeing first,
                                                final AbstractBeing second) {
        // Assert
        assertThat(first).isEqualTo(second);
    }

    @ParameterizedTest
    @MethodSource("getNotEqualBeings")
    public void equalsReturnsFalseForNotEqualBeings(final AbstractBeing first,
                                                    final AbstractBeing second) {
        // Assert
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void pawnBuildsHouses() {
        // Arrange

        final IFinder mockFinder = Mockito.mock(IFinder.class);
        final Position position = new Position();
        final IStructure structure = new House(position);
        when(mockFinder.findNearestStructure(any())).thenReturn(Optional.of(structure));
        final Pawn pawn = new Pawn(position, position, Mockito.mock(Random.class), mockFinder);

        // Act
        final float initialBuildStatus = structure.builtStatus();
        pawn.update();
        final float finalBuildStatus = structure.builtStatus();

        // Assert

        assertThat(initialBuildStatus).isLessThan(finalBuildStatus);
    }

    @Test
    public void pawnDoNotBuildsHouses() {
        // Arrange
        final IFinder mockFinder = Mockito.mock(IFinder.class);
        final Position position = new Position(0, 0);
        final Position farAway = new Position(50, 50);
        final IStructure structure = new House(farAway);
        when(mockFinder.findNearestStructure(any())).thenReturn(Optional.of(structure));
        final Pawn pawn = new Pawn(position, position, Mockito.mock(Random.class), mockFinder);

        // Act
        final float initialBuildStatus = structure.builtStatus();
        pawn.update();
        final float finalBuildStatus = structure.builtStatus();

        // Assert
        // Value did not change because Pawn was not close enough
        assertThat(initialBuildStatus).isEqualTo(finalBuildStatus);
    }

    @Test
    public void doesPawnWalkToNearestUnBuiltBuilding() {
        // Arrange
        final Position positionA = new Position(1, 0);
        final Position positionB = new Position(2, 0);
        final Position positionC = new Position(3, 0);

        final IStructure unBuiltStructure = Mockito.mock(IStructure.class);
        final IStructure builtStructure = Mockito.mock(IStructure.class);

        when(unBuiltStructure.getPosition()).thenReturn(positionA);
        when(builtStructure.getPosition()).thenReturn(positionC);
        when(unBuiltStructure.builtStatus()).thenReturn(0f);
        when(builtStructure.builtStatus()).thenReturn(1f);

        final Collection<Optional<IStructure>> structures = new ArrayList<>();
        structures.add(Optional.of(builtStructure));
        structures.add(Optional.of(unBuiltStructure));

        final IFinder mockFinder = Mockito.mock(IFinder.class);

        when(mockFinder.findNearestStructure(any(), anyInt())).thenReturn(structures);
        // builtStructure would be found first by the current finder
        when(mockFinder.findNearestStructure(any())).thenReturn(Optional.of(builtStructure));

        final Pawn pawn = new Pawn(positionB, positionB, Mockito.mock(Random.class), mockFinder);

        // Act
        pawn.update();

        // Assert
        // Value did not change because Pawn was not close enough
        assertThat(pawn.getDestination()).isEqualTo(positionA);
    }

}
