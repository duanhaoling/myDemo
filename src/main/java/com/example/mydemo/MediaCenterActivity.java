package com.example.mydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mydemo.image_picker.clip.ClipActivity;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MediaCenterActivity extends AppCompatActivity {
    @Bind(R.id.image_card)
    ImageView imageCard;
    @Bind(R.id.take_photo)
    Button takePhoto;
    @Bind(R.id.select_pic)
    Button selectPic;
    private int mScreenWidth;
    public static final int REQUEST_CLIP = 1;
    private File cacheFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_center);
        ButterKnife.bind(this);
        initScreenWidth();
        initView();
    }

    private void initView() {
        takePhoto.setOnClickListener(v -> {
            gotoClip(ClipActivity.REQUEST_CAMERA);
        });
        selectPic.setOnClickListener(v -> {
            gotoClip(ClipActivity.REQUEST_GALLARY);
        });
    }


    private void initScreenWidth() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
    }

    private void gotoClip(int requestCode) {
        Intent intent = new Intent(this, ClipActivity.class);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("width", mScreenWidth);
        intent.putExtra("height", mScreenWidth * 270 / 640);
        startActivityForResult(intent, REQUEST_CLIP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CLIP:
                if (resultCode == RESULT_OK) {
                    String cacheUrl = data.getStringExtra(ClipActivity.KEY_CLIP_CACHE_URL);
                    if (TextUtils.isEmpty(cacheUrl)) {
                        return;
                    }
                    Toast.makeText(this, cacheUrl, Toast.LENGTH_LONG).show();


                    cacheFile = new File(cacheUrl);

                    Glide.with(this).load(cacheUrl).into(imageCard);

//                    new UploadImageTask().execute(cacheFile.getAbsolutePath());

                }
                break;
        }
    }
}
