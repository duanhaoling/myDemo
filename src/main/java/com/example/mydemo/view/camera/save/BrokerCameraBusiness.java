package com.example.mydemo.view.camera.save;

import android.app.Activity;
import android.content.Intent;

import com.anjuke.android.newbroker.manager.constants.ConstantsUtils;

import java.util.ArrayList;

public class BrokerCameraBusiness {
    public static final String DIRECTORY_THUMB = ConstantsUtils.PATH_IMG_NAME;
    private ArrayList<String> originalImagesPaths = new ArrayList<String>();

    /**
     * 描述:在拍照后需要做的事情。修改逻辑时候，只需要修改这里
     * 
     * @param originalPicPath 拍照拍出来的照片的存储路径
     */
    public void onAddImage(String originalPicPath) {
        originalImagesPaths.add(originalPicPath);
    }

    public void onClickOKBtn(Activity activity) {
        Intent mIntent = activity.getIntent();
        mIntent.putStringArrayListExtra("images", originalImagesPaths);
        activity.setResult(Activity.RESULT_OK, mIntent);
        activity.finish();
    }

    public void onClickCancleBtn(Activity cameraActivity) {
    }

    public void onGalleryItemClick(int position) {
        if (position < originalImagesPaths.size()) {
            originalImagesPaths.remove(position);
        }
    }
}
