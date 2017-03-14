package com.github.hyacinth.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2016/7/28
 * Time: 14:43
 */
public class PathTools {

    private static String rootClassPath;

    private static String webRootPath;

    /**
     * 获取classpath路径
     *
     * @return classpath的绝对路径
     */
    public static String getRootClassPath() {
        if (rootClassPath == null) {
            try {
                String path = PathTools.class.getClassLoader().getResource("").toURI().getPath();
                rootClassPath = new File(path).getAbsolutePath();
            } catch (Exception e) {
                String path = PathTools.class.getClassLoader().getResource("").getPath();
                rootClassPath = new File(path).getAbsolutePath();
            }
        }
        return rootClassPath;
    }

    /**
     * 获取WebRoot路径
     *
     * @return WebRoot绝对路径
     */
    public static String getWebRootPath() {
        if (webRootPath == null) {
            try {
                String path = PathTools.class.getResource("/").toURI().getPath();
                webRootPath = new File(path).getParentFile().getParentFile().getCanonicalPath();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return webRootPath;
    }

}
