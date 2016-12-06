package com.example.mydemo.camera;

import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;

import com.example.mydemo.camera.exception.CameraHardwareException;


public class CameraHolder {
    private android.hardware.Camera mCameraDevice;
    private Parameters mParameters;
    private CameraInfo[] mInfo;

    private int mNumberOfCameras;
    private int mCameraId = -1; // current camera id
    private int mBackCameraId = -1, mFrontCameraId = -1;
    // 单例模式.
    private static CameraHolder sHolder;

    private CameraHolder() {
        mNumberOfCameras = android.hardware.Camera.getNumberOfCameras();
        mInfo = new CameraInfo[mNumberOfCameras];
        for (int i = 0; i < mNumberOfCameras; i++) {
            mInfo[i] = new CameraInfo();
            android.hardware.Camera.getCameraInfo(i, mInfo[i]);
            if (mBackCameraId == -1 && mInfo[i].facing == CameraInfo.CAMERA_FACING_BACK) {
                mBackCameraId = i;
            }
            if (mFrontCameraId == -1 && mInfo[i].facing == CameraInfo.CAMERA_FACING_FRONT) {
                mFrontCameraId = i;
            }
        }
    }

    public static synchronized CameraHolder instance() {
        if (sHolder == null) {
            sHolder = new CameraHolder();
        }
        return sHolder;
    }

    public synchronized void release() {
        if (mCameraDevice != null) {
            mCameraDevice.setErrorCallback(null);
            mCameraDevice.stopPreview();
            mCameraDevice.release();
            mCameraDevice = null;
            mParameters = null;
        }
    }

    public synchronized android.hardware.Camera open()
            throws CameraHardwareException {
        if (mCameraDevice == null) {
            try {
                mCameraDevice = android.hardware.Camera.open();
                mParameters = mCameraDevice.getParameters();
            } catch (Exception e) {
                throw new CameraHardwareException(e);
            }
        } else {
            mParameters = mCameraDevice.getParameters();
            mCameraDevice.setParameters(mParameters);
        }
        return mCameraDevice;
    }

    public CameraInfo[] getCameraInfo() {
        // TODO Auto-generated method stub
        return mInfo;
    }
}
