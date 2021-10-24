package com.thebois.persistence;

import java.io.File;

import com.badlogic.gdx.utils.Disposable;

/**
 * Basic foundation for persistence, common functionality for all persistence classes.
 *
 * @author Jonathan
 */
abstract class AbstractPersistence implements Disposable {

    private final String saveDirectory =
        System.getProperty("user.home") + "/Documents/Pawntastic/saves/";

    /**
     * Ensures that the save directory exists before any other commands/operations are carried out.
     */
    protected AbstractPersistence() {
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
