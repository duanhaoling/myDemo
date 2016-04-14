package com.example.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.example.myapplication.R;

import java.util.List;

/**
 * Created by ldh on 2016/4/12 0012.
 */
public class MyAdapter extends CommonAdapter<Bean> {


    public MyAdapter(Context context, List<Bean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(final Bean bean, ViewHolder holder) {
        holder.setText(R.id.id_title, bean.getTitle())
                .setText(R.id.id_desc, bean.getDesc())
                .setText(R.id.id_time, bean.getTime())
                .setText(R.id.id_phone, bean.getPhone());
        final CheckBox cb = holder.getView(R.id.id_cb);
//        cb.setChecked(bean.isChecked());
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bean.setChecked(cb.isChecked());
            }
        });

    }
}
