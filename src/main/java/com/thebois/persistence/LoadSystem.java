package com.thebois.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Handles loading of saved objects in the save-file.
 */
public class LoadSystem extends AbstractPersistence {

    private final ObjectInputStream objectInputStream;

    LoadSystem() throws IOException {

        directoryExist();
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

    /**
     * Returns computer resources used for holding the input stream.
     *
     * @throws IOException If the file is not found. Will only occur if file is deleted *
     *                     during runtime.
     */
    public void close() throws IOException {
        objectInputStream.close();
    }

}
