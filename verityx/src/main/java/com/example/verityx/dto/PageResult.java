package com.example.verityx.dto;

import java.util.List;

/**
 * 分页结果包装类
 *
 * @param <T> 数据类型
 */
public class PageResult<T> {
    private List<T> list;
    private Integer total;
    private Integer page;
    private Integer pageSize;

    public PageResult() {
    }

    public PageResult(List<T> list, Integer total, Integer page, Integer pageSize) {
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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
} 