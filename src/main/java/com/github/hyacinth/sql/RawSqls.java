package com.github.hyacinth.sql;

/**
 * 从Markdown文件获取的sql
 *
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/24
 * Time: 14:38
 */
public class RawSqls {

    private String group;
    private String subKey;
    private StringBuilder sql;
    private String comment;

    public RawSqls(String group, String subKey, StringBuilder sql, String comment) {
        this.group = group;
        this.subKey = subKey;
        this.sql = sql;
        this.comment = comment;
    }

    public RawSqls(String group, String subKey, StringBuilder sql) {
        this.group = group;
        this.subKey = subKey;
        this.sql = sql;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSubKey() {
        return subKey;
    }

    public void setSubKey(String subKey) {
        this.subKey = subKey;
    }

    public StringBuilder getSql() {
        return sql;
    }

    public void setSql(StringBuilder sql) {
        this.sql = sql;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
