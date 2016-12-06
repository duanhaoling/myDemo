package com.example.mydemo.camera;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mydemo.R;
import com.example.mydemo.camera.exception.CameraDisabledException;
import com.example.mydemo.camera.exception.CameraErrorCallback;
import com.example.mydemo.camera.exception.CameraHardwareException;
import com.example.mydemo.camera.ui.ShutterButton;

public class CameraActivity extends CameraActivityBase {
    // 保存的相机的配置偏好
    private ComboPreferences mPreferences;
    private int mCameraId;
    private AFocusManager mFocusManager;

    // 相机拍照过程中的几种状态
    private static final int STATE_PREVIEW_STOPPED = 0;
    private static final int STATE_IDLE = 1; // preview is active
    // Focus is in progress. The exact focus state is in Focus.java.
    private static final int STATE_FOCUSING = 2;
    private static final int STATE_SNAPSHOT_IN_PROGRESS = 3;
    private int mCameraState = STATE_PREVIEW_STOPPED;
    private boolean mSnapshotOnIdle = false;


    // 获取相机硬件设备的异常结果
    private boolean mOpenCameraFail = false;
    private boolean mCameraDisabled = false;
    CameraErrorCallback mErrorCallback = new CameraErrorCallback();
    ShutterCallback shutterCallback = new ShutterCallback();

    // 相机拍照过程中的时间记录
    private long mFocusStartTime;
    private long mCaptureStartTime;
    private long mShutterCallbackTime;
    private long mPostViewPictureCallbackTime;
    private long mRawPictureCallbackTime;
    private long mJpegPictureCallbackTime;
    private long mOnResumeTime;

    // These latency time are for the CameraLatency test.
    public long mAutoFocusTime;
    public long mShutterLag;
    public long mShutterToPictureDisplayedTime;
    public long mPictureDisplayedToJpegCallbackTime;
    public long mJpegCallbackFinishTime;

    // 照相控制区域的控件
    private Button cancle_btn;
    ShutterButton mShutterButton;
    private Button ok_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO 初始化混合设置
        getPreferredCameraId();
        // TODO 初始化FocusManager
        String[] defaultFocusModes = getResources().getStringArray(
                R.array.pref_camera_focusmode_default_array);
        mFocusManager = new AFocusManager(mPreferences, defaultFocusModes);
        mPreferences.setLocalId(this, mCameraId);
    /*
     * To reduce startup time, we start the camera open and preview threads.
     * We make sure the preview is started at the end of onCreate.
     */
        mCameraOpenThread.start();
        setContentView(R.layout.activity_camera);
        // TODO 去做一些显示那些控件的初始化
        ok_btn = (Button) findViewById(R.id.camera_ok_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                savePicsBeforePause();
//                cameraBusiness.onClickOKBtn(CameraActivity.this);
            }
        });
        cancle_btn = (Button) findViewById(R.id.camera_cancle_btn);
        cancle_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelActivity();
            }
        });


    }

    private void cancelActivity() {
    }

    private void savePicsBeforePause() {
    }

    @Override
    protected void doOnResume() {

    }

    private void getPreferredCameraId() {
        mPreferences = new ComboPreferences(this);
        mCameraId = CameraSettings.readPreferredCameraId(mPreferences);
    }

    Thread mCameraOpenThread = new Thread(new Runnable() {
        public void run() {
            try {
                mCameraDevice = Util.openCamera(CameraActivity.this);
            } catch (CameraHardwareException e) {
                mOpenCameraFail = true;
            } catch (CameraDisabledException e) {
                mCameraDisabled = true;
            }
        }
    });

    private final class ShutterCallback implements android.hardware.Camera.ShutterCallback {
        public void onShutter() {
            mShutterCallbackTime = System.currentTimeMillis();
            mShutterLag = mShutterCallbackTime - mCaptureStartTime;
            mFocusManager.onShutter();
        }
    }

}
