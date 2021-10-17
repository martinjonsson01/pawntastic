package com.thebois.persistence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Handles the saving of objects to the save-file.
 */
public class SaveSystem extends AbstractPersistence {

    private final ObjectOutputStream objectOutputStream;

    /**
     * Creates an instance of an output stream for the save-file.
     *
     * @throws IOException If the file save-file is deleted during runtime.
     */
    public SaveSystem() throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(getSaveFilePath());
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
    }

    /**
     * Serilizes the given obejct and stores it in a save-file.
     *
     * @param object The object to be saved.
     *
     * @throws IOException If the save-file is not found. Will only occur if file is deleted *
     *                     during runtime.
     */
    public void save(final Object object) throws IOException {
        objectOutputStream.writeObject(object);
    }

    @Override
    public void dispose() {
        try {
            objectOutputStream.flush();
            objectOutputStream.close();
        }
        catch (final IOException error) {
            error.printStackTrace();
        }
    }

}
