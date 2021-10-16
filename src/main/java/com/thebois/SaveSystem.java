package com.thebois;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Handles the saving of objects to the save-file.
 */
public class SaveSystem extends AbstractPersistence {

    private final ObjectOutputStream objectOutputStream;

    SaveSystem() throws IOException {

        directoryExist();

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

    /**
     * Flushes the output stream and returns computer resoruceses used but the output stream.
     *
     * @throws IOException If the save-file is not found. Will only occur if file is deleted *
     *                     during runtime.
     */
    public void exit() throws IOException {
        objectOutputStream.flush();
        objectOutputStream.close();
    }

}
