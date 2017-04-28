package com.github.hyacinth;

import java.util.Map;

/**
 * Bean 标记型接口.
 * 标记类似于像Model这种以Map为数据承载的bean对象
 */
public interface Bean {

    Map<String, Object> attrsMap();
}

