package com.example.atcdemo.network;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.UnknownHostException;


public class ServiceError {
    public static final ServiceError UNKNOWN = new ServiceError(-1, "Unknown", "");
    public static final ServiceError IOEXCEPTION = new ServiceError(-2, "IOException", "");
    public static final ServiceError UNKNOWNHOSTEXCEPTION = new ServiceError(-3, "UNKNOWNHOSTEXCEPTION", "");

    private int statusCode;
    private final String error;
    private @SerializedName("error_description")
    final String message;
    private String status;
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ServiceError(int statusCode, String error, String description) {
        this.statusCode = statusCode;
        this.error = error;
        this.message = description;
    }



    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public static ServiceError from(Throwable t) {

        if (t instanceof UnknownHostException) {
            return UNKNOWNHOSTEXCEPTION;
        } else if (t instanceof IOException) {
            return IOEXCEPTION;
        }

        return UNKNOWN;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
