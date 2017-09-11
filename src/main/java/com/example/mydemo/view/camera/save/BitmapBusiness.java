
package com.example.mydemo.view.camera.save;

import android.content.Context;

import com.example.mydemo.AppContext;

public class BitmapBusiness {

    /**
     * 描述 生成指定路径图片的缩略图到sd卡上的.anjukeBroker目录
     * 
     * @param context 上下文
     * @param originalPicPath 原始拍照好的大图的路径
     * @return 生成的缩略图的路径
     */
    private static String generateThumbnail(Context context, String originalPicPath) {
        String targetPicPath = BrokerThumbnailUtils.getInstance(context).getInSampleBitmap(originalPicPath,
                ConstantsUtils.SAVE_PHOTO_WIDTH,
                ConstantsUtils.SAVE_PHOTO_HEIGHT, ConstantsUtils.PATH_IMG_NAME);
        return targetPicPath;
    }

    public static String getTargetBitmap(String originalPicPath) {
        String targetBitMap = generateThumbnail(AppContext.getInstance(), originalPicPath);
        addLatLngInfomation(AppContext.getInstance(), targetBitMap);
        return targetBitMap;
    }

    /**
     * 描述 给指定路径的图片的Exif信息加上经纬度信息
     * 
     * @param context 上下文
     * @param thumbnailPicPath 我们压缩好大小用来上传到图片服务器上的图片的路径
     */
    private static void addLatLngInfomation(Context context, String thumbnailPicPath) {
        // if (LocationService.libGetLng() != null &&
        // LocationService.libGetLat() != null) {
        // ImageHelper
        // .getInstance(context)
        // .initExif(thumbnailPicPath)
        // .setExifValue(ConstantsUtils.SAVE_PHOTO_LNG,
        // Double.toString(LocationService.libGetLng()))
        // .setExifValue(ConstantsUtils.SAVE_PHOTO_LAT,
        // Double.toString(LocationService.libGetLat()))
        // .commitExif();
        // }
    }

}
