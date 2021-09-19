package com.thebois.views;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class SpinnerButton extends HorizontalGroup {

    private static final float PAD_Y = 5f;
    private static final float PAD_X = 15f;

    /**
     * Instantiates a new spinner.
     *
     * @param skin The skin to style widgets with
     */
    public SpinnerButton(final Skin skin) {
        final TextButton.TextButtonStyle buttonStyle = skin.get(TextButton.TextButtonStyle.class);
        final TextButton addButton = new TextButton("+", buttonStyle);
        final TextButton removeButton = new TextButton("-", buttonStyle);
        final Label countLabel = new Label("0", skin);

        this.space(PAD_X);

        addButton.pad(PAD_Y, PAD_X, PAD_Y, PAD_X);
        removeButton.pad(PAD_Y, PAD_X, PAD_Y, PAD_X);

        this.addActor(removeButton);
        this.addActor(countLabel);
        this.addActor(addButton);
    }

}
