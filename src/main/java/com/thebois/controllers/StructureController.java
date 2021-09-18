package com.thebois.controllers;

import com.thebois.models.world.World;
import com.thebois.views.StructureView;

/**
 * Controller for Structures in the world.
 */
public class StructureController extends AbstractInputProcessor {

    private final World world;
    private final StructureView structureView;

    /**
     * Creates a instance of a Structure Controller.
     *
     * @param world         The world in which the structures exists.
     * @param structureView A view where the structures will be displayed.
     */
    public StructureController(final World world, final StructureView structureView) {
        this.world = world;
        this.structureView = structureView;
    }

}
