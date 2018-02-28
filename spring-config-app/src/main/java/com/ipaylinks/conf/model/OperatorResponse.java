package com.ipaylinks.conf.model;

import java.io.Serializable;

public class OperatorResponse implements Serializable{
    private static final long serialVersionUID = 4535562840716116L;
    private boolean success;

    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "OperatorResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
