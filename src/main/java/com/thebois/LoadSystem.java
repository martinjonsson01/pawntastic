package com.thebois;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoadSystem extends AbstractPersistence {

    private final ObjectInputStream objectInputStream;

    LoadSystem() throws IOException {

        diretoryExist();
        final FileInputStream fileInputStream = new FileInputStream(FILE);

        objectInputStream = new ObjectInputStream(fileInputStream);
    }

    public Object read() throws IOException, ClassNotFoundException {
        return objectInputStream.readObject();
    }

    public void close() throws IOException {
        objectInputStream.close();
    }

}
