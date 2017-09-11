package com.example.mydemo.view.camera.save;

import android.os.Environment;

public class ConstantsUtils {

    /**
     * 保存图片的宽度
     */
    public static final int SAVE_PHOTO_WIDTH = 800;
    /**
     * 保存图片的高度
     */
    public static final int SAVE_PHOTO_HEIGHT = 800;
    public static final String DIR_NAME = "newbroker";// 保存图像的目录名 相对"/"
    public static final String IMG_NAME = ".newbroker";
    public static final String IMG_NAME_PREFIX = "anjukebroker_pic_"; // 保存图片的名称前缀

    public static String PATH_DIR_NAME = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + DIR_NAME;
    public static String PATH_IMG_NAME = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + IMG_NAME;

}
