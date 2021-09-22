package com.thebois.models.beings;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.RoleFactory;

import static org.assertj.core.api.Assertions.*;

public class BeingTests {

    @Test
    public void deepCloneCreatesCloneThatIsEqual() {
        // Arrange
        final Position originalPosition = new Position(123, 456);
        final Position originalDestination = new Position(-444, -777);
        final IBeing original = new Pawn(originalPosition, originalDestination);
        original.setRole(RoleFactory.lumberjack());

        // Act
        final IBeing deepClone = original.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(original);
    }

    @Test
    public void equalsReturnsTrueForSameInstance() {
        // Arrange
        final IBeing being = new Pawn(new Position(0, 0), new Position(1, 1));
        being.setRole(RoleFactory.farmer());

        // Assert
        assertThat(being).isEqualTo(being);
    }

    @Test
    public void equalsReturnsFalseForOtherType() {
        // Arrange
        final IBeing being = new Pawn(new Position(0, 0), new Position(1, 1));
        being.setRole(RoleFactory.farmer());

        // Assert
        // noinspection AssertBetweenInconvertibleTypes
        assertThat(being).isNotEqualTo(new Position());
    }

    @Test
    public void equalsIsSymmetric() {
        // Arrange
        final Position currentPosition = new Position(0, 0);
        final Position destination = new Position(1, 1);
        final AbstractRole role = RoleFactory.farmer();
        final IBeing first = new Pawn(currentPosition.deepClone(), destination.deepClone());
        first.setRole(role);
        final IBeing second = new Pawn(currentPosition.deepClone(), destination.deepClone());
        second.setRole(role);

        // Assert
        assertThat(first).isNotSameAs(second);
        assertThat(first).isEqualTo(second);
        assertThat(second).isEqualTo(first);
    }

    @Test
    public void equalsIsTransitive() {
        // Arrange
        final Position currentPosition = new Position(0, 0);
        final Position destination = new Position(1, 1);
        final AbstractRole role = RoleFactory.farmer();
        final IBeing first = new Pawn(currentPosition.deepClone(), destination.deepClone());
        first.setRole(role);
        final IBeing second = new Pawn(currentPosition.deepClone(), destination.deepClone());
        second.setRole(role);
        final IBeing third = new Pawn(currentPosition.deepClone(), destination.deepClone());
        third.setRole(role);

        // Assert
        assertThat(first).isEqualTo(second);
        assertThat(second).isEqualTo(third);
        assertThat(first).isEqualTo(third);
    }

}
