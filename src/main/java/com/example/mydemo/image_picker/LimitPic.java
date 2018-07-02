package com.example.mydemo.image_picker;

import java.io.Serializable;

/**
 * 限制图片大小的类
 * Created by zhouying18 on 2017/8/3.
 */

public class LimitPic implements Serializable {
    public int minWidth;//宽最小值
    public int maxWidth;//宽最大值，当为0时，不对图片进行压缩
    public int minHeight;//高最小值
    public int maxHeight;//高最大值，当为0时，不对图片进行压缩

    public LimitPic() {
    }

    public LimitPic(int minWidth, int maxWidth, int minHeight, int maxHeight) {
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    //室内图
    public static LimitPic getRoomLimitPic(){
        return new LimitPic(600, 1242, 600, 1242);
    }

    //户型图
    public static LimitPic getModelLimitPic(){
        return new LimitPic(300, 1242, 300, 1242);
    }
}
