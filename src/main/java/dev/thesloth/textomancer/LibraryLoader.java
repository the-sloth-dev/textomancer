package dev.thesloth.textomancer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class LibraryLoader {

    private static final Path tempDir = createTempDirectory();

    private static Path createTempDirectory() {
        try {
            // Create a single temporary directory for all libraries
            return Files.createTempDirectory("nativeLibs");
        } catch (Exception e) {
            throw new RuntimeException("Could not create temp directory for native libraries", e);
        }
    }

    public static void load(String... libraryPaths) throws IOException {
        for (String resourcePath : libraryPaths) {
            InputStream inputStream = LibraryLoader.class.getResourceAsStream("/libs/" + resourcePath);
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }

            String fileName = new File(resourcePath).getName();
            File tempFile = new File(tempDir.toFile(), fileName);
            tempFile.deleteOnExit();

            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }
        System.setProperty("jna.library.path", tempDir.toString());
    }
}