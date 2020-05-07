package com.gzf01.rxzmvvm.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * Title: Result 类 <br/>
 * Description: 返回实体类 <br/>
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class Result implements Serializable {
    private int code;
    private String message;
    private Map<String,String> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
