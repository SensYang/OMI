package com.omi.bean.base;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by SensYang on 2017/04/03 18:46
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseJson<T> {
    private String result;
    private String type;
    private String msg;
    private List<T> userinfo;
    private List<T> datalist;
    private T data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(List<T> userinfo) {
        this.userinfo = userinfo;
    }

    public List<T> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<T> datalist) {
        this.datalist = datalist;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
