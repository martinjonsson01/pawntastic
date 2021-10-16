package com.thebois;

import java.io.File;

/**
 * Basic foundation for persistence, common functionality for all persistence classes.
 */
abstract class AbstractPersistence {

    private final String directory =
        System.getProperty("user.home") + "/Documents/Pawntastic/saves/";

    /**
     * All subclasses need to use this in the constructor to ensure the directory is there before
     * any commands/operations can be carried out.
     */
    protected void directoryExist() {
        final File filePath = new File(directory);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
    }

    /**
     * Subclasses uses the same save-file.
     *
     * @return The path to the save-file.
     */
    protected String getSaveFilePath() {
        return directory + "save.txt";
    }

}
