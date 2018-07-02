package com.example.mydemo.image_picker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.mydemo.R;
import com.ldh.androidlib.utils.PermissionConstant;
import com.ldh.androidlib.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 从相册选择图片页
 */
public class ImageGridActivity extends AppCompatActivity  implements ImageFolderGridFragment.SelectFolderListener, ImageGridFragment.SelectPicListener {
    protected final String TAG = this.getClass().getSimpleName();
    private static final String EXTRA_MAX_NUM = "maxNum";
    //可选择的最大图片数
    private int maxNum;
    private ImageFolderGridFragment folderFragment;
    private ImageGridFragment imgFragment;
    private LimitPic limitPic;

    /**
     * 启动相册选择页
     *
     * @param activity
     * @param maxNum      最大可选择图片数
     * @param requestCode
     */
    public static void start(Activity activity, int maxNum, int requestCode) {
        Intent intent = new Intent(activity, ImageGridActivity.class);
        intent.putExtra(EXTRA_MAX_NUM, maxNum);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Activity activity, int maxNum, int requestCode, LimitPic limitPic) {
        Intent intent = new Intent(activity, ImageGridActivity.class);
        intent.putExtra(EXTRA_MAX_NUM, maxNum);
        intent.putExtra("limitPic", limitPic);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkStoragePermission()) {
            init();
        }
    }


    private boolean checkStoragePermission() {
        String msg = getString(R.string.permission_tips, getString(R.string.app_name), getString(R.string.permission_storage));
        return PermissionUtils.checkActivityPermisssion(this, Manifest.permission.READ_EXTERNAL_STORAGE, msg,
                PermissionConstant.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionConstant.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    String msg = getString(R.string.permission_tips, getString(R.string.app_name), getString(R.string.permission_storage));
                    PermissionUtils.createPermissionDialog(this, msg, true);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void init() {
        maxNum = getIntent().getIntExtra(EXTRA_MAX_NUM, 1);
        limitPic = (LimitPic) getIntent().getSerializableExtra("limitPic");
        if (null == folderFragment) {
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(android.R.id.content, folderFragment = new ImageFolderGridFragment(), TAG)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .show(folderFragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onSelectFolder(LocalImageFolder folder) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //点击文件夹后隐藏folderFragment
        if (null != folderFragment) {
            transaction.hide(folderFragment);
        }
        if (null == imgFragment) {
            if (limitPic == null) {
                transaction.add(android.R.id.content, imgFragment = ImageGridFragment.newInstance(folder, maxNum))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            } else {
                transaction.add(android.R.id.content, imgFragment = ImageGridFragment.newInstance(folder, maxNum, limitPic))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            }

        } else {
            imgFragment.updateData(folder.getBucket_id(), maxNum);
            transaction.show(imgFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onSelectPicsListener(List<LocalImage> imageList) {
        ArrayList<BaseImage> baseImageList = new ArrayList<>();
        if (imageList != null && !imageList.isEmpty()) {
            for (LocalImage localImage : imageList) {
                BaseImage baseImage = new BaseImage();
                baseImage.setImgUrl(localImage.getData());
                baseImageList.add(baseImage);
            }
        }
        Intent i = getIntent();
        i.putParcelableArrayListExtra("images", baseImageList);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (null != imgFragment && imgFragment.isVisible() && null != folderFragment && folderFragment.isHidden()) {
            setTitle("选择图片");
            getSupportFragmentManager().beginTransaction()
                    .hide(imgFragment)
                    .show(folderFragment)
                    .commitAllowingStateLoss();
        } else {
            super.onBackPressed();
        }
    }


}
