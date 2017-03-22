package com.github.hyacinth;

/**
 * 数据库字段
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/1/11
 * Time: 10:07
 */
public class Column {
    //列名
    private String name;
    //列类型
    private String sqlType;
    //是否允许为空
    private boolean nullAble;
    //注释
    private String comment;

    public Column(String name, String sqlType, boolean nullAble, String comment) {
        this.name = name;
        this.sqlType = sqlType;
        this.nullAble = nullAble;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public boolean isNullAble() {
        return nullAble;
    }

    public void setNullAble(boolean nullAble) {
        this.nullAble = nullAble;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
