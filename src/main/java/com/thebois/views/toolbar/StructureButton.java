package com.thebois.views.toolbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.thebois.listeners.IEventListener;
import com.thebois.listeners.IEventSource;
import com.thebois.listeners.events.OnClickEvent;
import com.thebois.models.world.structures.StructureType;
import com.thebois.utils.StringUtils;

/**
 * A button that represent a structure and is used to select which structure to build.
 *
 * @author Mathias
 */
public class StructureButton extends Button implements IEventSource<OnClickEvent<StructureType>> {

    private static final float TOP_AND_BOTTOM_PADDING = 5f;
    private static final float LEFT_AND_RIGHT_PADDING = 20f;
    private final Collection<IEventListener<OnClickEvent<StructureType>>> onClickListeners =
        new ArrayList<>();
    private final StructureType structureType;

    /**
     * Instantiate a structure button with the type of structure it should represent.
     *
     * @param structureType The type or structure to represent.
     * @param skin          The skin used to display the button.
     */
    public StructureButton(final StructureType structureType, final Skin skin) {
        super(skin, "toggle");
        this.structureType = structureType;
        final String labelText = StringUtils.capitalizeFirst(structureType
                                                                 .toString()
                                                                 .toLowerCase(Locale.ROOT));
        final Label text = new Label(labelText, skin);

        this.add(text).expandY().fillY();
        this.pad(
            TOP_AND_BOTTOM_PADDING,
            LEFT_AND_RIGHT_PADDING,
            TOP_AND_BOTTOM_PADDING,
            LEFT_AND_RIGHT_PADDING);
        registerListeners();
    }

    private void registerListeners() {
        // This in lambda function does not work, which is why this line of code as added.
        final Button thisButton = this;
        this.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (thisButton.isChecked()) {
                    notifyListeners(structureType);
                }
            }
        });
    }

    private void notifyListeners(final StructureType type) {
        final OnClickEvent<StructureType> event = new OnClickEvent<>(type);
        onClickListeners.forEach(listener -> listener.onEvent(event));
    }

    @Override
    public void registerListener(final IEventListener<OnClickEvent<StructureType>> listener) {
        onClickListeners.add(listener);
    }

    @Override
    public void removeListener(final IEventListener<OnClickEvent<StructureType>> listener) {
        if (!onClickListeners.contains(listener)) {
            throw new IllegalArgumentException("Can not remove listener that is not listening");
        }
        onClickListeners.remove(listener);
    }

}
