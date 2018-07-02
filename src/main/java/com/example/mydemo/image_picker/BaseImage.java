package com.example.mydemo.image_picker;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseImage implements Parcelable {

    public static final Parcelable.Creator<BaseImage> CREATOR = new Parcelable.Creator<BaseImage>() {
        public BaseImage createFromParcel(Parcel source) {
            return new BaseImage(source);
        }

        public BaseImage[] newArray(int size) {
            return new BaseImage[size];
        }
    };
    // 图片id
    private String imgId;
    // 图片路径
    private String imgUrl;
    // 图片状态
    private int imgStatus;
    // 图片上传状态
    private int imgUploadStatus;
    // 图片上传时显示的进度
    private String progressString;
    // 图片获取方式（app端特有）entry":图片获取方式 1：系统默认 2：相机拍摄 3：相册中选取,4:来自线上固有图片
    private int entry;
    // 2.1新增字段 图片描述
    private String imgDesc;


    public BaseImage() {
    }


    public BaseImage(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    protected BaseImage(Parcel in) {
        this.imgId = in.readString();
        this.imgUrl = in.readString();
        this.imgStatus = in.readInt();
        this.imgUploadStatus = in.readInt();
        this.progressString = in.readString();
        this.entry = in.readInt();
        this.imgDesc = in.readString();
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getImgStatus() {
        return imgStatus;
    }

    public void setImgStatus(int imgStatus) {
        this.imgStatus = imgStatus;
    }

    public int getImgUploadStatus() {
        return imgUploadStatus;
    }

    public void setImgUploadStatus(int imgUploadStatus) {
        this.imgUploadStatus = imgUploadStatus;
    }

    public String getProgressString() {
        return progressString;
    }

    public void setProgressString(String progressString) {
        this.progressString = progressString;
    }

    public int getEntry() {
        return entry;
    }

    public void setEntry(int entry) {
        this.entry = entry;
    }


    public String getImgDesc() {
        return imgDesc;
    }

    public void setImgDesc(String imgDesc) {
        this.imgDesc = imgDesc;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imgId == null) ? 0 : imgId.hashCode());
        result = prime * result + ((imgUrl == null) ? 0 : imgUrl.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseImage other = (BaseImage) obj;
        if (imgId == null) {
            if (other.imgId != null)
                return false;
        } else if (!imgId.equals(other.imgId))
            return false;
        if (imgUrl == null) {
            if (other.imgUrl != null)
                return false;
        } else if (!imgUrl.equals(other.imgUrl))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgId);
        dest.writeString(this.imgUrl);
        dest.writeInt(this.imgStatus);
        dest.writeInt(this.imgUploadStatus);
        dest.writeString(this.progressString);
        dest.writeInt(this.entry);
        dest.writeString(this.imgDesc);
    }
}