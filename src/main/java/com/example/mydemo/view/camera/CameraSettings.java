package com.example.mydemo.view.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;

import com.example.mydemo.R;

import java.util.List;

public class CameraSettings {
    private static CommonLog log = new CommonLog("CameraSettings");
    // 有些key可能暂时用不到，有的是多相机支持的，有的是摄像用的
    public static final int NOT_FOUND = -1;
    public static final String KEY_PICTURE_SIZE = "pref_camera_picturesize_key";
    public static final String KEY_FOCUS_MODE = "pref_camera_focusmode_key";
    public static final String KEY_FLASH_MODE = "pref_camera_flashmode_key";
    public static final String KEY_VIDEO_TIME_LAPSE_FRAME_INTERVAL = "pref_video_time_lapse_frame_interval_key";
    public static final String KEY_CAMERA_ID = "pref_camera_id_key";
    public static final String KEY_CAMERA_FIRST_USE_HINT_SHOWN = "pref_camera_first_use_hint_shown_key";
    public static final String KEY_VIDEO_FIRST_USE_HINT_SHOWN = "pref_video_first_use_hint_shown_key";
    public static final String KEY_VIDEO_EFFECT = "pref_video_effect_key";

    public static int readPreferredCameraId(SharedPreferences pref) {
        return Integer.parseInt(pref.getString(KEY_CAMERA_ID, "0"));
    }

    public static void initialCameraPictureSize(
            Context context, Parameters parameters) {
        // When launching the camera app first time, we will set the picture
        // size to the first one in the list defined in "arrays.xml" and is also
        // supported by the driver.
        List<Size> supported = parameters.getSupportedPictureSizes();
        if (supported == null)
            return;
        for (String candidate : context.getResources().getStringArray(
                R.array.pref_camera_picturesize_entryvalues)) {
            if (setCameraPictureSize(candidate, supported, parameters)) {
                SharedPreferences.Editor editor = ComboPreferences
                        .get(context).edit();
                editor.putString(KEY_PICTURE_SIZE, candidate);
                editor.commit();
                return;
            }
        }
        log.e("No supported picture size found");
    }

    /**
     * 尝试将candidate所表示的分辨率赋给parameters的PictureSize。
     * 
     * @param candidate 类似“1024x768”这样的字符串
     * @param supported 相机所支持的分辨率列表
     * @param parameters 相机的parameters
     * @return 是否匹配到并赋值成功
     */
    public static boolean setCameraPictureSize(
            String candidate, List<Size> supported, Parameters parameters) {
        int index = candidate.indexOf('x');
        if (index == NOT_FOUND)
            return false;
        int width = Integer.parseInt(candidate.substring(0, index));
        int height = Integer.parseInt(candidate.substring(index + 1));
        for (Size size : supported) {
            if (size.width == width && size.height == height) {
                parameters.setPictureSize(width, height);
                return true;
            }
        }
        return false;
    }
}
