package io.github.stefanji.playground;

/**
 * Create by jy on 2019-11-06
 */
public class TestJNIRegiester {

    static {
        System.loadLibrary("jni_test_lib");
    }
    public native void hello();
    public native void hello2();
}
