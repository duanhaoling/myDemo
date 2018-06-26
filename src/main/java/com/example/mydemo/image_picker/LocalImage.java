
package com.example.mydemo.image_picker;

/**
 * 相册选择图片时候使用到的本地图片类
 *
 */
public class LocalImage extends LocalImageFolder {

    private int id;// 在数据库中的ID
    private String display_name;// 显示时候的名字
    private boolean isSelected;// 是否被选择
    private int width;
    private int height;

    public int getId() {
        return id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((display_name == null) ? 0 : display_name.hashCode());
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        LocalImage other = (LocalImage) obj;
        if (display_name == null) {
            if (other.display_name != null)
                return false;
        } else if (!display_name.equals(other.display_name))
            return false;
        if (id != other.id)
            return false;
        return true;
    }

}
