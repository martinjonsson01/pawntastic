package com.thebois.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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
    public Object read() throws IOException, ClassNotFoundException {
        return objectInputStream.readObject();
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
