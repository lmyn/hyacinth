package com.github.hyacinth.sql.xml.structure;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/6/17
 * Time: 10:20
 */
@XmlRootElement
public class SqlGroup {

    @XmlAttribute
    public String name;

    @XmlAttribute
    public String dbType;

    @XmlElement(name = "sql")
    public List<SqlItem> sqlItems = new ArrayList<SqlItem>();

    public void add(SqlItem sqlItem) {
        sqlItems.add(sqlItem);
    }

}
