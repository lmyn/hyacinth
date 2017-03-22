package com.github.hyacinth.support.spring;

import com.github.hyacinth.*;
import com.github.hyacinth.dialect.Dialect;
import com.github.hyacinth.sql.DefaultCompiler;
import com.github.hyacinth.sql.markdown.MdFileMonitor;
import com.github.hyacinth.sql.DefaultBuilder;
import com.github.hyacinth.sql.markdown.MdResolve;
import com.github.hyacinth.tools.StringTools;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring Bean
 * 支持Spring 容器启动
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/28
 * Time: 15:04
 */
public class HyacinthConfiguration {

    private DataSource dataSource;
    //热加载
    private Boolean isHotLoad;
    //sql 模板文件路径
    private Resource[] mdLocations;
    //配置
    private String configName;

    private String basePackage;

    private Dialect dialect;

    private long interval = 5000;

    private boolean isStarted = false;

    private MdFileMonitor monitor;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDialect(String dialect) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        this.dialect = (Dialect) Class.forName(dialect).newInstance();
    }

    public void setIsHotLoad(Boolean hotLoad) {
        this.isHotLoad = hotLoad;
    }

    public void setMdLocations(Resource[] mdLocations) {
        this.mdLocations = mdLocations;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void init() throws IOException {
        if (isStarted) return;
        if (StringTools.isBlank(configName)) {
            throw new IllegalArgumentException("configName can not be blank");
        }
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource can not be null");
        }
        Config config = new Config(configName, dataSource, dialect);

        if (config.getDataSource() == null && dataSource != null) {
            config.setDataSource(dataSource);
        }

        MdResolve.setTemplateCompiler(new DefaultCompiler());

        //处理sql资源文件
        processSqlFile(isHotLoad);

        new TableBuilder().build(basePackage, config);
        DbKit.addConfig(config);
        DbKit.setSqlBuilder(new DefaultBuilder());
        isStarted = true;

    }

    /**
     * 开启sql文件热加载
     */
    private void processSqlFile(boolean isHotLoad) throws IOException {
        List<File> files = new ArrayList<File>();
        List<File> folders = new ArrayList<File>();
        List<String> foldersPaths = new ArrayList<String>();
        for (Resource resource : mdLocations) {
            File file = resource.getFile();
            files.add(file);
            String path = file.getParentFile().getAbsolutePath();
            if (!foldersPaths.contains(path)) {
                foldersPaths.add(path);
                folders.add(file);
            }
        }
        //解析sql文件
        new MdResolve().resolve(files);
        //如果配置上，开启了热加载功能，则启用文件监听进程
        if (isHotLoad) {
            if (this.monitor == null) {
                this.monitor = new MdFileMonitor();
            }

            monitor.addListener(folders, interval);
        }
    }



    /**
     * spring 容器销毁bean时调用此方法
     * 方法将停止文件监控进程
     *
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        //停止文件监控进程
        if(this.monitor != null){
            this.monitor.shutdown();
        }
    }
}
