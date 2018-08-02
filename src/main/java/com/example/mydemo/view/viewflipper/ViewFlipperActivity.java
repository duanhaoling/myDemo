package com.example.mydemo.view.viewflipper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

import com.example.mydemo.R;

public class ViewFlipperActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private float startX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flipper);
        initView();

    }

    private void initView() {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.setFlipInterval(1000);
        viewFlipper.setAutoStart(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() > startX) { //向右滑动
                    viewFlipper.setInAnimation(this, R.anim.in_leftright);
                    viewFlipper.setOutAnimation(this,R.anim.out_leftright);
                    viewFlipper.showNext();
                } else if (event.getX() < startX) { //向左滑动
                    viewFlipper.setInAnimation(this, R.anim.in_rightleft);
                    viewFlipper.setOutAnimation(this,R.anim.out_rightleft);
                    viewFlipper.showPrevious();
                }
        }
        return super.onTouchEvent(event);
    }
}
