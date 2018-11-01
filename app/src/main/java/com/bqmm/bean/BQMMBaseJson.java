package com.bqmm.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by SensYang on 2017/04/01 22:46
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BQMMBaseJson<T> {
    private int error_code;
    private List<T> data_list;
    private T data;
    private String message;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<T> getData_list() {
        return data_list;
    }

    public void setData_list(List<T> data_list) {
        this.data_list = data_list;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
