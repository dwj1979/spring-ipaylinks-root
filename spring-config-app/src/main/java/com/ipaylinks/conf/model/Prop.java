package com.ipaylinks.conf.model;

import java.io.Serializable;

public class Prop implements Serializable{
    private static final long serialVersionUID = 5816578131297011394L;

    private String name;
    private String value;
    private String desc;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Prop{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
