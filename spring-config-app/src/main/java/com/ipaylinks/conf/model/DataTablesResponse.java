package com.ipaylinks.conf.model;

import java.io.Serializable;
import java.util.List;

public class DataTablesResponse implements Serializable{
    private static final long serialVersionUID = -5082683198053887025L;

    private int recordsTotal;

    private int recordsFiltered;

    private List<Prop> data;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<Prop> getData() {
        return data;
    }

    public void setData(List<Prop> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataTablesResponse{" +
                "recordsTotal=" + recordsTotal +
                ", recordsFiltered=" + recordsFiltered +
                ", data=" + data +
                '}';
    }
}
