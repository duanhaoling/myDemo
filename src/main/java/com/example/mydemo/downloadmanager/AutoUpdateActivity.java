package com.example.mydemo.downloadmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.mydemo.R;
import com.example.mydemo.util.DownLoadUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AutoUpdateActivity extends AppCompatActivity {
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.button)
    Button button;
    private DownLoadUtils downLoadUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anto_upload);
        ButterKnife.bind(this);
        downLoadUtils = new DownLoadUtils(this);
        intEvent();
    }

    private void intEvent() {
        button.setOnClickListener(v -> {
            downLoadUtils.downloadAPK("http://d.koudai.com/com.koudai.weishop/1000f/weishop_1000f.apk", "weshop.apk");
        });
    }


}
