package com.thebois.views;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import com.thebois.listeners.IEventListener;
import com.thebois.listeners.IEventSource;
import com.thebois.listeners.events.ValueChangedEvent;

/**
 * A widget for changing an amount in discrete steps using two buttons.
 */
public class SpinnerButton extends Table implements IEventSource<ValueChangedEvent<Integer>> {

    private static final float PAD_Y = 5f;
    private static final float PAD_X = 15f;
    private static final float PAD_TOP = 30f;
    private static final float LABEL_MIN_WIDTH = 40f;
    private final Label countLabel;
    private final int min;
    private final int max;
    private final TextButton addButton;
    private final TextButton removeButton;
    private final Collection<IEventListener<ValueChangedEvent<Integer>>>
        valueChangedListeners =
        new ArrayList<>();
    private int value = 0;

    /**
     * Instantiates a new spinner.
     *
     * @param skin The skin to style widgets with
     * @param min  The minimum value the spinner will allow
     * @param max  The maximum value the spinner will allow
     */
    public SpinnerButton(final Skin skin, final int min, final int max) {
        this.min = min;
        this.max = max;

        final TextButton.TextButtonStyle buttonStyle = skin.get(TextButton.TextButtonStyle.class);
        addButton = new TextButton("+", buttonStyle);
        removeButton = new TextButton("-", buttonStyle);
        countLabel = new Label("0", skin);
        countLabel.setAlignment(Align.center);

        setPadding();

        registerListeners();

        updateButtonDisabledState();

        this.add(removeButton);
        this.add(countLabel).minWidth(LABEL_MIN_WIDTH).expandX();
        this.add(addButton);
    }

    private void setPadding() {
        this.padTop(PAD_TOP);
        addButton.pad(PAD_Y, PAD_X, PAD_Y, PAD_X);
        removeButton.pad(PAD_Y, PAD_X, PAD_Y, PAD_X);
    }

    private void registerListeners() {
        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (addButton.isDisabled()) return;
                updateValue(value + 1);
            }
        });
        removeButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (removeButton.isDisabled()) return;
                updateValue(value - 1);
            }
        });
    }

    private void updateButtonDisabledState() {
        addButton.setDisabled(value >= max);
        removeButton.setDisabled(value <= min);
    }

    private void updateValue(final int newValue) {
        value = newValue;
        countLabel.setText(value);

        updateButtonDisabledState();
    }

    @Override
    public void addListener(final IEventListener<ValueChangedEvent<Integer>> listener) {
        valueChangedListeners.add(listener);
    }

    @Override
    public void removeListener(final IEventListener<ValueChangedEvent<Integer>> listener) {
        if (!valueChangedListeners.contains(listener)) {
            throw new IllegalArgumentException("Can not remove listener that is not listening");
        }
        valueChangedListeners.remove(listener);
    }

}
