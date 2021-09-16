package com.thebois.views;

import com.thebois.models.Position;

import com.badlogic.gdx.math.Vector2;

/**
 * Adapts different position formats to each other.
 */
public final class PositionAdapter {

    private PositionAdapter() {

    }

    /**
     * Converts custom position to vector.
     *
     * @param position The position to convert from.
     *
     * @return The same position in Vector form.
     */
    public static Vector2 toVector(Position position) {
        return new Vector2(position.getPosX(), position.getPosY());
    }

}
