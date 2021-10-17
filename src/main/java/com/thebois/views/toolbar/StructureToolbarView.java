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
public class StructureToolbarView implements IActorView {

    private final Actor structureButtons;

    /**
     * Instantiate a table with the different structure buttons.
     *
     * @param skin     The skin used to create the buttons.
     * @param listener The listener that wants to listen to the structure buttons.
     */
    public StructureToolbarView(
        final Skin skin, final IEventListener<OnClickEvent<StructureType>> listener) {
        structureButtons = createToolbar(skin, listener);
    }

    private Actor createToolbar(
        final Skin skin, final IEventListener<OnClickEvent<StructureType>> listener) {

        // Set up table.
        final Table toolbarTable = new Table();
        toolbarTable.left().top();
        toolbarTable.row().expand().fill();

        // Button group settings.
        final ButtonGroup<StructureButton> buttonGroup = new ButtonGroup<>();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);

        // Add buttons to table and button group.
        for (final StructureType type : StructureType.values()) {
            final StructureButton structureButton = new StructureButton(type, skin);
            toolbarTable.add(structureButton).expand().fill();
            buttonGroup.add(structureButton);
            structureButton.registerListener(listener);
        }
        return toolbarTable;
    }

    @Override
    public Actor getWidgetContainer() {
        return structureButtons;
    }

}
