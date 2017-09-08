package com.example.mydemo.rx.retrofit;

import java.util.List;

/**
 * Created by ldh on 2017/8/14.
 */

public class TestData {

    /**
     * user : {}
     * orderArray : []
     */

    private UserBean user;
    private List<?> orderArray;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<?> getOrderArray() {
        return orderArray;
    }

    public void setOrderArray(List<?> orderArray) {
        this.orderArray = orderArray;
    }

    public static class UserBean {
    }
}
