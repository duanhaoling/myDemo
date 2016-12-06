package com.example.mydemo.camera;

import android.hardware.Camera;
import android.view.View;

import com.example.mydemo.camera.ui.FocusIndicatorView;

/**
 * Created by ldh on 2016/11/24 0024.
 */
public class AFocusManager {
    // 对焦的参数和保存配置
    private Camera.Parameters mParameters;// 相机的参数
    private ComboPreferences mPreferences;

    private String[] mDefaultFocusModes;
    private String mFocusMode;
    // 对焦状态的几种值
    private int mState = STATE_IDLE;
    private static final int STATE_IDLE = 0; // Focus is not active.
    private static final int STATE_FOCUSING = 1; // Focus is in progress.
    // Focus is in progress and the camera should take a picture after focus
    // finishes.
    private static final int STATE_FOCUSING_SNAP_ON_FINISH = 2;
    private static final int STATE_SUCCESS = 3; // Focus finishes and succeeds.
    private static final int STATE_FAIL = 4; // Focus finishes and fails.
    // 和对焦相关的UI
    private View mPreviewFrame; // 相机边框的View
    private FocusIndicatorView mFocusIndicator;// 对焦指示器的View
    // 其他状态值
    private boolean mInitialized;// 是否已经初始化成功
    // Listener的实例
    Listener mListener;

    /**
     * Camera页面的回调接口
     */
    public interface Listener {

        public void autoFocus();
        public void cancelAutoFocus();

        public boolean capture();

        public void setFocusParameters();

        public void playSound();

    }
    /**
     * 初始化FocusManager
     *
     * @param preferences 保存的混合的配置
     * @param defaultFocusModes 我们推荐的默认对焦模式，包括自动对焦和持续自动对焦，后者更厉害，但是出现的API晚
     */
    public AFocusManager(ComboPreferences preferences, String[] defaultFocusModes) {
        mDefaultFocusModes = defaultFocusModes;
        mPreferences = preferences;
    }

    /**
     * 初始化相机的设置参数,需要在initialize()之前调用。 <br>
     * This has to be initialized before initialize().
     *
     * @param parameters
     */
    public void initializeParameters(Camera.Parameters parameters) {
        mParameters = parameters;
    }

    public void onShutter() {

    }


}
