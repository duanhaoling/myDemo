package com.example.mydemo.viewpager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydemo.R;

import java.util.ArrayList;

/**
 * desc:
 * Created by ldh on 2018/8/2.
 */
public class ViewPagerDemoAdapter extends PagerAdapter {
    private Activity activity;

    private ArrayList<Integer> images;

    public ViewPagerDemoAdapter(Activity activity) {
        this.activity = activity;
        this.images = new ArrayList<>();
    }


    public void setImages(ArrayList<Integer> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public void addImage(int imgRes) {
        this.images.add(imgRes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = View.inflate(container.getContext(), R.layout.layout_pager_demo_item, null);
        ImageView iv = itemView.findViewById(R.id.iv);
        TextView tv = itemView.findViewById(R.id.tv);
        iv.setImageResource(images.get(position));
        tv.setVisibility(position == images.size() - 1 ? View.VISIBLE : View.GONE);
        if (position == images.size() - 1) {
            tv.setOnClickListener(v -> {
                Toast.makeText(activity, "nice", Toast.LENGTH_SHORT).show();
            });
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeViewAt(position); //这个方法会造成空白页面
        container.removeView((View) object);
    }
}
