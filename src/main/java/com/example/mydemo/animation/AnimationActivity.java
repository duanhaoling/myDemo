package com.example.mydemo.animation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.mydemo.R;

/**
 * Created by ldh on 2016/4/25 0025.
 */
public class AnimationActivity extends AppCompatActivity{

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animaiton_demo);
        image = (ImageView) findViewById(R.id.image);
    }

    public void onClick(View view) {
        Animation animation = new MyAnimaiotn();
        image.startAnimation(animation);
    }
}
