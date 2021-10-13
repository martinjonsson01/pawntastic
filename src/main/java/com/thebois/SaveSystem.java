package com.thebois;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveSystem extends AbstractPersistence {

    private final ObjectOutputStream objectOutputStream;

    SaveSystem() throws IOException {

        diretoryExist();

        final FileOutputStream fileOutputStream = new FileOutputStream(FILE);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);

    }

    public void save(final Object object) throws IOException {
        objectOutputStream.writeObject(object);
    }

    public void exit() throws IOException {
        objectOutputStream.flush();
        objectOutputStream.close();
    }

}
