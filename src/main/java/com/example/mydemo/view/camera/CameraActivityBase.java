package com.example.mydemo.view.camera;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.mydemo.util.LogUtil;

/**
 * Created by ldh on 2016/11/23 0023.
 */

public abstract class CameraActivityBase extends AppCompatActivity {
    public static final String TAG = "ActivityBase";
    private static boolean LOGV = false;
    private boolean mOnResumePending;
    protected Camera mCameraDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Util.isTabletUI(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (LOGV)
            LogUtil.v(TAG, "onWindowFocusChanged.hasFocus=" + hasFocus
                    + ".mOnResumePending=" + mOnResumePending);
        if (hasFocus && mOnResumePending) {
            doOnResume();
            mOnResumePending = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Don't grab the camera if in use by lockscreen. For example, face
        // unlock may be using the camera. Camera may be already opened in
        // onCreate. doOnResume should continue if mCameraDevice != null.
        // Suppose camera app is in the foreground. If users turn off and turn
        // on the screen very fast, camera app can still have the focus when the
        // lock screen shows up. The keyguard takes input focus, so the caemra
        // app will lose focus when it is displayed.
        if (LOGV)
            LogUtil.v(TAG, "onResume. hasWindowFocus()=" + hasWindowFocus());
        if (mCameraDevice == null && isKeyguardLocked()) {
            if (LOGV)
                LogUtil.v(TAG, "onResume. mOnResumePending=true");
            mOnResumePending = true;
        } else {
            if (LOGV)
                LogUtil.v(TAG, "onResume. mOnResumePending=false");
            doOnResume();
            mOnResumePending = false;
        }

    }

    // Put the code of onResume in this method.
    abstract protected void doOnResume();

    @Override
    public boolean onSearchRequested() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Prevent software keyboard or voice search from showing up.
        if (keyCode == KeyEvent.KEYCODE_SEARCH
                || keyCode == KeyEvent.KEYCODE_MENU) {
            if (event.isLongPress())
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }



    private boolean isKeyguardLocked() {
        KeyguardManager kgm = (KeyguardManager)
                getSystemService(Context.KEYGUARD_SERVICE);
        // isKeyguardSecure excludes the slide lock case.
        return (kgm != null) && kgm.inKeyguardRestrictedInputMode();
    }
}
