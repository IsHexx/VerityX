package com.example.verityx.dto;

import java.util.List;

/**
 * 分页查询结果
 * 
 * @param <T> 数据类型
 */
public class PageResult<T> {
    
    private List<T> list;
    private int total;
    private int page;
    private int pageSize;
    
    public PageResult() {
    }
    
    public PageResult(List<T> list, int total, int page, int pageSize) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }
    
    public List<T> getList() {
        return list;
    }
    
    public void setList(List<T> list) {
        this.list = list;
    }
    
    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    public int getTotalPages() {
        return (total + pageSize - 1) / pageSize;
    }
} 