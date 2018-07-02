package com.example.mydemo.image_picker.clip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.mydemo.R;
import com.example.mydemo.image_picker.BaseImage;
import com.example.mydemo.image_picker.ImageGridActivity;
import com.ldh.androidlib.base.BaseActivity;
import com.ldh.androidlib.utils.PermissionUtils;
import com.ldh.androidlib.view.ClipImageView;
import com.ldh.androidlib.view.ClipView;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClipActivity extends BaseActivity {

    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_GALLARY = 2;
    public static final String KEY_CLIP_CACHE_URL = "bitmapurl";
    private static final int PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;
    @Bind(R.id.clipview)
    ClipView clipview;
    @Bind(R.id.src_pic)
    ClipImageView imageView;

    private File cacheFile;
    private int width;
    private int height;
    private String msg1;
    private String msg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        width = getIntent().getIntExtra("width", 0);
        height = getIntent().getIntExtra("height", 0);
        int requestCode = getIntent().getIntExtra("requestCode", 1);
        msg1 = getString(R.string.permission_tips, getString(R.string.app_name), getString(R.string.permission_camera));
        msg2 = getString(R.string.permission_tips, getString(R.string.app_name), getString(R.string.permission_storage));
        switch (requestCode) {
            case REQUEST_CAMERA:
                checkCamera();
                break;
            case REQUEST_GALLARY:
                checkeStorage();
                break;
        }
    }

    private void gotoImages() {
        ImageGridActivity.start(ClipActivity.this, 1, REQUEST_GALLARY);
    }

    private void gotoCamera() {
        Intent i1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cacheFile = new File(getPath());
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            Uri imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", cacheFile);
            i1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            i1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cacheFile));
        }
        startActivityForResult(i1, REQUEST_CAMERA);
    }

    private void checkCamera() {
        if (PermissionUtils.checkActivityPermisssion(this, Manifest.permission.CAMERA, msg1,
                PERMISSIONS_REQUEST_CAMERA, true)) {
            gotoCamera();
        }
    }

    private void checkeStorage() {
        if (PermissionUtils.checkActivityPermisssion(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                msg2, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, true)) {
            gotoImages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        checkCamera();
                    } else {
                        PermissionUtils.createPermissionDialog(this, msg1, true);
                    }
                }
                break;
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        checkeStorage();
                    } else {
                        PermissionUtils.createPermissionDialog(this, msg2, true);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            finish();
            return;
        }
        setContentView(R.layout.activity_clip);
        ButterKnife.bind(this);
        clipview.set(width, height);
        imageView.set(width, height);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (null != cacheFile) {
                    onOk("file://" + cacheFile.getAbsolutePath());
                }
                break;
            case REQUEST_GALLARY:
                if (null != data) {
                    onGallaryOk(data);
                }
                break;
        }
    }

    private String getPath() {
        return getExternalCacheDir() != null ?
                getExternalCacheDir().getAbsolutePath() + "/" + System.currentTimeMillis() :
                getCacheDir().getAbsolutePath() + "/" + System.currentTimeMillis();
    }

    private void onOk(String path) {
        imageView.setImageResource(android.R.drawable.progress_indeterminate_horizontal);
//        ImageManager.loadBitmap(path, width, height, new ImageLoadListener() {
//            @Override
//            public void onSuccess(@Nullable ImageView targetView, @Nullable String url, @Nullable Bitmap bitmap) {
//                imageView.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void onFailed(@Nullable ImageView targetView, @Nullable String url) {
//            }
//        });

        Glide.with(this).load(path).into(imageView);


    }

    private void onGallaryOk(Intent data) {
        ArrayList<BaseImage> mList = data.getParcelableArrayListExtra("images");
        if (mList.size() == 0) {
            return;
        }
        String imgUrl = mList.get(0).getImgUrl();
        onOk("file://" + imgUrl);
    }

    @OnClick(R.id.tv_clip_cancel)
    void onCancel() {
        finish();
    }

    @OnClick(R.id.tv_clip_xuanqu)
    void onXuanqu() {
        showProgressDialog("剪裁图片中……");
        new Thread() {
            public void run() {
                final byte[] bs = ImageHelper.getInstance(ClipActivity.this).bitmap2Byte(imageView.clip());
                File file = new File(getPath());
                ImageHelper.getInstance(ClipActivity.this).byte2File(bs, file.getAbsolutePath());
                String url = file.getAbsolutePath();
                runOnUiThread(new Runnable() {
                    public void run() {
                        dismissProgressDialog();
                        Intent intent = new Intent();
                        intent.putExtra(KEY_CLIP_CACHE_URL, url);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cacheFile != null) {
            if (cacheFile.exists()) {
                cacheFile.delete();
            }
            cacheFile = null;
        }
    }


}
