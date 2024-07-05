package dev.thesloth.textomancer;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface Leptonica extends Library {
    Leptonica INSTANCE = Native.load("libleptonica.dylib", Leptonica.class);
    Pointer pixRead(String filename);
}
