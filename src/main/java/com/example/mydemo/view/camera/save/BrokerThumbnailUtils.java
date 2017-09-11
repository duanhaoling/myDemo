
package com.example.mydemo.view.camera.save;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anjuke.android.newbrokerlibrary.util.DevUtil;
import com.anjuke.android.newbrokerlibrary.util.MD5Util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BrokerThumbnailUtils {
    private Context mContext;
    private static BrokerThumbnailUtils _instance = null;

    public synchronized static BrokerThumbnailUtils getInstance(Context context) {

        if (_instance == null) {
            _instance = new BrokerThumbnailUtils(context);
        }
        return _instance;
    }

    private BrokerThumbnailUtils(Context context) {
        mContext = context;
    }

    // http://stackoverflow.com/questions/2641726/decoding-bitmaps-in-android-with-the-right-size
    /**
     * 取得图片的略缩图<br>
     * <br>
     * 缓存算法：<br>
     * 以字符串maxWidth + maxHeight + filePatch拼接字符串Md5为缓存文件名<br>
     * 缓存在mContext.getCacheDir()目录中,下次取直接可取得缓存图片。<br>
     * 
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param filePath
     * @param maxWidth 0表示不限
     * @param maxHeight 0表示不限
     * @param cachePath 指定缓存图片的路径 路径结尾不要带"/"
     * @return
     */
    public String getInSampleBitmap(String filePath, int maxWidth, int maxHeight, String cachePath) {

        if (cachePath == null) {
            cachePath = mContext.getCacheDir().getAbsolutePath();
        }

        Bitmap ret = null;

        final String bitmapKey = getInSampleBitmapFileNameKey(filePath, maxWidth, maxHeight);
        final String bitmapFileNameMd5 = url2Md5FileName(bitmapKey);
        // 缓存 在特殊情况会有问题,建议每次拍照生成不同图片: 拍照在本地循环使用同一张照片保存。
        // 会产生问题，后续拍照生成的略缩图因为filePath全是返回的缓存的第一张图
        if (isFileNameInCache(bitmapFileNameMd5, cachePath)) {// 已经有缓存
            DevUtil.v("jackzhou", String.format("DiskCache hit(InSample) path:%s/%s", cachePath, bitmapFileNameMd5));
            return cachePath + "/" + bitmapFileNameMd5;
        }

        if (maxWidth == 0 && maxHeight == 0) {// 都不限制？
            ret = BitmapFactory.decodeFile(filePath);
        } else {
            if (maxWidth == 0) {
                maxWidth = -1;
            }
            if (maxHeight == 0) {
                maxHeight = -1;
            }

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, opts);
            if (opts.outWidth == -1) {// error
                return null;
            }
            int width = opts.outWidth;// 图片宽
            int height = opts.outHeight;// 图片高
            if (maxWidth >= width && maxHeight >= height) {// 略缩图比原图还大？！！
                ret = BitmapFactory.decodeFile(filePath);
            } else {
                // 计算到maxWidth的压缩比
                float inSampleSizeWidthFloat = (float) width / (float) maxWidth;
                int inSampleSizeWidth = Math.round(inSampleSizeWidthFloat);
                // 计算到maxHeight的压缩比
                float inSampleSizeHeightFloat = (float) height / (float) maxHeight;
                int inSampleSizeHeight = Math.round(inSampleSizeHeightFloat);

                int inSampleSize = Math.max(inSampleSizeWidth, inSampleSizeHeight);

                opts.inJustDecodeBounds = false;
                opts.inSampleSize = inSampleSize;
                ret = BitmapFactory.decodeFile(filePath, opts);
            }

        }
        // 缓存ret图片 不放入线程。在线程中，一些功能需要马上使用得到对应缓存图片做操作会有问题
        saveImage2Local(ret, cachePath, bitmapFileNameMd5);
        ret.recycle();
        return cachePath + "/" + bitmapFileNameMd5;
    }

    /**
     * 略缩图本地缓存时的文件名算法
     * 
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param filePath
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static String getInSampleBitmapFileNameKey(String filePath, int maxWidth, int maxHeight) {
        return "" + maxWidth + maxHeight + filePath;// filePatch需要放在最后为了适用url2Md5FileName方法
    }

    /**
     * 转化url为对应md5的文件名
     * 
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param url
     * @return
     */
    public static String url2Md5FileName(String url) {
        String ret = null;

        String tempfileNameExt = null;
        if (url == null || url.length() == 0) {
            return null;
        }
        if (url.lastIndexOf("/") != -1) {
            String tempFileName = url.substring(url.lastIndexOf("/") + 1);
            if (tempFileName.lastIndexOf(".") != -1) {
                tempfileNameExt = tempFileName.substring(tempFileName.lastIndexOf(".") + 1);
                ret = MD5Util.Md5(url) + "." + tempfileNameExt;
            } else {
                ret = MD5Util.Md5(url) + ".jpg";
            }
        }

        return ret;
    }

    /**
     * 判断图片是否在指定的cache目录中
     * 
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param
     * @return
     */
    public boolean isFileNameInCache(String fileName, String cachePath) {

        if (cachePath == null) {
            cachePath = mContext.getCacheDir().getAbsolutePath();
        }

        if (fileName == null) {
            return false;
        }

        File file = new File(cachePath, fileName);
        if (file.exists() && file.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存bitmap到sd卡
     * 
     * @deprecated 不建议public使用
     * @param bitmap 待保存的bitmap
     * @param string 待保存的目录路径
     * @param fileName 待保存的文件名
     * @return 图片路径或者null
     */
    public String saveImage2Local(Bitmap bitmap, String string, String fileName) {
        return saveImage2Local(bitmap, string, fileName, 100);
    }

    /**
     * 保存bitmap到sd卡
     * 
     * @deprecated 不建议public使用
     * @param bitmap 待保存的bitmap
     * @param path 待保存的目录路径
     * @param fileName 待保存的文件名
     * @param compress 图片压缩率
     * @return 图片路径或者null
     */
    public String saveImage2Local(Bitmap bitmap, String path, String fileName, int compress) {

        File imagePath = null;
        try {
            if (bitmap != null && !bitmap.isRecycled()) {

                File imgDir = new File(path);
                if (!imgDir.exists()) {// 如果存储的不存在，先创建
                    imgDir.mkdirs();
                }

                imagePath = new File(path, fileName);// 给新照的照片文件命名

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imagePath));

                /* 采用压缩转档方法 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, compress, bos);

                /* 调用flush()方法，更新BufferStream */
                bos.flush();

                /* 结束OutputStream */
                bos.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imagePath != null && bitmap != null) {
            return imagePath.toString();
        } else {
            return null;
        }

    }

}
