package com.example.mydemo.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;

import com.example.mydemo.R;

import java.util.List;

/**
 * Created by ldh on 2016/4/12 0012.
 */
public class MyAdapter extends CommonAdapter<Bean> {
    private Context mContext;

    public MyAdapter(Context context, List<Bean> datas, int layoutId) {
        super(context, datas, layoutId);
        this.mContext = context;
    }

    @Override
    public void convert(final Bean bean, ViewHolder holder) {
        holder.setText(R.id.id_title, bean.getTitle())
                .setText(R.id.id_desc, bean.getDesc())
                .setText(R.id.id_time, bean.getTime())
                .setText(R.id.id_phone, bean.getPhone());
        final CheckBox cb = holder.getView(R.id.id_cb);
        //解决复用导致内容错位问题,有两个思路,
        // 1,将变化保存到javaBean中的,每次getView的时候重新设置图片,
        // 2,使用集合记录不同的position,根据不同的position设置不同的item
        cb.setChecked(bean.isChecked());
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //解决复用导致内容错乱
                bean.setChecked(cb.isChecked());
            }
        });
        holder.getView(R.id.id_phone).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String phone=bean.getPhone();
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                mContext.startActivity(phoneIntent);
            }
        });


    }
}
