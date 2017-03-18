package com.github.hyacinth.sql;

import com.github.hyacinth.sql.monitor.FileAlterationMonitor;
import com.github.hyacinth.sql.monitor.FileAlterationObserver;
import com.github.hyacinth.tools.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/2
 * Time: 9:06
 */
public class SqlTemplateFileMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlTemplateFileMonitor.class);

    //创建文件变化监听器
    private List<FileAlterationMonitor> monitors;

    public void addListener(String filePath, long interval) {
        if (StringTools.isBlank(filePath)) {
            throw new IllegalArgumentException("Monitor the path cannot be empty！");
        }
        // 创建一个文件观察器用于处理文件的格式
        FileAlterationObserver observer = new FileAlterationObserver(filePath);
        observer.addListener(new SqlTemplateFileListenerAdaptor());
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        if (monitors == null) {
            this.monitors = new ArrayList<FileAlterationMonitor>();
        }
        monitors.add(monitor);
        try {
            monitor.start();
        } catch (Exception e) {
            throw new RuntimeException("Boot failure monitoring process！");
        }

    }

    public void addListener(List<String> filePaths, long interval) {
        for (String path : filePaths) {
            addListener(path, interval);
        }
    }

    public void shutdown() {
        if (monitors != null && !monitors.isEmpty()) {
            try {
                for (FileAlterationMonitor monitor : monitors) {
                    monitor.stop();
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
