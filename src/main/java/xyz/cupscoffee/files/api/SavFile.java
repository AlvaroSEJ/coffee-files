package xyz.cupscoffee.files.api;

import java.util.Map;

/**
 * Represents a {@code .sav} file.
 */
public interface SavFile {
    /**
     * Returns the name of the {@code .sav} file.
     * 
     * @return the name of the {@code .sav} file
     */
    String getName();

    /**
     * Returns the header of the {@code .sav} file.
     * 
     * @return the header of the {@code .sav} file
     */
    String getHeader();

    /**
     * Returns the version of the {@code .sav} file.
     * 
     * @return the version of the {@code .sav} file
     */
    Disk[] getDisks();

    /**
     * Returns the metadata of the {@code .sav} file.
     * 
     * @return the metadata of the {@code .sav} file
     */
    Map<String, String> getMetadata();
}
