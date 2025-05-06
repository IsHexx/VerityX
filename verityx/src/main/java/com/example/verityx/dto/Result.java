package com.example.verityx.dto;

/**
 * 通用响应结果
 * 
 * @param <T> 数据类型
 */
public class Result<T> {
    
    private int code;
    private String message;
    private T data;
    
    public Result() {
    }
    
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * 成功响应（无数据）
     * 
     * @return 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }
    
    /**
     * 成功响应
     * 
     * @param <T> 数据类型
     * @param data 数据
     * @return 成功响应
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }
    
    /**
     * 成功响应
     * 
     * @param <T> 数据类型
     * @param message 消息
     * @param data 数据
     * @return 成功响应
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }
    
    /**
     * 失败响应
     * 
     * @param <T> 数据类型
     * @param code 错误码
     * @param message 错误消息
     * @return 失败响应
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }
    
    /**
     * 失败响应
     * 
     * @param <T> 数据类型
     * @param message 错误消息
     * @return 失败响应
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
    
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
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
} 