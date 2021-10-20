package com.thebois.models.world.resources;

import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.generation.noises.INoise;
import com.thebois.models.world.generation.noises.NoiseFactory;

/**
 * All types of resources.
 */
public enum ResourceType {
    /**
     * A body of water.
     */
    WATER(0.5f, 0, NoiseFactory.createLargeChunksNoise(), ItemType.FISH),
    /**
     * A natural tree.
     */
    TREE(0.5f, 31, NoiseFactory.createSmallChunksNoise(), ItemType.LOG),
    /**
     * A large stone on the ground.
     */
    STONE(0.5f, 27, NoiseFactory.createVerySmallChunksNoise(), ItemType.ROCK);
    private final float threshold;
    private final int seedPermutation;
    private final INoise noise;
    private final ItemType itemType;

    ResourceType(
        final float threshold,
        final int seedPermutation,
        final INoise noise,
        final ItemType itemType) {
        this.threshold = threshold;
        this.seedPermutation = seedPermutation;
        this.noise = noise;
        this.itemType = itemType;
    }

    /**
     * Threshold value used for resource generation.
     * <p>
     * Used together with some kind of value generator and if the threshold is over, under or equal
     * to the generated value then the resource should be created.
     * </p>
     *
     * <p>
     * Should be used like this: Value >= Threshold or Value <= Threshold.
     * </p>
     *
     * @return The Threshold.
     */
    public float getThreshold() {
        return threshold;
    }

    /**
     * Seed permutation used for resource generation.
     *
     * <p>
     * Used to offset the resource from other resources.
     * </p>
     * <p>
     * Should be used like this: BaseSeed "Some Operation" seedPermutation. Make sure to use the
     * same operation for all terrain in order to not get unwanted results.
     * </p>
     *
     * @return The seed permutation.
     */
    public int getSeedPermutation() {
        return seedPermutation;
    }

    /**
     * The noise used to generate this type of resource.
     *
     * @return The noise.
     */
    public INoise getNoise() {
        return noise;
    }

    /**
     * Gets the item type of the item that can be harvested from the resource.
     *
     * @return The item type.
     */
    public ItemType getItemType() {
        return itemType;
    }
}
