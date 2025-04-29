package com.example.verityx.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一API响应结果工具类
 */
public class JsonResult {
    private static final int SUCCESS_CODE = 200;
    private static final int ERROR_CODE = 500;
    private static final String SUCCESS_MESSAGE = "操作成功";
    private static final String ERROR_MESSAGE = "操作失败";

    private int code;
    private String message;
    private Object data;
    private boolean success;

    /**
     * 私有构造方法
     */
    private JsonResult(int code, String message, Object data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    /**
     * 创建成功响应
     */
    public static JsonResult success() {
        return new JsonResult(SUCCESS_CODE, SUCCESS_MESSAGE, null, true);
    }

    /**
     * 创建带数据的成功响应
     */
    public static JsonResult success(Object data) {
        return new JsonResult(SUCCESS_CODE, SUCCESS_MESSAGE, data, true);
    }

    /**
     * 创建带自定义消息的成功响应
     */
    public static JsonResult success(String message, Object data) {
        return new JsonResult(SUCCESS_CODE, message, data, true);
    }

    /**
     * 创建错误响应
     */
    public static JsonResult error() {
        return new JsonResult(ERROR_CODE, ERROR_MESSAGE, null, false);
    }

    /**
     * 创建带自定义消息的错误响应
     */
    public static JsonResult error(String message) {
        return new JsonResult(ERROR_CODE, message, null, false);
    }

    /**
     * 创建带自定义代码和消息的错误响应
     */
    public static JsonResult error(int code, String message) {
        return new JsonResult(code, message, null, false);
    }

    /**
     * 创建带自定义代码、消息和数据的错误响应
     */
    public static JsonResult error(int code, String message, Object data) {
        return new JsonResult(code, message, data, false);
    }

    // Getters and Setters
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
} 