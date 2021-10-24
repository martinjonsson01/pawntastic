package com.thebois.views;

import com.badlogic.gdx.math.Vector2;

import com.thebois.models.Position;

/**
 * Adapts different position formats to each other.
 *
 * @author Martin
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
        return new Vector2(position.getX(), position.getY());
    }

    /**
     * Converts vector to custom position.
     *
     * @param vector The vector to convert from.
     *
     * @return The same vector in position form.
     */
    public static Position toPosition(Vector2 vector) {
        return new Position(vector.x, vector.y);
    }

}
