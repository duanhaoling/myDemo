package com.example.mydemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 扫描图的绘制
 * Created by ldh on 2016/5/12 0012.
 */
public class CircleSimple extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mRotate;
    private Matrix mMatrix = new Matrix();
    private Shader mShader;
    String TAG = getClass().getSimpleName();

    int mLastX;
    int mLastY;

    public CircleSimple(Context context) {
        this(context, null);
    }

    public CircleSimple(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleSimple(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置可以获取焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //初始化画笔
        float x = 160;
        float y = 160;
        mShader = new SweepGradient(x, y, new int[]{
                0xFFB0F44B,
                0xFFE8DD30,
                0xFFF1CA2E,
                0xFFFF902F,
                0xFFFF6433
        }, null);
        mPaint.setShader(mShader);
        mPaint.setStyle(Paint.Style.STROKE);
        PathEffect effect = new DashPathEffect(new float[]{2, 3, 2, 3}, 1);
        mPaint.setPathEffect(effect);
        mPaint.setStrokeWidth(40);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = mPaint;
        float x = 160;
        float y = 160;
        //背景色
        canvas.drawColor(Color.WHITE);
        //
        mMatrix.setRotate(mRotate, x, y);
        mShader.setLocalMatrix(mMatrix);
        mRotate += 3;
        if (mRotate >= 360) {
            mRotate = 0;
        }
        invalidate();
        getArc(canvas, x, y, 100, 135, 405, paint);
    }

    public void getArc(Canvas canvas, float o_x, float o_y, float r, float startAngel, float endAngel, Paint paint) {
        RectF rectF = new RectF(o_x - r, o_y - r, o_x + r, o_y + r);
        Path path = new Path();
        path.moveTo(o_x, o_y);
        path.lineTo((float) (o_x + r * Math.cos(startAngel * Math.PI / 180)), (float) (o_y + r * Math.sin(startAngel * Math.PI / 180)));
        path.lineTo((float) (o_x + r * Math.cos(endAngel * Math.PI / 180)), (float) (o_y + r * Math.sin(endAngel * Math.PI / 180)));
        path.addArc(rectF, startAngel, endAngel - startAngel);
        canvas.clipPath(path);
        canvas.drawCircle(o_x, o_y, r, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mLastX = getLeft();
        mLastY = getTop();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.d(TAG, "move,deltaX:" + deltaX + "deltaY:" + deltaY);
                int translationX = (int) ViewHelper.getTranslationX(this) + deltaX;
                int translationY = (int) ViewHelper.getTranslationY(this) + deltaY;
                ViewHelper.setTranslationX(this, translationX);
                ViewHelper.setTranslationY(this, translationY);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }
}
