package com.thebois.controllers.infocontrollers;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.thebois.controllers.IController;
import com.thebois.models.world.World;
import com.thebois.views.infoviews.IActorView;
import com.thebois.views.infoviews.InfoView;

/**
 * Container class for controllers that manage the info.
 */
public class InfoController {

    private final Collection<IController<IActorView>> controllers;
    private final InfoView infoView;

    /**
     * Instantiate Info Controller with all controller and views used for all panels.
     *
     * @param world The world that controllers manage.
     * @param skin  The skin to style widgets with.
     */
    public InfoController(final World world, final Skin skin) {

        final RoleController roleController = new RoleController(world.getRoleAllocator(), skin);
        final InventoryController inventoryController =
            new InventoryController(world.getColonyInventory(), skin);

        controllers = new ArrayList<>();
        controllers.add(roleController);
        controllers.add(inventoryController);

        infoView = createInfoView();
    }

    private InfoView createInfoView() {
        final ArrayList<IActorView> views = new ArrayList<>();
        for (final IController<IActorView> controller : controllers) {
            views.add(controller.getIView());
        }
        return new InfoView(views);
    }

    public InfoView getInfoView() {
        return infoView;
    }

    /**
     * Updates all controllers in the info controller.
     */
    public void update() {
        for (final IController<IActorView> controller : controllers) {
            controller.update();
        }
    }

}
