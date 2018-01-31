package com.example.mydemo.view.recycledemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mydemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ldh
 * @date 2017/11/19
 */
public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {
    private Context context;
    private List<Integer> datas;

    /**
     * item的点击事件的长按事件接口
     */
    private OnItemClickListener onItemClickListener;
    /**
     * 瀑布流时item随机高度
     */
    private List<Integer> heights = new ArrayList<>();
    /**
     * 不同的类型设置item不同的高度
     *
     * @param type
     */

    private int type = 0;

    public RvAdapter(Context context, List<Integer> datas) {
        this.context = context;
        this.datas = datas;
        for (int i : datas) {
            int height = (int) (Math.random() * 100 + 300);
            heights.add(height);
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 设置点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_recycle_demo, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(contentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecyclerView.LayoutParams layoutParams;
        if (type == 0) {
            layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (type == 1) {
            layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heights.get(position));
            layoutParams.setMargins(2, 2, 2, 2);
        }
        holder.itemView.setLayoutParams(layoutParams);
        holder.imageView.setImageResource(datas.get(position));
        holder.tv.setText("分类" + position);
        /**设置item点击监听**/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position, datas.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    /**
     * 用于缓存的ViewHolder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    /**
     * 设置item监听的接口
     */
    public interface OnItemClickListener {
        void onItemClickListener(int position, Integer data);

    }
}
