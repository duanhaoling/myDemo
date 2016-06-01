package com.example.mydemo.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ldh on 2016/4/26 0026.
 */
public class MyToast {

    /**
     * 单例吐司
     */
    private static Toast toast;
    private static boolean isDebug = false;

    public static void showtoast(Context context,String s) {

        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        if (isDebug) {
            toast.setText(s);
            toast.show();
        }
    }
}
