package com.example.mydemo.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ldh on 2016/4/12 0012.
 */
public class ViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mViews = new SparseArray<View>();
        mConvertView.setTag(this);
    }

    /**
     * 获取ViewHoder对象,如果convertView不为空,直接复用
     * @param context
     * @param ConvertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View ConvertView, ViewGroup parent, int layoutId, int position) {
        if (ConvertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) ConvertView.getTag();
            //虽然再次进来holder复用了,但是每次都要更新位置
            holder.mPosition = position;
            return holder;
        }
    }

    /**
     * 返回converView
     * @return
     */
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 返回holder的item索引
     * @return
     */
    public int getPosition() {
        return mPosition;
    }

    /**
     * 通过viewId获取控件
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置TextView的值
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId,String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setImageResource(int viewId,int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }


    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setImageURL(int viewId,String url){
        ImageView iv = getView(viewId);

//        iv.setImageResource(resId);
        return this;
    }
}
