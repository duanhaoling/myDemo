package com.example.mydemo.greendaotdemo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ldh on 2017/7/17.
 */
@Entity()
public class LocalVideo {
    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String fileName;

    private String creationTime;

    @NotNull
    private String md5;

    private String latitude; //纬度

    private String longitude;  //经度

    @Generated(hash = 294175055)
    public LocalVideo(Long id, @NotNull String fileName, String creationTime,
            @NotNull String md5, String latitude, String longitude) {
        this.id = id;
        this.fileName = fileName;
        this.creationTime = creationTime;
        this.md5 = md5;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Generated(hash = 551533627)
    public LocalVideo() {
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
