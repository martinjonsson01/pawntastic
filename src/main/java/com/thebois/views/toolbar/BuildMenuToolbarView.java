package com.thebois.views.toolbar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.thebois.listeners.IEventListener;
import com.thebois.listeners.events.OnClickEvent;
import com.thebois.models.world.structures.StructureType;
import com.thebois.views.info.IActorView;

/**
 * View that renders structure buttons used for selecting what structure to build.
 *
 * @author Mathias
 */
public class BuildMenuToolbarView implements IActorView {

    private final Actor structureButtonContainer;

    /**
     * Instantiate with buttons for each structure.
     *
     * @param skin     The skin used to create the buttons.
     * @param listener The listener that wants to listen to the structure buttons.
     */
    public BuildMenuToolbarView(
        final Skin skin, final IEventListener<OnClickEvent<StructureType>> listener) {
        structureButtonContainer = createStructureButtonContainer(skin, listener);
    }

    private Actor createStructureButtonContainer(
        final Skin skin, final IEventListener<OnClickEvent<StructureType>> listener) {

        final Table toolbarTable = createTable();

        final ButtonGroup<StructureButton> buttonGroup = createButtonGroup();

        createStructureButtons(skin, listener, toolbarTable, buttonGroup);
        return toolbarTable;
    }

    private void createStructureButtons(
        final Skin skin,
        final IEventListener<OnClickEvent<StructureType>> listener,
        final Table toolbarTable,
        final ButtonGroup<StructureButton> buttonGroup) {
        for (final StructureType type : StructureType.values()) {
            final StructureButton structureButton = new StructureButton(type, skin);
            toolbarTable.add(structureButton).expand().fill();
            buttonGroup.add(structureButton);
            structureButton.registerListener(listener);
        }
    }

    private ButtonGroup<StructureButton> createButtonGroup() {
        final ButtonGroup<StructureButton> buttonGroup = new ButtonGroup<>();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        return buttonGroup;
    }

    private Table createTable() {
        final Table toolbarTable = new Table();
        toolbarTable.left().top();
        toolbarTable.row().expand().fill();
        return toolbarTable;
    }

    @Override
    public Actor getWidgetContainer() {
        return structureButtonContainer;
    }

}
