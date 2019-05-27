package com.balckhao.testActivities.spinnerNew;

/**
 * Author ： BlackHao
 * Time : 2019/3/1 16:48
 * Description : 测试
 */
public class TestBean {

    private int id;
    private String name;
    long time;

    public TestBean(int id, String name, long time) {
        this.id = id;
        this.name = name;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
