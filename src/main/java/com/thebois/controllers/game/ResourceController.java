package com.thebois.controllers.game;

import com.thebois.controllers.IController;
import com.thebois.models.world.World;
import com.thebois.views.game.IView;
import com.thebois.views.game.ResourceView;

/**
 * Controller that gets resource data from the world and passes it to the view that renders it.
 */
public class ResourceController implements IController<IView> {

    private final World world;
    private final ResourceView resourceView;

    /**
     * Creates a Resource Controller.
     *
     * @param world    The world that controller should get data from.
     * @param tileSize The tile size of the world.
     */
    public ResourceController(final World world, final float tileSize) {
        this.world = world;
        this.resourceView = new ResourceView(tileSize);
    }

    @Override
    public IView getView() {
        return resourceView;
    }

    @Override
    public void update() {
        resourceView.update(world.getResources());
    }

}
