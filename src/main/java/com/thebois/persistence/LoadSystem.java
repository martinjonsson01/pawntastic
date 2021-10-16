package com.thebois.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.thebois.models.beings.Colony;
import com.thebois.models.world.World;

/**
 * Handles loading of saved objects in the save-file.
 */
public class LoadSystem extends AbstractPersistence {

    private final ObjectInputStream objectInputStream;

    /**
     * Creates an instance of an input stream for the save-file.
     *
     * @throws IOException If the save-file is deleted during runtime.
     */
    public LoadSystem() throws IOException {

        ensureDirectoryExists();
        final FileInputStream fileInputStream = new FileInputStream(getSaveFilePath());

        objectInputStream = new ObjectInputStream(fileInputStream);
    }

    /**
     * Reads an object from the save-file.
     *
     * @return The read object.
     *
     * @throws IOException            If the file is not found. Will only occur if file is deleted
     *                                during runtime.
     * @throws ClassNotFoundException If there are no objects in the save-file left.
     */
    private Object read() throws IOException, ClassNotFoundException {
        return objectInputStream.readObject();
    }

    /**
     * Reads an object from the save-file, and casts it as a World. The right order for
     * deserialization need to be done by the caller.
     *
     * @return The read object cast as World.
     *
     * @throws IOException            If the file is not found. Will only occur if file is deleted
     *                                during runtime.
     * @throws ClassNotFoundException If there are no objects in the save-file left.
     */
    public World loadWorld() throws IOException, ClassNotFoundException {
        return (World) read();
    }

    /**
     * Reads an object from the save-file, and casts it as a Colony. The right order for
     * deserialization need to be done by the caller.
     *
     * @return The read object cast as Colony.
     *
     * @throws IOException            If the file is not found. Will only occur if file is deleted
     *                                during runtime.
     * @throws ClassNotFoundException If there are no objects in the save-file left.
     */
    public Colony loadColony() throws IOException, ClassNotFoundException {
        return (Colony) read();
    }

    @Override
    public void dispose() {
        try {
            objectInputStream.close();
        }
        catch (final IOException error) {
            error.printStackTrace();
        }
    }

}
