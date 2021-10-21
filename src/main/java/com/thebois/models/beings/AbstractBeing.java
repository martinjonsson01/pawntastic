package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.IStoreable;
import com.thebois.models.inventory.ITakeable;
import com.thebois.models.inventory.Inventory;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * An independent agent that can act in the world according to its assigned role.
 */
public abstract class AbstractBeing implements IBeing, IActionPerformer {

    /**
     * The max speed of the being, in tiles/second.
     */
    private static final float SPEED = 6f;
    /**
     * The distance at which the being stops moving towards a destination and considers itself
     * arrived.
     */
    private static final float DESTINATION_REACHED_DISTANCE = 0.01f;
    /**
     * How many kilograms a being can carry.
     */
    private static final float MAX_CARRYING_CAPACITY = 100f;
    private final IInventory inventory = new Inventory(MAX_CARRYING_CAPACITY);
    private Position position;
    private AbstractRole role;
    private Position destination;

    /**
     * Instantiates with an initial position and role.
     *
     * @param startPosition The initial position.
     * @param role          The starting role.
     */
    public AbstractBeing(
        final Position startPosition, final AbstractRole role) {
        this.position = startPosition;
        this.destination = startPosition;
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition(), getRole());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof AbstractBeing)) return false;
        final AbstractBeing that = (AbstractBeing) other;
        return Objects.equals(getPosition(), that.getPosition()) && Objects.equals(getRole(),
                                                                                   that.getRole());
    }

    @Override
    public Position getPosition() {
        return position.deepClone();
    }

    @Override
    public AbstractRole getRole() {
        return role.deepClone();
    }

    @Override
    public void setRole(final AbstractRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role can not be null. Use IdleRole instead.");
        }
        this.role = role;
    }

    @Override
    public void update(final float deltaTime) {
        role.obtainNextAction(this).perform(this);
        move(deltaTime);
    }

    public Position getDestination() {
        return destination;
    }

    @Override
    public void setDestination(final Position destination) {
        this.destination = destination;
    }

    @Override
    public boolean isFull() {
        return inventory.isFull();
    }

    @Override
    public boolean tryAdd(final IItem item) {
        return inventory.tryAdd(item);
    }

    @Override
    public void addMultiple(final List<IItem> stack) {
        inventory.addMultiple(stack);
    }

    @Override
    public boolean canFitItem(final ItemType itemType) {
        return inventory.canFitItem(itemType);
    }

    @Override
    public IItem take(final ItemType itemType) {
        return inventory.take(itemType);
    }

    @Override
    public ArrayList<IItem> takeAmount(final ItemType itemType, final int amount) {
        return inventory.takeAmount(itemType, amount);
    }

    @Override
    public boolean hasItem(final ItemType itemType) {
        return inventory.hasItem(itemType);
    }

    @Override
    public boolean hasItem(final ItemType itemType, final int amount) {
        return inventory.hasItem(itemType, amount);
    }

    @Override
    public int numberOf(final ItemType itemType) {
        return inventory.numberOf(itemType);
    }

    /**
     * Calculates and sets new position.
     *
     * @param deltaTime How much time the being should move at its speed forward, in seconds.
     */
    protected void move(final float deltaTime) {

        final float distanceToDestination = destination.distanceTo(getPosition());

        if (distanceToDestination < DESTINATION_REACHED_DISTANCE) {
            onArrivedAtDestination(destination);
            return;
        }

        movePositionTowardsDestination(deltaTime, distanceToDestination);
    }

    private void onArrivedAtDestination(final Position segmentDestination) {
        position = segmentDestination;
    }

    private void movePositionTowardsDestination(final float deltaTime, final float totalDistance) {
        // Calculate how much to move and in what direction.
        final Position delta = destination.subtract(position);
        final Position direction = delta.multiply(1f / totalDistance);
        final Position velocity = direction.multiply(SPEED);
        final Position movement = velocity.multiply(deltaTime);

        Position newPosition = position.add(movement);

        if (hasOvershotDestination(delta, newPosition)) {
            // Clamp position to destination,
            // to prevent walking past the destination during large time skips.
            newPosition = destination;
        }

        position = newPosition;
    }

    private boolean hasOvershotDestination(final Position delta, final Position newPosition) {
        // Destination has been overshot if the delta has changed sign before and after moving.
        final Position newDelta = destination.subtract(newPosition);
        return hasChangedSign(newDelta.getX(), delta.getX()) || hasChangedSign(newDelta.getY(),
                                                                               delta.getY());
    }

    private boolean hasChangedSign(final float posX, final float posX2) {
        return Math.signum(posX) != Math.signum(posX2);
    }

}
