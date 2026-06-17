package org.example.export;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Resolves (and creates on demand) the project's {@code output/} directory,
 * where the exported PDF and JSON files are stored.
 */
public final class OutputDirectory {

    private static final Path OUTPUT = Path.of("output");

    private OutputDirectory() {
    }

    /**
     * @param fileName the file to place inside the output directory
     * @return the absolute path to that file, with the directory guaranteed to exist
     */
    public static Path resolve(String fileName) {
        try {
            Files.createDirectories(OUTPUT);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not create output directory: " + OUTPUT.toAbsolutePath(), e);
        }
        return OUTPUT.resolve(fileName).toAbsolutePath();
    }
}
