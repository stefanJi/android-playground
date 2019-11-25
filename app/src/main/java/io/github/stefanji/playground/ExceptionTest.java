package io.github.stefanji.playground;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Create by jy on 2019-11-23
 */
public class ExceptionTest extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "A");
        new Thread() {
            @Override
            public void run() {
                Object o = null;
                Log.d("TAG", o.toString());
            }
        }.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "B");
    }
}
