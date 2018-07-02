package com.example.mydemo.image_picker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mydemo.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 从相册选择图片页-文件
 * 支持尺寸限制
 */
public class ImageGridFragment extends Fragment {


    private GridView mGridView;
    private ImagesGridAdapter mAdapter;
    private SelectPicListener mListener;

    private int maxNum;
    private int bucketId;
    private LimitPic limitPic;

    public interface SelectPicListener {
        void onSelectPicsListener(List<LocalImage> imageList);
    }

    public static ImageGridFragment newInstance(@NonNull LocalImageFolder localImageFolder, int maxNum) {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle bundle = new Bundle(2);
        bundle.putInt("maxNum", maxNum);
        bundle.putInt("bucketId", localImageFolder.getBucket_id());
        fragment.setArguments(bundle);

        return fragment;
    }

    //在图片选择页增加限制图片大小的校验
    public static ImageGridFragment newInstance(@NonNull LocalImageFolder localImageFolder, int maxNum, LimitPic limitPic) {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle bundle = new Bundle(2);
        bundle.putInt("maxNum", maxNum);
        bundle.putInt("bucketId", localImageFolder.getBucket_id());
        bundle.putSerializable("limitPic", limitPic);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SelectPicListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        if (null != bundle) {
            maxNum = bundle.getInt("maxNum", 1);
            bucketId = bundle.getInt("bucketId", 0);
            limitPic = (LimitPic) bundle.getSerializable("limitPic");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_grid, container, false);
        mGridView = view.findViewById(R.id.gridView);
        mGridView.setAdapter(mAdapter = new ImagesGridAdapter());
        mGridView.setOnItemClickListener((parent, view1, position, id) -> {
            int state = mAdapter.changeSelection(view1, position);
            if (state == 0) {
                Toast.makeText(getContext(), "最多只能选择" + maxNum + "张图片", Toast.LENGTH_SHORT).show();
            } else if (state == 1) {
                Toast.makeText(getContext(), "图片的尺寸不能小于" + limitPic.minWidth + "x" + limitPic.minHeight, Toast.LENGTH_SHORT).show();
            } else {
                updateTitle();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter.updateData(getPhotos(bucketId), maxNum, limitPic);
        updateTitle();
    }

    private void updateTitle() {
        getActivity().setTitle("选择图片(" + mAdapter.getSelectedCount() + "/" + maxNum + ")");
    }

    public void updateData(int bucketId, int maxNum) {
        this.bucketId = bucketId;
        this.maxNum = maxNum;
        mAdapter.updateData(getPhotos(bucketId), maxNum, limitPic);
        updateTitle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add(0, 0x100, 0, "确定");
        MenuItemCompat.setShowAsAction(item, MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0x100:
                if (mAdapter.getSelectedCount() > maxNum) {
                    Toast.makeText(getContext(), "最多只能选择" + maxNum + "张图片", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.onSelectPicsListener(mAdapter.getSelectedList());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 根据bucketId获取图片
     *
     * @param bucketId
     * @return
     */
    private List<LocalImage> getPhotos(int bucketId) {
        String[] columns = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
                MediaStore.Images.Media.DISPLAY_NAME, // 图片文件名
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.WIDTH,//宽度
                MediaStore.Images.Media.HEIGHT,//高度
        };
        String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                MediaStore.Images.Media.BUCKET_ID + "=" + bucketId + " AND " + MediaStore.Images.Media.SIZE + " > 500",
                null, orderBy + " desc");
        if (cursor == null) return null;
        List<LocalImage> galleryList = null;
        try {
            galleryList = new ArrayList<>(cursor.getCount());

            int folderIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int folderColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int fileIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int fileNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int pathColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int widthColumn = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
            int heightColumn = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);
            while (cursor.moveToNext()) {
                LocalImage localImage = new LocalImage();
                localImage.setBucket_display_name(cursor.getString(folderColumn));
                localImage.setBucket_id(cursor.getInt(folderIdColumn));
                localImage.setDisplay_name(cursor.getString(fileNameColumn));
                localImage.setData(cursor.getString(pathColumn));
                localImage.setId(cursor.getInt(fileIdColumn));
                localImage.setWidth(cursor.getInt(widthColumn));
                localImage.setHeight(cursor.getInt(heightColumn));
                galleryList.add(localImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return galleryList;
    }

    private class ImagesGridAdapter extends BaseAdapter {

        private List<LocalImage> list;
        private int maxCount = 1;//最大个数
        private List<LocalImage> selectedList = new ArrayList<>();//已选
        private LimitPic limitPic;

        public void updateData(List<LocalImage> list, int maxCount, LimitPic limitPic) {
            this.list = list;
            this.maxCount = maxCount;
            this.limitPic = limitPic;
            this.selectedList.clear();
            notifyDataSetChanged();
        }

        public int getSelectedCount() {
            return selectedList.size();
        }

        public List<LocalImage> getSelectedList() {
            return selectedList;
        }


        public int changeSelection(View v, int position) {
            if (selectedList.size() >= maxCount && !((ViewHolder) v.getTag()).cb.isChecked()) {
                return 0;
            } else {
                LocalImage item = getItem(position);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(item.getData(), opts);
                opts.inJustDecodeBounds = false;
                if (limitPic != null && (opts.outWidth < limitPic.minWidth || opts.outHeight < limitPic.minHeight)) {
                    return 1;
                } else {
                    item.setSelected(!item.isSelected());
                    ((ViewHolder) v.getTag()).cb.setChecked(item.isSelected());
                    if (item.isSelected()) {
                        selectedList.add(item);
                    } else {
                        selectedList.remove(item);
                    }
                    return 2;
                }
            }
        }

        @Override
        public int getCount() {
            if (null == list) return 0;
            return list.size();
        }

        @Override
        public LocalImage getItem(int position) {
            if (null == list || list.isEmpty()) return null;
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_image, parent, false);
                holder.iv = (ImageView) convertView.findViewById(R.id.item_image);
                holder.cb = (CheckBox) convertView.findViewById(R.id.imgQueueMultiSelected);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            LocalImage item = getItem(position);
            if (null != item) {
                Glide.with(ImageGridFragment.this).load("file://" + item.getData()).into(holder.iv);
                holder.cb.setChecked(item.isSelected());
            }

            return convertView;
        }

        class ViewHolder {
            ImageView iv;
            CheckBox cb;
        }
    }


}
