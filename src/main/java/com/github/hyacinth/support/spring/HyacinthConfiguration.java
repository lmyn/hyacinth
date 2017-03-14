package com.github.hyacinth.support.spring;

import com.github.hyacinth.Config;
import com.github.hyacinth.DbKit;
import com.github.hyacinth.Table;
import com.github.hyacinth.TableBuilder;
import com.github.hyacinth.dialect.Dialect;
import com.github.hyacinth.sql.SqlTemplateFileMonitor;
import com.github.hyacinth.tools.StringTools;

import javax.sql.DataSource;
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
    private String locations;
    //配置
    private String configName;
    private Dialect dialect;

    private long interval = 5000;

    private boolean isStarted = false;

    private SqlTemplateFileMonitor monitor = new SqlTemplateFileMonitor();

    private List<Table> tableList = new ArrayList<Table>();

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDialect(String dialect) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        this.dialect = (Dialect) Class.forName(dialect).newInstance();
    }

    public void setIsHotLoad(Boolean hotLoad) {
        this.isHotLoad = hotLoad;
    }

    public void setLocations(String location) {
        this.locations = location;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void init() {
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

        //如果配置上，开启了热加载功能，则启用文件监听进程
        if (isHotLoad) {
            monitor.addListener(locations, interval);
        }

        new TableBuilder().build(tableList, config);
        DbKit.addConfig(config);
        isStarted = true;

    }

    /**
     * spring 容器销毁bean时调用此方法
     * 方法将停止文件监控进程
     *
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        //停止文件监控进程
        monitor.shutdown();
    }
}
