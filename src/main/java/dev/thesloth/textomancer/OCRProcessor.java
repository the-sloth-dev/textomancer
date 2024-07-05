package dev.thesloth.textomancer;

import com.sun.jna.Pointer;
import java.io.File;

public class OCRProcessor {
    static {
        try {
            LibraryLoader.load("libtesseract.dylib", "libleptonica.dylib");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load native libraries", e);
        }
    }

    public static void main(String[] args) {
        File imageFile;
        File tessdataDir;
        try {
            imageFile = ResourceExtractor.extract("/testocr.png");
            tessdataDir = ResourceExtractor.extractTessdata("/tessdata");
        } catch (Exception e) {
            System.err.println("Failed to resources: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Pointer tesseract = Tesseract.INSTANCE.TessBaseAPICreate();
        Pointer image = null;
        try {
            // Initialize Tesseract API
            if (Tesseract.INSTANCE.TessBaseAPIInit3(tesseract, tessdataDir.getAbsolutePath(), "eng") != 0) {
                throw new RuntimeException("Could not initialize Tesseract.");
            }

            // Load the image using Leptonica
            image = Leptonica.INSTANCE.pixRead(imageFile.getAbsolutePath());
            if (image == null) {
                throw new RuntimeException("Failed to read the image file.");
            }

            // Set the image to Tesseract
            Tesseract.INSTANCE.TessBaseAPISetImage2(tesseract, image);

            // Extract text
            Pointer textPointer = Tesseract.INSTANCE.TessBaseAPIGetUTF8Text(tesseract);
            if (textPointer != null) {
                String extractedText = textPointer.getString(0);
                System.out.println("Extracted Text: " + extractedText);
                Tesseract.INSTANCE.TessDeleteText(textPointer); // Free text memory
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            Tesseract.INSTANCE.TessBaseAPIEnd(tesseract);
            Tesseract.INSTANCE.TessBaseAPIDelete(tesseract);
            if (imageFile != null && imageFile.exists()) {
                imageFile.delete();
            }
            if (tessdataDir != null && tessdataDir.exists()) {
                tessdataDir.delete();
            }
        }
    }
}