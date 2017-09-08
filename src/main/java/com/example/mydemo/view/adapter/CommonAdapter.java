package com.example.mydemo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by ldh on 2016/4/12 0012.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected int mItemLayout;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mDatas = datas;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mItemLayout = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mItemLayout, position);

        convert(getItem(position), holder);

        return holder.getConvertView();
    }

    protected abstract void convert(T t, ViewHolder holder);
}
