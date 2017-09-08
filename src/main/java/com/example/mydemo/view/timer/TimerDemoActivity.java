package com.example.mydemo.view.timer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.mydemo.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimerDemoActivity extends AppCompatActivity {


    TextView textView;
    Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        textView = (TextView)findViewById(R.id.text);
        timer = new Timer();
        timer.schedule(task,1000,1000);

        initActionBar();
    }
    private void initActionBar() {
        ActionBar actionBar =getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }
    }

    final Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    textView.setText(msg.obj.toString());
                    break;
            }
            super.handleMessage(msg);
        }
    };

    long time = 0;
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = formatElapsedTime(time);
            time++;
            handler.sendMessage(msg);
        }
    };

    /**
     * 格式化输出时间字符串
     * @param elapsedSeconds
     * @return
     */
    public static String formatElapsedTime(long elapsedSeconds){
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        if(elapsedSeconds >= 3600){
            hours = elapsedSeconds / 3600;
            elapsedSeconds -= hours * 3600;
        }
        if(elapsedSeconds >= 60){
            minutes = elapsedSeconds / 60;
            elapsedSeconds -= minutes * 60;
        }
        seconds = elapsedSeconds;
        StringBuilder sb = new StringBuilder();
        sb.append(addZeroIfNeed(hours));
        sb.append(":");
        sb.append(addZeroIfNeed(minutes));
        sb.append(":");
        sb.append(addZeroIfNeed(seconds));
        return sb.toString();
    }

    /**
     * 加0处理，不足两位时补0
     * @param number
     * @return
     */
    public static String addZeroIfNeed(long number){
        StringBuilder sb = new StringBuilder();
        if(number >= 0 && number <= 9){
            return sb.append("0").append(number).toString();
        }
        return sb.append(number).toString();
    }


}
