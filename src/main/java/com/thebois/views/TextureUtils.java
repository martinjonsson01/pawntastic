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
     * Creates a new instance of a square shaped texture with the side length.
     *
     * @param sideLength The size of the square texture, in pixels.
     *
     * @return The square texture.
     */
    public static Texture createSquareTexture(final float sideLength) {
        final int roundedTileSize = (int) sideLength;
        final Pixmap pixmap = new Pixmap(roundedTileSize, roundedTileSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(0, 0, roundedTileSize, roundedTileSize);
        final Texture squareTexture = new Texture(pixmap);
        pixmap.dispose();
        return squareTexture;
    }

    /**
     * Creates a new instance of a triangle shaped texture with the side length.
     *
     * @param sideLength The size of the triangle texture, in pixels.
     *
     * @return The trinagle texture.
     */
    public static Texture createTriangleTexture(final float sideLength) {
        final int roundedTileSize = (int) sideLength;
        final Pixmap pixmap = new Pixmap(roundedTileSize, roundedTileSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillTriangle(roundedTileSize / 2,
                            0,
                            0,
                            roundedTileSize,
                            roundedTileSize,
                            roundedTileSize);
        final Texture squareTexture = new Texture(pixmap);
        pixmap.dispose();
        return squareTexture;
    }

}
