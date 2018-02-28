package com.ipaylinks.conf.model;

import java.io.Serializable;

public class Project implements Serializable{
    private static final long serialVersionUID = 4241716048239597627L;

    private String path;

    private String springApplicationName;

    public String getSpringApplicationName() {
        return springApplicationName;
    }

    public void setSpringApplicationName(String springApplicationName) {
        this.springApplicationName = springApplicationName;
    }

    private String name;

    private String desc;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Project{" +
                "path='" + path + '\'' +
                ", springApplicationName='" + springApplicationName + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
