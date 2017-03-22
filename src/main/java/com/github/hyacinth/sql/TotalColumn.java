package com.github.hyacinth.sql;

import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;

/**
 * 自定义total查询项
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/16
 * Time: 16:40
 */
public class TotalColumn implements SelectItem {

    public TotalColumn() {
    }

    @Override
    public String toString() {
        return "COUNT(*) AS total";
    }

    @Override
    public void accept(SelectItemVisitor selectItemVisitor) {

    }

}
