package com.github.hyacinth.sql.markdown;

import com.github.hyacinth.sql.monitor.FileAlterationListener;
import com.github.hyacinth.sql.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 观察者
 * Markdown文件变动，重新加载变动的文件
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/1
 * Time: 11:09
 */
public class MdFileListener implements FileAlterationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileAlterationListener.class);

    @Override
    public void onStart(FileAlterationObserver observer) {
        LOGGER.info("starting to monitor folder. path: {}", observer.getDirectory().getAbsolutePath());
    }

    @Override
    public void onDirectoryCreate(File directory) {

    }

    @Override
    public void onDirectoryChange(File directory) {

    }

    @Override
    public void onDirectoryDelete(File directory) {

    }

    /**
     * 新文件创建时，重新加载新文件
     *
     * @param file The file created
     */
    @Override
    public void onFileCreate(File file) {
        new MdResolve().resolve(file);
        LOGGER.info("reload file {}", file);
    }

    /**
     * 文件修改，重新加载修改后的文件
     *
     * @param file The file changed
     */
    @Override
    public void onFileChange(File file) {
        new MdResolve().resolve(file);
        LOGGER.info("reload file {}", file);
    }

    @Override
    public void onFileDelete(File file) {

    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        try {
            observer.destroy();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
