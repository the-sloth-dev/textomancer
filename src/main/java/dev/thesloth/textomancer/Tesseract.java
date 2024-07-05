package dev.thesloth.textomancer;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface Tesseract extends Library {
    Tesseract INSTANCE = Native.load("libtesseract.dylib", Tesseract.class);

    Pointer TessBaseAPICreate();
    int TessBaseAPIInit3(Pointer handle, String dataPath, String language);
    void TessBaseAPISetImage2(Pointer handle, Pointer pix);
    Pointer TessBaseAPIGetUTF8Text(Pointer handle);
    void TessBaseAPIEnd(Pointer handle);
    void TessBaseAPIDelete(Pointer handle);
    void TessDeleteText(Pointer text);
}