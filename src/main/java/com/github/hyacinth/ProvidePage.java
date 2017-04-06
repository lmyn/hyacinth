package com.github.hyacinth;

import java.util.List;

/**
 * 默认的分页数据承载对象
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/21
 * Time: 10:03
 */
public class ProvidePage<T> implements Page<T> {

    //当前页码
    private int pageNumber;
    //页大小
    private int pageSize;
    //总页数
    private int totalPage;
    //总记录数
    private int totalRow;
    //数据主体
    private List<T> list;

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public Page<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    @Override
    public Page<T> setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    @Override
    public Page<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public Page<T> setTotalPage(int totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    @Override
    public Page<T> setTotalRow(int totalRow) {
        this.totalRow = totalRow;
        return this;
    }
}
