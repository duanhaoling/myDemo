package com.example.mydemo.greendaotdemo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * desc:
 * Created by ldh on 2018/7/13.
 */
@Entity
public class Test {
    @Id(autoincrement = true)
    private Long id;

    private String hello;

    private String world;

    @Generated(hash = 1405388283)
    public Test(Long id, String hello, String world) {
        this.id = id;
        this.hello = hello;
        this.world = world;
    }

    @Generated(hash = 372557997)
    public Test() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHello() {
        return this.hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }
}
