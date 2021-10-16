package com.thebois.views.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

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
    private final Collection<IEventListener<ValueChangedEvent<Integer>>> valueChangedListeners =
        new ArrayList<>();
    private Predicate<Integer> canIncrease = currentValue -> true;
    private Predicate<Integer> canDecrease = currentValue -> true;
    private int value;

    /**
     * Instantiates a new spinner.
     *
     * @param skin The skin to style widgets with
     * @param min  The minimum value the spinner will allow
     * @param max  The maximum value the spinner will allow
     */
    public SpinnerButton(final Skin skin, final int value, final int min, final int max) {
        this.value = value;
        this.min = min;
        this.max = max;

        final TextButton.TextButtonStyle buttonStyle = skin.get(TextButton.TextButtonStyle.class);
        addButton = new TextButton("+", buttonStyle);
        removeButton = new TextButton("-", buttonStyle);
        countLabel = new Label("" + value, skin);
        countLabel.setAlignment(Align.center);

        setPadding();

        registerListeners();

        updateButtonDisabledState();

        this.add(removeButton);
        this.add(countLabel).minWidth(LABEL_MIN_WIDTH).expandX();
        this.add(addButton);
    }

    public void setCanIncrease(final Predicate<Integer> canIncrease) {
        this.canIncrease = canIncrease;
    }

    public void setCanDecrease(final Predicate<Integer> canDecrease) {
        this.canDecrease = canDecrease;
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

    /**
     * Recalculate whether the add/remove-buttons should be disabled.
     */
    public void updateButtonDisabledState() {
        addButton.setDisabled(!canIncreaseValue());
        removeButton.setDisabled(!canDecreaseValue());
    }

    private boolean canDecreaseValue() {
        return value > min && canDecrease.test(value);
    }

    private boolean canIncreaseValue() {
        return value < max && canIncrease.test(value);
    }

    private void updateValue(final int newValue) {
        final int oldValue = value;
        value = newValue;
        countLabel.setText(value);

        notifyListeners(oldValue, newValue);

        // Update disabled-state after listeners have been notified because it might
        // depend on the state of the listeners.
        updateButtonDisabledState();
    }

    private void notifyListeners(final int oldValue, final int newValue) {
        final ValueChangedEvent<Integer> event = new ValueChangedEvent<>(oldValue, newValue);
        for (final IEventListener<ValueChangedEvent<Integer>> listener : valueChangedListeners) {
            listener.onEvent(event);
        }
    }

    @Override
    public void registerListener(final IEventListener<ValueChangedEvent<Integer>> listener) {
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
