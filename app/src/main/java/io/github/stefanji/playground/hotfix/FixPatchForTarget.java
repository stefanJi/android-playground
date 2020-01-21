package io.github.stefanji.playground.hotfix;

import android.util.Log;

/**
 * Create by jy on 2020-01-21
 */
public class FixPatchForTarget {
    public String hello() {
        return "hello word";
    }

    private void func() {
        Log.d("TAG", "patch call func");
    }
}
