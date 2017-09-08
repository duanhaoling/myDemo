package com.example.mydemo.rx.rxjava.bean;

import java.util.List;

/**
 * Created by ldh on 2016/12/2 0002.
 */

public class Employee {
    private String name;
    private List<Mission> missions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Mission> getMission() {
        return missions;
    }

    public void setMission(List<Mission> missions) {
        this.missions = missions;
    }

    public Employee(String name, List<Mission> missions) {
        this.name = name;
        this.missions = missions;
    }

    public static class Mission {
        public String desc;

        public Mission(String desc) {
            this.desc = desc;
        }
    }
}
