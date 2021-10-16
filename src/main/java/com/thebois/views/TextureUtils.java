package com.thebois.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Helpers for creating and using textures.
 */
public final class TextureUtils {

    private TextureUtils() {

    }

    /**
     * Creates a new instance of a circle shaped texture with the given radius.
     *
     * <p>
     * Sets the base color to white.
     * </p>
     *
     * @param radius The radius of the circle texture, in pixels.
     *
     * @return The circle texture.
     */
    public static Texture createCircleTexture(final int radius) {
        final Pixmap pixmap = new Pixmap(radius * 2, radius * 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(radius, radius, radius);
        final Texture circleTexture = new Texture(pixmap, true);
        circleTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear,
                                Texture.TextureFilter.MipMapLinearLinear);
        pixmap.dispose();
        return circleTexture;
    }

    /**
     * Creates a new instance of a circle shaped texture with the given radius.
     *
     * @param radius The radius of the circle texture, in pixels.
     * @param color  The color used in the texture.
     *
     * @return The circle texture.
     */
    public static Texture createCircleTexture(final int radius, final Color color) {
        final Pixmap pixmap = new Pixmap(radius * 2, radius * 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillCircle(radius, radius, radius);
        final Texture circleTexture = new Texture(pixmap, true);
        circleTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear,
                                Texture.TextureFilter.MipMapLinearLinear);
        pixmap.dispose();
        return circleTexture;
    }

    /**
     * Creates a new instance of a square shaped texture with the side length.
     * <p>
     * Sets the base color to white.
     * </p>
     *
     * @param sideLength The size of the square texture, in pixels.
     *
     * @return The square texture.
     */
    public static Texture createSquareTexture(final int sideLength) {
        return createSquareTexture(sideLength, Color.WHITE);
    }

    /**
     * Creates a new instance of a square shaped texture with the side length.
     *
     * @param sideLength The size of the square texture, in pixels.
     * @param color      The color used in the texture.
     *
     * @return The square texture.
     */
    public static Texture createSquareTexture(final int sideLength, final Color color) {
        final Pixmap pixmap = new Pixmap(sideLength, sideLength, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, sideLength, sideLength);
        final Texture squareTexture = new Texture(pixmap);
        pixmap.dispose();
        return squareTexture;
    }

    /**
     * Creates a new instance of a triangle shaped texture with the side length.
     *
     * @param sideLength The size of the triangle texture, in pixels.
     *
     * @return The triangle texture.
     */
    public static Texture createTriangleTexture(final int sideLength) {
        return createTriangleTexture(sideLength, Color.WHITE);
    }

    /**
     * Creates a new instance of a triangle shaped texture with the side length.
     *
     * @param sideLength The size of the triangle texture, in pixels.
     * @param color      The color used in the texture.
     *
     * @return The triangle texture.
     */
    public static Texture createTriangleTexture(final int sideLength, final Color color) {
        final Pixmap pixmap = new Pixmap(sideLength, sideLength, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillTriangle(sideLength / 2, 0, 0, sideLength, sideLength, sideLength);
        final Texture squareTexture = new Texture(pixmap);
        pixmap.dispose();
        return squareTexture;
    }

}
