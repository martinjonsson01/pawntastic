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
 */
public class BuildMenuToolbarView implements IActorView {

    private final Actor structureButtonContainer;
    private final ButtonGroup<StructureButton> structureButtonGroup = createButtonGroup();

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
        createStructureButtons(skin, listener, toolbarTable);
        return toolbarTable;
    }

    private void createStructureButtons(
        final Skin skin,
        final IEventListener<OnClickEvent<StructureType>> listener,
        final Table toolbarTable) {
        for (final StructureType type : StructureType.values()) {
            // Don't add a button for the town hall structure.
            if (type.equals(StructureType.TOWN_HALL)) break;
            final StructureButton structureButton = new StructureButton(type, skin);
            toolbarTable.add(structureButton).expand().fill();
            structureButtonGroup.add(structureButton);
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

    /**
     * Allows for the buttons to be activated or disabled.
     *
     * @param activate Whether the buttons are to be disabled or not.
     */
    public void setButtonsActive(final boolean activate) {
        structureButtonGroup.getButtons().forEach(structureButton -> structureButton.setDisabled(
            activate));
    }

}
