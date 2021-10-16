package com.thebois.views.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.thebois.models.world.structures.StructureType;

/**
 * A button that represent a structure and is used to select which structure to build.
 */
public class StructureButton extends Button {

    private static final float PADDING = 5f;
    private final StructureType structureType;
    private final Button button;

    /**
     * Instantiate a structure button with the type of structure it should represent.
     *
     * @param structureType The type or structure to represent.
     * @param skin          The skin used to display the button.
     */
    public StructureButton(final StructureType structureType, final Skin skin) {
        super(skin, "toggle");
        button = this;
        this.structureType = structureType;
        final Label text = new Label(structureType.toString(), skin);

        button.add(text).expandY().fillY();
        button.pad(PADDING);
        registerListeners();
    }

    private void registerListeners() {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (button.isChecked()) {
                    System.out.println(StructureType.HOUSE.toString());
                }
            }
        });
    }

}
