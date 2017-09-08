package com.example.mydemo.net.volleydemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.mydemo.R;
import com.example.mydemo.net.volley.BitmapCache;

import java.util.List;

/**
 * volley不仅提供了这些请求的方式，还提供了加载图片的一些方法和控件：
 * 比如我们一个列表需要加载很多图片我们可以使用volley给我们提供的ImageLoader
 * （ ImageLoader比ImageRequest更加高效，因为它不仅对图片进行缓存，还可以过滤掉重复的链接，避免重复发送请求。）
 */
public class ImageAdapter extends ArrayAdapter<String> {

    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    public ImageAdapter(Context context, List<String> objects) {
        super(context, 0, objects);
        mQueue = Volley.newRequestQueue(getContext());
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = getItem(position);
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(getContext());
        } else {
            imageView = (ImageView) convertView;
        }
        // getImageListener（imageView控件对象，默认图片地址，失败图片地址）;  
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
        // get(图片地址，listener，宽，高)；自动帮你处理图片的宽高再也不怕大图片的oom了  
        mImageLoader.get(url, listener, 100, 200);
        return imageView;
    }

    /**
     * Volley还提供的加载图片的控件com.android.volley.NetworkImageView。
     * （这个控件在被从父控件detach的时候，会自动取消网络请求的，即完全不用我们担心相关网络请求的生命周期问题，
     * 而且NetworkImageView还会根据你对图片设置的width和heigh自动压缩该图片不会产生多的内存，还有NetworkImageView在列表中使用不会图片错误）
     * @param iv
     * @param url
     */
    private void networkImageViewUse(NetworkImageView iv, String url) {
        ImageLoader imLoader = new ImageLoader(mQueue, new BitmapLruCache());
        iv.setDefaultImageResId(R.mipmap.ic_launcher);
        iv.setErrorImageResId(R.mipmap.ic_launcher);
        iv.setImageUrl(url, imLoader);
    }
}  