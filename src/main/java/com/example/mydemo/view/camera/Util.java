package com.example.mydemo.view.camera;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;

import com.example.mydemo.R;
import com.example.mydemo.view.camera.exception.CameraDisabledException;
import com.example.mydemo.view.camera.exception.CameraHardwareException;
import com.example.mydemo.util.LogUtil;

import java.util.List;

public class Util {
    private static final String TAG = "Util";
    // Orientation hysteresis amount used in rounding, in degrees
    public static final int ORIENTATION_HYSTERESIS = 5;


    public static android.hardware.Camera openCamera(Activity activity) throws CameraHardwareException,
            CameraDisabledException {
        try {
            return CameraHolder.instance().open();
        } catch (CameraHardwareException e) {
            // In eng build, we throw the exception so that test tool
            // can detect it and report it
            if ("eng".equals(Build.TYPE)) {
                throw new RuntimeException("openCamera failed", e);
            } else {
                throw e;
            }
        }
    }

    public static Size getOptimalPreviewSize(Activity currentActivity, List<Size> sizes, double targetRatio) {
        // Use a very small tolerance because we want an exact match.
        final double ASPECT_TOLERANCE = 0.001;
        if (sizes == null)
            return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        // Because of bugs of overlay and layout, we sometimes will try to
        // layout the viewfinder in the portrait orientation and thus get the
        // wrong size of mSurfaceView. When we change the preview size, the
        // new overlay will be created before the old one closed, which causes
        // an exception. For now, just get the screen size

        Display display = currentActivity.getWindowManager().getDefaultDisplay();
        int targetHeight = Math.min(display.getHeight(), display.getWidth());

        if (targetHeight <= 0) {
            // We don'toast know the size of SurfaceView, use screen height
            targetHeight = display.getHeight();
        }

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio. This should not happen.
        // Ignore the requirement.
        if (optimalSize == null) {
            LogUtil.v(TAG, "No preview size match the aspect ratio");
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     * 算出相机的图像该显示的方向(CmeraInfo中设置)
     * 
     * @param degrees 屏幕显示方向的度数
     * @param cameraId 使用的相机的Id
     * @return 相机CmeraInfo中该设置的图像的方向值
     */
    public static int getDisplayOrientation(int degrees, int cameraId) {
        // See android.hardware.Camera.setDisplayOrientation for
        // documentation.
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    /**
     * 翻转相机的方向
     * 
     * @param orientation 需要翻转的度数
     * @param orientationHistory 翻转前的方向度数
     * @return 翻转后的方向度数
     */
    public static int roundOrientation(int orientation, int orientationHistory) {
        boolean changeOrientation = false;
        if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
            changeOrientation = true;
        } else {
            int dist = Math.abs(orientation - orientationHistory);
            dist = Math.min(dist, 360 - dist);
            changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
        }
        if (changeOrientation) {
            return ((orientation + 45) / 90 * 90) % 360;
        }
        return orientationHistory;
    }

    /**
     * 获得页面的图像显示的方向的int值
     * 
     * @param activity 相机页面的activity
     * @return 返回0,90,180或270
     */
    public static int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    public static void setRotationParameter(Parameters parameters, int cameraId, int orientation) {
        // See android.hardware.Camera.Parameters.setRotation for
        // documentation.
        int rotation = 0;
        if (orientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
            CameraInfo info = CameraHolder.instance().getCameraInfo()[cameraId];
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                rotation = (info.orientation - orientation + 360) % 360;
            } else { // back-facing camera
                rotation = (info.orientation + orientation) % 360;
            }
        }
        parameters.setRotation(rotation);
    }

    public static void showErrorAndFinish(final Activity activity, int msgId) {
        DialogInterface.OnClickListener buttonListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        };
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setCancelable(false).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.camera_error_title).setMessage(msgId)
                .setNeutralButton(R.string.dialog_ok, buttonListener).create();
        alertDialog.show();
    }

    /**
     * 判断是不是平板的UI
     * 
     * @param context
     * @return
     */
    public static boolean isTabletUI(Context context) {
        // TODO Auto-generated method stub
        return false;
        // 这里本来是判断是不是平板的UI，因为平板的默认方向是：ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        // 但是2.3.3不支持，所以这里先直接返回false;
        // return
        // (context.getResources().getConfiguration().smallestScreenWidthDp >=
        // 600)
    }

}
