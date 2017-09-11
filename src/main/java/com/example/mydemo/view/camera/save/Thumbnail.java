/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mydemo.view.camera.save;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;

import com.anjuke.android.newbrokerlibrary.util.DevUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Thumbnail {
    private static final String TAG = "Thumbnail";

    public static final String LAST_THUMB_FILENAME = "last_thumb";
    private static final int BUFSIZE = 4096;

    private Uri mUri;
    private Bitmap mBitmap;
    // whether this thumbnail is read from file
    private boolean mFromFile = false;

    // Camera, VideoCamera, and Panorama share the same thumbnail. Use sLock
    // to serialize the access.
    private static Object sLock = new Object();

    public Thumbnail(Uri uri, Bitmap bitmap, int orientation) {
        mUri = uri;
        mBitmap = rotateImage(bitmap, orientation);
        if (mBitmap == null)
            throw new IllegalArgumentException("null bitmap");
    }

    public Uri getUri() {
        return mUri;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setFromFile(boolean fromFile) {
        mFromFile = fromFile;
    }

    public boolean fromFile() {
        return mFromFile;
    }

    private static Bitmap rotateImage(Bitmap bitmap, int orientation) {
        if (orientation != 0) {
            // We only rotate the thumbnail once even if we get OOM.
            Matrix m = new Matrix();
            m.setRotate(orientation, bitmap.getWidth() * 0.5f, bitmap.getHeight() * 0.5f);

            try {
                Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                // If the rotated bitmap is the original bitmap, then it
                // should not be recycled.
                if (rotated != bitmap)
                    bitmap.recycle();
                return rotated;
            } catch (Throwable t) {
                DevUtil.w(TAG, "Failed to rotate thumbnail", t);
            }
        }
        return bitmap;
    }

    public String saveImage2Local(File file, int compress) {
        try {
            if (mBitmap != null && !mBitmap.isRecycled()) {

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file), BUFSIZE);

                /* 采用压缩转档方法 */
                mBitmap.compress(Bitmap.CompressFormat.JPEG, compress, bos);

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

        if (mBitmap != null) {
            return file.toString();
        } else {
            return null;
        }

    }

    // Stores the bitmap to the specified file.
    public void saveTo(File file) {
        FileOutputStream f = null;
        BufferedOutputStream b = null;
        DataOutputStream d = null;
        synchronized (sLock) {
            try {
                f = new FileOutputStream(file);
                b = new BufferedOutputStream(f, BUFSIZE);
                d = new DataOutputStream(b);
                d.writeUTF(mUri.toString());
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, d);
                d.close();
            } catch (IOException e) {
                DevUtil.e(TAG, "Fail to store bitmap. path=" + file.getPath(), e);
            } finally {
                Storage.closeSilently(f);
                Storage.closeSilently(b);
                Storage.closeSilently(d);
            }
        }
    }

    // Loads the data from the specified file.
    // Returns null if failure.
    public static Thumbnail loadFrom(File file) {
        Uri uri = null;
        Bitmap bitmap = null;
        FileInputStream f = null;
        BufferedInputStream b = null;
        DataInputStream d = null;
        synchronized (sLock) {
            try {
                f = new FileInputStream(file);
                b = new BufferedInputStream(f, BUFSIZE);
                d = new DataInputStream(b);
                uri = Uri.parse(d.readUTF());
                bitmap = BitmapFactory.decodeStream(d);
                d.close();
            } catch (IOException e) {
                DevUtil.d(TAG, "Fail to load bitmap. " + e);
                return null;
            } finally {
                Storage.closeSilently(f);
                Storage.closeSilently(b);
                Storage.closeSilently(d);
            }
        }
        Thumbnail thumbnail = createThumbnail(uri, bitmap, 0);
        if (thumbnail != null)
            thumbnail.setFromFile(true);
        return thumbnail;
    }

    public static Thumbnail getLastThumbnail(ContentResolver resolver) {
        Media image = getLastImageThumbnail(resolver);
        if (image == null)
            return null;

        Bitmap bitmap = null;
        Media lastMedia;
        bitmap = Images.Thumbnails.getThumbnail(resolver, image.id, Images.Thumbnails.MINI_KIND, null);
        lastMedia = image;

        // Ensure database and storage are in sync.
        if (Storage.isUriValid(lastMedia.uri, resolver)) {
            return createThumbnail(lastMedia.uri, bitmap, lastMedia.orientation);
        }
        return null;
    }

    public static Media getLastImageThumbnail(ContentResolver resolver) {
        Uri baseUri = Images.Media.EXTERNAL_CONTENT_URI;

        Uri query = baseUri.buildUpon().appendQueryParameter("limit", "1").build();
        String[] projection = new String[] {
                ImageColumns._ID, ImageColumns.ORIENTATION, ImageColumns.DATE_TAKEN
        };
        String selection = ImageColumns.MIME_TYPE + "='image/jpeg' AND " + ImageColumns.BUCKET_ID + '='
                + Storage.BUCKET_ID;
        String order = ImageColumns.DATE_TAKEN + " DESC," + ImageColumns._ID + " DESC";

        Cursor cursor = null;
        try {
            cursor = resolver.query(query, projection, selection, null, order);
            if (cursor != null && cursor.moveToFirst()) {
                long id = cursor.getLong(0);
                cursor.close();
                return new Media(id, cursor.getInt(1), cursor.getLong(2), ContentUris.withAppendedId(baseUri, id));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private static class Media {
        public Media(long id, int orientation, long dateTaken, Uri uri) {
            this.id = id;
            this.orientation = orientation;
            this.dateTaken = dateTaken;
            this.uri = uri;
        }

        public final long id;
        public final int orientation;
        public final long dateTaken;
        public final Uri uri;
    }

    // public static Media getLastImageThumbnail(ContentResolver resolver) {
    // Uri baseUri = Images.Media.EXTERNAL_CONTENT_URI;
    //
    // Uri query = baseUri.buildUpon().appendQueryParameter("limit",
    // "1").build();
    // String[] projection = new String[] {ImageColumns._ID,
    // ImageColumns.ORIENTATION,
    // ImageColumns.DATE_TAKEN};
    // String selection = ImageColumns.MIME_TYPE + "='image/jpeg' AND " +
    // ImageColumns.BUCKET_ID + '=' + Storage.BUCKET_ID;
    // String order = ImageColumns.DATE_TAKEN + " DESC," + ImageColumns._ID +
    // " DESC";
    //
    // Cursor cursor = null;
    // try {
    // cursor = resolver.query(query, projection, selection, null, order);
    // if (cursor != null && cursor.moveToFirst()) {
    // long id = cursor.getLong(0);
    // return new Media(id, cursor.getInt(1), cursor.getLong(2),
    // ContentUris.withAppendedId(baseUri, id));
    // }
    // } finally {
    // if (cursor != null) {
    // cursor.close();
    // }
    // }
    // return null;
    // }

    public static Thumbnail createThumbnail(byte[] jpeg, int orientation, int inSampleSize, Uri uri) {
        // Create the thumbnail.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length, options);
        // Bitmap bitmap2 =ThumbnailUtils.extractThumbnail(bitmap,
        // ConstantsUtils.SAVE_PHOTO_WIDTH, ConstantsUtils.SAVE_PHOTO_HEIGHT);
        // //将生成的缩略图保存为正方形
        // bitmap.recycle();
        return createThumbnail(uri, bitmap, orientation);
    }

    private static Thumbnail createThumbnail(Uri uri, Bitmap bitmap, int orientation) {
        if (bitmap == null) {
            DevUtil.e(TAG, "Failed to create thumbnail from null bitmap");
            return null;
        }
        try {
            return new Thumbnail(uri, bitmap, orientation);
        } catch (IllegalArgumentException e) {
            DevUtil.e(TAG, "Failed to construct thumbnail", e);
            return null;
        }
    }
}
