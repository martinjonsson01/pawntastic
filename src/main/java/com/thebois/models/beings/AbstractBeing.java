package com.thebois.models.beings;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import com.thebois.Pawntastic;
import com.thebois.listeners.events.OnDeathEvent;
import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.Inventory;
import com.thebois.models.inventory.items.IConsumableItem;
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
    private static final float MAX_HEALTH = 100f;
    private static final float MAX_HUNGER = 100f;
    /**
     * How much hunger the pawn should lose per second.
     */
    private static final float HUNGER_RATE = 50f;
    /**
     * How much health the pawn should gain or lose per second.
     */
    private static final float HEALTH_RATES = 1f;
    private final IInventory inventory = new Inventory(MAX_CARRYING_CAPACITY);
    private Position position;
    private AbstractRole role;
    private Position destination;
    private float hunger = MAX_HUNGER;
    private float health = MAX_HEALTH;

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
        updateHunger(deltaTime);
        eatIfHungry();
        updateHealth(deltaTime);
        if (health > 0f) {
            role.obtainNextAction(this).perform(this);
            move(deltaTime);
        }
    }

    private void updateHunger(final float deltaTime) {
        final float changeHungerValue = HUNGER_RATE * deltaTime;
        hunger = Math.max(hunger - changeHungerValue, 0f);
    }

    private void eatIfHungry() {
        if (!isHungry()) return;

        final IConsumableItem food = findFoodInInventory();
        if (food != null) eat(food);
    }

    private void updateHealth(final float deltaTime) {
        final float changeHealthValue = HEALTH_RATES * deltaTime;
        if (hunger <= 0) {
            health = Math.max(health - changeHealthValue, 0);
            if (health == 0f) {
                Pawntastic.getEventBus().post(new OnDeathEvent(this));
            }
        }
        else {
            health = Math.min(health + changeHealthValue, MAX_HEALTH);
        }
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

    private boolean isHungry() {
        return hunger <= MAX_HUNGER / 2f;
    }

    private IConsumableItem findFoodInInventory() {
        final Iterable<ItemType> edibleItemTypes =
            Arrays.stream(ItemType.values()).filter(ItemType::isEdible).collect(Collectors.toSet());

        for (final ItemType edibleItemType : edibleItemTypes) {
            if (inventory.hasItem(edibleItemType)) {
                return (IConsumableItem) inventory.take(edibleItemType);
            }
        }
        return null;
    }

    private void eat(final IConsumableItem item) {
        hunger = Math.min(MAX_HUNGER, hunger + item.getNutrientValue());
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

    public Position getDestination() {
        return destination;
    }

    @Override
    public float getHealthRatio() {
        return health / MAX_HEALTH;
    }

    @Override
    public void setDestination(final Position destination) {
        this.destination = destination;
    }

    @Override
    public void addItem(final IItem item) {
        inventory.tryAdd(item);
    }

}
