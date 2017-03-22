package com.github.hyacinth;

import java.util.List;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/8
 * Time: 10:59
 */
public interface Page<T> {

    Page<T> setList(List<T> list);

    Page<T> setPageNumber(int pageNumber);

    Page<T> setPageSize(int pageSize);

    Page<T> setTotalPage(int totalPage);

    Page<T> setTotalRow(int totalRow);
}
