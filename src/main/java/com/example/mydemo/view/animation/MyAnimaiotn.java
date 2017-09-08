package com.example.mydemo.view.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;

/**
 * Created by ldh on 2016/4/25 0025.
 */
public class MyAnimaiotn extends Animation{

    private int mCenterWidth;
    private int mCenterHeight;
    private Camera mCamera;
    private float mRotateY;

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        //设置默认时长
        setDuration(2000);
        //动画结束后保留状态
        setFillAfter(true);
        //设置默认插值器
        setInterpolator(new BounceInterpolator());
        mCenterWidth = width / 2;
        mCenterHeight = height / 2;
        mRotateY = 180f;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        final Matrix matrix = t.getMatrix();
        //通过matrix的各种操作来实现动画
        //3D动画
        simone(interpolatedTime,matrix);
        //模拟电视机关闭
        matrix.preScale(1, 1 - interpolatedTime, mCenterWidth, mCenterHeight);
    }

    private void simone(float interpolatedTime, Matrix matrix) {
        mCamera = new Camera();
        mCamera.save();
        //使用Camera设置旋转的角度
        mCamera.rotateY(mRotateY * interpolatedTime);
        //将旋转变换作用到matrix上
        mCamera.getMatrix(matrix);
        mCamera.restore();
        //通过pre方法设置矩阵作用前的偏移量来改变旋转中心
        matrix.preTranslate(mCenterWidth, mCenterHeight);
        matrix.postTranslate(-mCenterWidth, -mCenterHeight);
    }
}
