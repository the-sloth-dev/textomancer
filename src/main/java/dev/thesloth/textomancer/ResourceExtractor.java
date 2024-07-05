package dev.thesloth.textomancer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceExtractor {

    public static File extract(String resourcePath) throws IOException {
        InputStream resourceStream = getResourceAsStream(resourcePath);
        if (resourceStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        File tempFile = Files.createTempFile("resource-", ".tmp").toFile();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = resourceStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }

    public static File extractTessdata(String tessdataResourcePath) throws IOException {
        Path tempDir = Files.createTempDirectory("tessdata");
        InputStream source =getResourceAsStream(tessdataResourcePath + "/eng.traineddata");
        Path destination = tempDir.resolve("eng.traineddata");
        Files.copy(source, destination);
        return tempDir.toFile();
    }

    private static InputStream getResourceAsStream(String resourcePath) {
        return ResourceExtractor.class.getResourceAsStream(resourcePath);
    }
}