package com.example.mydemo.image_picker;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mydemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 从相册选择图片页-文件夹
 *
 * The main fragment that powers the ImageGridActivity screen. Fairly straight
 * forward GridView implementation with the key addition being the ImageWorker
 * class w/ImageCache to load children asynchronously, keeping the UI nice and
 * smooth and caching thumbnails for quick retrieval. The cache is retained over
 * configuration changes like orientation change so the images are populated
 * quickly if, for example, the user rotates the device.
 */
public class ImageFolderGridFragment extends Fragment {
    private ImageAdapter mAdapter;
    private SelectFolderListener mListener;
    private List<LocalImageFolder> mFolderList;

    public interface SelectFolderListener {
        void onSelectFolder(LocalImageFolder folder);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (SelectFolderListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_folder_grid, container, false);
        GridView gridView = view.findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter = new ImageAdapter(getContext()));
        gridView.setOnItemClickListener((parent, view1, position, id) -> mListener.onSelectFolder(mFolderList.get(position)));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryImageFolders();
    }

    /**
     * 查询sd卡上的图片，并以文件夹归类
     */
    private void queryImageFolders() {
        String[] projection = new String[]{MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
                MediaStore.Images.Media.DISPLAY_NAME, // 图片文件名
                MediaStore.Images.Media.DATA// 图片绝对路径
        };
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " asc");
        if (cursor == null) return;
        try {
            mFolderList = new ArrayList<>(cursor.getCount());
            SparseIntArray mFolderImgNum = new SparseIntArray(cursor.getCount());
            int fileIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int folderIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int folderColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int fileNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int pathColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            while (cursor.moveToNext()) {
                LocalImage localImage = new LocalImage();
                localImage.setBucket_display_name(cursor.getString(folderColumn));
                localImage.setBucket_id(cursor.getInt(folderIdColumn));
                localImage.setDisplay_name(cursor.getString(fileNameColumn));
                localImage.setData(cursor.getString(pathColumn));
                localImage.setId(cursor.getInt(fileIdColumn));

                //获取此文件夹下的图片数
                int num = mFolderImgNum.get(localImage.getBucket_id());
                //若此文件夹下的图片数为0，说明是首次遍历到此图片，则新建对应的文件夹对象
                if (num == 0) {
                    LocalImageFolder localImageFolder = new LocalImageFolder(localImage);
                    mFolderList.add(localImageFolder);
                }
                //设置文件夹的bucket_id以及内部的图片数
                mFolderImgNum.put(localImage.getBucket_id(), ++num);
            }
            mAdapter.updateData(mFolderList, mFolderImgNum);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }


    private static class ImageAdapter extends BaseAdapter {

        private List<LocalImageFolder> list;
        private SparseIntArray folderImgNum;
        private Context context;

        public ImageAdapter(Context context) {
            this.context = context;
        }

        public void updateData(List<LocalImageFolder> list, SparseIntArray folderImgNum) {
            this.list = list;
            this.folderImgNum = folderImgNum;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (null == list) return 0;
            return list.size();
        }

        @Override
        public LocalImageFolder getItem(int position) {
            if (null == list || list.isEmpty()) return null;
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_image_folder, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.imgQueue);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.image_photo_folder_title_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            LocalImageFolder item = getItem(position);
            if (null != item) {
                Glide.with(context)
                        .load("file://" + item.getData())
                        .into(viewHolder.iv);
                viewHolder.tv.setText(item.getBucket_display_name() + "(" + folderImgNum.get(item.getBucket_id()) + ")");
            }

            return convertView;
        }

        private class ViewHolder {
            ImageView iv;
            TextView tv;
        }
    }


}
