package com.example.mydemo.view.camera.save;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StatFs;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.TextUtils;

import com.anjuke.android.newbroker.R;
import com.anjuke.android.newbroker.manager.constants.ConstantsUtils;
import com.anjuke.android.newbroker.util.CommonLog;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Storage {
    static CommonLog log = new CommonLog("Storage");
    public static final long UNAVAILABLE = -1L;
    public static final long PREPARING = -2L;
    public static final long UNKNOWN_SIZE = -3L;
    public static final long LOW_STORAGE_THRESHOLD = 50000000;
    public static final long PICTURE_SIZE = 1500000;
    private static ImageFileNamer sImageFileNamer;
    // public static final String DCIM =
    // Environment.getExternalStorageDirectory().toString() + "/DCIM";

    // public static final String DIRECTORY = DCIM + "/ACamera";
    public static final String DIRECTORY = ConstantsUtils.PATH_DIR_NAME;

    // Match the code in MediaProvider.computeBucketValues().
    public static final String BUCKET_ID = String.valueOf(DIRECTORY.toLowerCase().hashCode());

    public static long getAvailableSpace() {
        String state = Environment.getExternalStorageState();
        log.d("External storage state=" + state);
        if (Environment.MEDIA_CHECKING.equals(state)) {
            return PREPARING;
        }
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return UNAVAILABLE;
        }

        // log.v(DIRECTORY);
        File dir = new File(DIRECTORY);
        dir.mkdirs();
        if (!dir.isDirectory() || !dir.canWrite()) {
            return UNAVAILABLE;
        }

        try {
            StatFs stat = new StatFs(DIRECTORY);
            return stat.getAvailableBlocks() * (long) stat.getBlockSize();
        } catch (Exception e) {
            log.i("Fail to access external storage***" + e);
        }
        return UNKNOWN_SIZE;
    }

    public static String generateFilepath(String title) {
        return DIRECTORY + '/' + title + ".jpg";
    }

    public static String generateFilepath(String title, String imageFormat) {
        if (!TextUtils.isEmpty(imageFormat)) {
            return DIRECTORY + '/' + title + ".jpg";
        } else {
            if(".png".equalsIgnoreCase(imageFormat)) {
                return DIRECTORY + '/' + title + ".png";
            } else if(".jpeg".equalsIgnoreCase(imageFormat)){
                return DIRECTORY + '/' + title + ".jpeg";
            }
        }
        return DIRECTORY + '/' + title + ".jpg";
    }

    /**
     * 真正的处理执行拍照动作后的逻辑，包括保存原图，缩略图
     * 
     * @param resolver
     * @param title 根据拍照的时间定制出来的图片名字
     * @param date 拍照的时间
     * @param jpeg 照片的数据流
     * @param width 照片的宽
     * @param height 照片的高
     * @return
     */
    public static Uri addImage(ContentResolver resolver, String title, long date, byte[] jpeg, int width, int height) {
        // Save the image.
        String path = generateFilepath(title);
        // log.v(path);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(jpeg);
        } catch (Exception e) {
            log.e("Failed to write image" + e);
            return null;
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
        // Insert into MediaStore.
        ContentValues values = new ContentValues(9);
        values.put(ImageColumns.TITLE, title);
        values.put(ImageColumns.DISPLAY_NAME, title + ".jpg");
        values.put(ImageColumns.DATE_TAKEN, date);
        values.put(ImageColumns.MIME_TYPE, "image/jpeg");
        values.put(ImageColumns.DATA, path);
        values.put(ImageColumns.SIZE, jpeg.length);
        // values.put(ImageColumns.WIDTH, width);
        // values.put(ImageColumns.HEIGHT, height);

        Uri uri = null;
        try {
            uri = resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Throwable th) {
            // This can happen when the external volume is already mounted, but
            // MediaScanner has not notify MediaProvider to add that volume.
            // The picture is still safe and MediaScanner will find it and
            // insert it into MediaProvider. The only problem is that the user
            // cannot click the thumbnail to review the picture.
            log.e("Failed to write MediaStore" + th.toString());
        }
        return uri;

    }

    public static String createJpegName(long dateTaken, Context context) {
        if (sImageFileNamer == null) {
            sImageFileNamer = new ImageFileNamer(context.getString(R.string.image_file_name_format));
        }
        synchronized (sImageFileNamer) {
            return sImageFileNamer.generateName(dateTaken);
        }
    }

    public static void closeSilently(Closeable c) {
        if (c == null)
            return;
        try {
            c.close();
        } catch (Throwable t) {
            // do nothing
        }
    }

    public static void broadcastNewPicture(Context context, Uri uri) {
        // Keep compatibility
        context.sendBroadcast(new Intent("com.android.camera.NEW_PICTURE", uri));
    }
    public static void broadcastScanPicture(Context context, Uri uri) {
        // Keep compatibility
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }

    public static boolean isUriValid(Uri uri, ContentResolver resolver) {
        if (uri == null)
            return false;

        try {
            ParcelFileDescriptor pfd = resolver.openFileDescriptor(uri, "r");
            if (pfd == null) {
                log.e("Fail to open URI. URI=" + uri);
                return false;
            }
            pfd.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
