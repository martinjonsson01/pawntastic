package com.thebois;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * The entrance point to the program.
 *
 * @author Martin
 */
public final class Main {

    private static final int IDLE_FPS = 60;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 480;

    private Main() {

    }

    /**
     * Creates the window in which the program runs.
     *
     * @param args The arguments provided
     */
    public static void main(final String[] args) {
        final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("Pawntastic");
        config.setWindowIcon("icon.png");
        config.setForegroundFPS(IDLE_FPS);
        config.useVsync(true);

        config.setWindowedMode(WIDTH, HEIGHT);

        new Lwjgl3Application(new Pawntastic(), config);
    }

}
