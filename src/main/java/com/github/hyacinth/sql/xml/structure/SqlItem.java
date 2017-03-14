package com.github.hyacinth.sql.xml.structure;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/6/17
 * Time: 10:22
 */
@XmlRootElement
public class SqlItem {

    @XmlAttribute
    public String id;

    @XmlAttribute
    public String stable;

    @XmlValue
    public String value;

}
