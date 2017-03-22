package com.github.hyacinth.sql.markdown;

import com.github.hyacinth.sql.monitor.FileAlterationMonitor;
import com.github.hyacinth.sql.monitor.FileAlterationObserver;
import com.github.hyacinth.tools.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 监听者
 *
 *
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/2
 * Time: 9:06
 */
public class MdFileMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MdFileMonitor.class);

    //创建文件变化监听器
    private List<FileAlterationMonitor> monitors;

    public void addListener(File file, long interval) {
        // 创建一个文件观察器用于处理文件的格式
        FileAlterationObserver observer = new FileAlterationObserver(file);
        observer.addListener(new MdFileListener());
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

    public void addListener(List<File> files, long interval) {
        for (File file : files) {
            addListener(file, interval);
        }
    }

    public void addListener(String path, long interval) {
        if (StringTools.isBlank(path)) {
            throw new IllegalArgumentException("Monitor the path cannot be empty！");
        }
        addListener(new File(path), interval);
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
