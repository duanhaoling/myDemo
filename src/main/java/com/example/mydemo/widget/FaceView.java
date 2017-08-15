package com.example.mydemo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by ldh on 2017/8/14.
 * 人脸检测 ,识别图片中的face
 */

@SuppressLint("AppCompatCustomView")
public class FaceView extends ImageView {

    public static final String TAG = "FaceView";
    /**
     * 识别图片的宽高
     */
    private int imageWidth, imageHeight;

    private FaceDetector mFaceDetector;

    /**
     * 一次可识别的最大数
     */
    private int maxFace = 3;
    private Bitmap mFaceImage;
    /**
     * 存储识别的脸
     */
    private FaceDetector.Face[] mFaces = new FaceDetector.Face[maxFace];
    /**
     * 真实检测到的人脸数
     */
    private int mFactFaces;

    private float myEyesDistance;


    public FaceView(Context context) {
        super(context);
    }

    public FaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        initFace(resId);
        super.setImageResource(resId);
    }

    private void initFace(@DrawableRes int resId) {
        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;//一定是 565，其他识别不了。
        // 拿到需要识别的图片
        mFaceImage = BitmapFactory.decodeResource(getResources(), resId, mOptions);
        imageWidth = mFaceImage.getWidth();
        imageHeight = mFaceImage.getHeight();
        //创建FaceDetector
        mFaceDetector = new FaceDetector(imageWidth, imageHeight, maxFace);
        // 开始检测，并将检测到的人脸存到mFaces数组中
        mFactFaces = mFaceDetector.findFaces(mFaceImage, mFaces);
        Log.e(TAG, "检测到人脸数:" + mFactFaces);
    }


    /**
     * 对每个人脸进行画框
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        //对每个人脸画框
        for (int i = 0; i < mFactFaces; i++) {
            FaceDetector.Face face = mFaces[i];
            PointF pointF = new PointF();
            face.getMidPoint(pointF);
            myEyesDistance = face.eyesDistance();//得到人脸中心点和眼间距离参数
            canvas.drawRect(pointF.x - myEyesDistance,
                    pointF.y - myEyesDistance,
                    pointF.x + myEyesDistance,
                    (float) (pointF.y + myEyesDistance * 1.5),
                    paint);

        }
    }
}
