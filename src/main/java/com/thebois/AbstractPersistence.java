package com.thebois;

import java.io.File;

abstract class AbstractPersistence {

    protected static final String DIRECTORY =
        System.getProperty("user.home") + "/Documents/Pawntastic/saves/";
    protected static final String FILE = DIRECTORY + "save.txt";

    protected static void diretoryExist() {
        final File file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
