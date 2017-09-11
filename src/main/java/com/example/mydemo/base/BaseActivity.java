package com.example.mydemo.base;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ldh on 2016/7/5 0005.
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * gotoPersonCenter
     * gotoMovieDetail:movieId = 100
     * gotoNewList:cityId=1&cityName = 上海
     * gotoUrl:http://www.sina.com
     */
    public void gotoAnyWhere(String url) {
        if (url != null) {
            if (url.startsWith("gotoMovieDetail:")) {
                String strMovieId = url.substring(24);
                int movieId = Integer.valueOf(strMovieId);

            }
        }
    }

    public void gotoActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
