package com.example.mydemo.view.animation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mydemo.R;
import com.ldh.androidlib.view.dialog.demo.CommonDialogFragment;

/**
 * Created by ldh on 2016/4/25 0025.
 */
public class AnimationActivity extends AppCompatActivity {

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
        showDialog();
    }

    private void showDialog() {
        //这里不可以使用匿名内部类public static
        CommonDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setTitle("hello")
                .setMessage("hello world")
                .setPositiveButton("yes", (dialog, which) -> {
                    Toast.makeText(AnimationActivity.this, "hello", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AnimationActivity.this, TestImageLoaderActivity.class);
                    AnimationActivity.this.startActivity(intent);
                })
                .setNegativeButton("no", null)
                .show("test");
    }

}
