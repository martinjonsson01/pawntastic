package com.thebois.controllers.toolbar;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.thebois.controllers.IController;
import com.thebois.models.world.World;
import com.thebois.views.IProjector;
import com.thebois.views.info.IActorView;
import com.thebois.views.toolbar.ToolbarView;

/**
 * Container class for controllers that manage the toolbar.
 */
public class ToolbarController implements IController<IActorView> {

    private final Collection<IController<IActorView>> controllers;
    private final Collection<InputProcessor> inputProcessors;
    private final ToolbarView toolbarView;

    /**
     * Instantiate with all controller and views used for the toolbar.
     *
     * @param world     The world that controllers manage.
     * @param skin      Used to style widgets with.
     * @param projector Used to translate screen coordinate to world coordinates.
     */
    public ToolbarController(
        final World world, final Skin skin, final IProjector projector) {
        final PlaceStructureController placeStructureController =
            new PlaceStructureController(world, projector, skin);

        controllers = List.of(placeStructureController);
        inputProcessors = List.of(placeStructureController);

        toolbarView = createToolbarView();

        placeStructureController.setToolbarWidget(toolbarView.getWidgetContainer());
    }

    private ToolbarView createToolbarView() {
        final List<IActorView> views = controllers.stream().map(IController::getView).collect(
            Collectors.toList());
        return new ToolbarView(views);
    }

    @Override
    public IActorView getView() {
        return toolbarView;
    }

    @Override
    public void update() {
        for (final IController<IActorView> controller : controllers) {
            controller.update();
        }
    }

    public Iterable<InputProcessor> getInputProcessors() {
        return inputProcessors;
    }

}
