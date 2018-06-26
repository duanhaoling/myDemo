
package com.example.mydemo.image_picker;

/**
 * 本地从媒体中心（MediaCenter）中选择图片时候的文件夹类
 *
 */
public class LocalImageFolder {

    private int bucket_id;
    private String bucket_display_name;
    private String data;
    private int picNum;

    public int getPicNum() {
        return picNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }

    public LocalImageFolder() {
    }

    public LocalImageFolder(LocalImage image) {
        this.bucket_id = image.getBucket_id();
        this.bucket_display_name = image.getBucket_display_name();
        this.data = image.getData();
    }

    public int getBucket_id() {
        return bucket_id;
    }

    public void setBucket_id(int bucket_id) {
        this.bucket_id = bucket_id;
    }

    public String getBucket_display_name() {
        return bucket_display_name;
    }

    public void setBucket_display_name(String bucket_display_name) {
        this.bucket_display_name = bucket_display_name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LocalImageFloder [bucket_id=" + bucket_id + ", bucket_display_name=" + bucket_display_name + ", data="
                + data + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bucket_display_name == null) ? 0 : bucket_display_name.hashCode());
        result = prime * result + bucket_id;
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
        LocalImageFolder other = (LocalImageFolder) obj;
        if (bucket_display_name == null) {
            if (other.bucket_display_name != null)
                return false;
        } else if (!bucket_display_name.equals(other.bucket_display_name))
            return false;
        if (bucket_id != other.bucket_id)
            return false;
        return true;
    }

}
