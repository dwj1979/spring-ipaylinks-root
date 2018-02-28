package com.ipaylinks.conf.model;

import java.io.Serializable;

public class DataTablesRequest implements Serializable{

    private static final long serialVersionUID = 7649168413071838074L;

    //请求次数
    private int draw;

    //起始页
    private int start;

    //数据长度
    private int length;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "DataTablesRequest{" +
                "draw=" + draw +
                ", start=" + start +
                ", length=" + length +
                '}';
    }
}
