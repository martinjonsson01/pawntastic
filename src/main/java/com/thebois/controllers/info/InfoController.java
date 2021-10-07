package com.thebois.controllers.info;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.thebois.controllers.IController;
import com.thebois.models.beings.Colony;
import com.thebois.views.info.IActorView;
import com.thebois.views.info.InfoView;

/**
 * Container class for controllers that manage the info.
 */
public class InfoController implements IController<InfoView> {

    private final Collection<IController<IActorView>> controllers;
    private final InfoView infoView;

    /**
     * Instantiate with all controller and views used for all panels.
     *
     * @param colony The colony that controllers manage.
     * @param skin   The skin to style widgets with.
     */
    public InfoController(final Colony colony, final Skin skin) {

        final RoleController roleController = new RoleController(colony, skin);
        final InventoryController inventoryController = new InventoryController(colony, skin);

        controllers = List.of(roleController, inventoryController);
        infoView = createInfoView();
    }

    private InfoView createInfoView() {
        final List<IActorView> views = controllers.stream().map(IController::getView).collect(
            Collectors.toList());
        return new InfoView(views);
    }

    @Override
    public InfoView getView() {
        return infoView;
    }

    @Override
    public void update() {
        for (final IController<IActorView> controller : controllers) {
            controller.update();
        }
    }

}
