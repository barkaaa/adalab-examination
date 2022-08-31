package com.adalab.examination.entity;

public class ServiceResponse<T> {
    int status;
    String message;
    T data;

    ServiceResponse(int status) {
        this.status = status;
    }

    public ServiceResponse(int status, String message) {
        this(status);
        this.message = message;
    }

    ServiceResponse(int status, T data) {
        this(status);
        this.data = data;
    }

    public ServiceResponse(int status, String message, T data) {
        this(status, message);
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
