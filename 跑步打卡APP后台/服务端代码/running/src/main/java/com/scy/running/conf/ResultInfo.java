package com.scy.running.conf;

import lombok.Data;

import java.io.Serializable;

/**
 *返回结果类统一封装
 */
@Data
public class ResultInfo implements Serializable {

    // 状态码
    private Integer code;
    // 消息
    private String message;
    // 数据对象
    private Object data;

    private Integer total;

    /**
     * 无参构造器
     */
    public ResultInfo() {
        super();
    }

    public ResultInfo(Status status) {
        super();
        this.code = status.code;
        this.message = status.message;
    }

    public ResultInfo result(Object data) {
        this.data = data;
        return this;
    }

    public ResultInfo message(String message) {
        this.message = message;
        return this;
    }
    public ResultInfo total(Integer total) {
        this.total = total;
        return this;
    }

    /**
     * 只返回状态，状态码，消息
     *
     * @param code
     * @param message
     */
    public ResultInfo(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    /**
     * 只返回状态，状态码，数据对象
     *
     * @param code
     * @param data
     */
    public ResultInfo(Integer code, Object data) {
        super();
        this.code = code;
        this.data = data;
    }

    /**
     * 返回全部信息即状态，状态码，消息，数据对象
     *
     * @param code
     * @param message
     * @param data
     */
    public ResultInfo(Integer code, String message, Object data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }
}