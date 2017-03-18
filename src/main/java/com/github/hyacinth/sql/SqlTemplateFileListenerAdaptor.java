package com.github.hyacinth.sql;

import com.github.hyacinth.sql.monitor.FileAlterationListener;
import com.github.hyacinth.sql.monitor.FileAlterationObserver;

import java.io.File;

/**
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/3/2
 * Time: 9:41
 */
public class SqlTemplateFileListenerAdaptor implements FileAlterationListener {
    @Override
    public void onStart(FileAlterationObserver observer) {

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

    @Override
    public void onFileCreate(File file) {
        System.out.println(file.getName() + "created!");
    }

    @Override
    public void onFileChange(File file) {
        System.out.println(file.getName() + "change!");
    }

    @Override
    public void onFileDelete(File file) {

    }

    @Override
    public void onStop(FileAlterationObserver observer) {

    }
}
