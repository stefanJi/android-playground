package io.github.stefanji.playground;

import java.lang.reflect.Method;

/**
 * Create by jy on 2019-11-06
 */
public class JNIRegiester {

    static {
        System.loadLibrary("jni_lib");
    }

    public native void hello();

    public native void hello2();

    public static void f1() {
    }

    public static void f2() {
    }

    /**
     * 动态替换类的任何方法，需要确保 dest 和 src 的描述符, 访问权限一致
     *
     * @param src  期望被替代的方法
     * @param dest 作为补丁的方法
     */
    public static native void hotfix(Method src, Method dest);
}
