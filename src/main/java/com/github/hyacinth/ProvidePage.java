package com.github.hyacinth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/21
 * Time: 10:03
 */
public class ProvidePage<T> implements Page<T> {

    private Map<String, Object> attrs = new HashMap<String, Object>();

    public List<T> getList() {
        return (List<T>) this.attrs.get("list");
    }

    public Integer getPageNumber() {
        return (Integer) this.attrs.get("pageNumber");
    }

    public Integer getPageSize() {
        return (Integer) this.attrs.get("pageSize");
    }

    public Integer getTotalPage() {
        return (Integer) this.attrs.get("totalPage");
    }

    public Integer getTotalRow() {
        return (Integer) this.attrs.get("totalRow");
    }

    @Override
    public Page<T> setList(List<T> list) {
        this.attrs.put("list", list);
        return this;
    }

    @Override
    public Page<T> setPageNumber(int pageNumber) {
        this.attrs.put("pageNumber", pageNumber);
        return this;
    }

    @Override
    public Page<T> setPageSize(int pageSize) {
        this.attrs.put("pageSize", pageSize);
        return this;
    }

    @Override
    public Page<T> setTotalPage(int totalPage) {
        this.attrs.put("totalPage", totalPage);
        return this;
    }

    @Override
    public Page<T> setTotalRow(int totalRow) {
        this.attrs.put("totalRow", totalRow);
        return this;
    }
}
