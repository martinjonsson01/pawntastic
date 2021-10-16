package com.thebois.persistence;

import java.io.File;

import com.badlogic.gdx.utils.Disposable;

/**
 * Basic foundation for persistence, common functionality for all persistence classes.
 */
abstract class AbstractPersistence implements Disposable {

    private final String saveDirectory =
        System.getProperty("user.home") + "/Documents/Pawntastic/saves/";

    /**
     * All subclasses need to use this in the constructor to ensure the directory is there before
     * any commands/operations can be carried out.
     */
    protected void ensureDirectoryExists() {
        final File filePath = new File(saveDirectory);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
    }

    /**
     * Gets the path to the save file.
     *
     * @return The path to the save-file.
     */
    protected String getSaveFilePath() {
        return saveDirectory + "save.txt";
    }

}
