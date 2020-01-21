package io.github.stefanji.playground.hotfix;

import android.util.Log;

/**
 * Create by jy on 2020-01-21
 */
public class Target {

    Target() {
        func();
    }

    public String hello() {
        return "hello";
    }

    private void func() {
        Log.d("TAG", "target call func");
    }

}
