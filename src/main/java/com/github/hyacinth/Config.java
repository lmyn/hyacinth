package com.github.hyacinth;

import com.github.hyacinth.dialect.Dialect;
import com.github.hyacinth.dialect.MysqlDialect;
import com.github.hyacinth.tools.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 核心配置对象
 * <p>
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/8
 * Time: 10:14
 */
public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    private final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    //标识名
    String name;
    //数据源
    DataSource dataSource;

    //数据库方言
    Dialect dialect;
    //是否展示Sql
    boolean showSql;
    Container container;

    /**
     * 以下4个构造方法
     */
    public Config(String name, DataSource dataSource, Dialect dialect, boolean showSql, Container container) {
        if (StringTools.isBlank(name))
            throw new IllegalArgumentException("Config name can not be blank");
        if (dataSource == null)
            throw new IllegalArgumentException("DataSource can not be null");
        if (dialect == null)
            throw new IllegalArgumentException("Dialect can not be null");
        if (container == null)
            throw new IllegalArgumentException("ContainerFactory can not be null");

        this.name = name.trim();
        this.dataSource = dataSource;
        this.dialect = dialect;
        this.showSql = showSql;
        this.container = container;
    }

    public Config(String name, DataSource dataSource) {
        this(name, dataSource, false, new MysqlDialect());
    }

    public Config(String name, DataSource dataSource, boolean showSql, Dialect dialect) {
        this(name, dataSource, dialect, showSql, Container.defaultContainer);
    }

    private Config() {
    }

    /**
     * 缺省Config  DbKit.brokenConfig = Config.createBrokenConfig()
     */
    static Config createBrokenConfig() {
        Config ret = new Config();
        ret.dialect = new MysqlDialect();
        ret.showSql = false;
        ret.container = Container.defaultContainer;
        return ret;
    }

    public String getName() {
        return name;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Container getContainer() {
        return container;
    }

    public boolean isShowSql() {
        return showSql;
    }

    /**
     * 获取数据库连接
     *
     * @return
     * @throws SQLException
     */
    public final Connection getConnection() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn != null)
            return conn;
        return showSql ? new SqlReporter(dataSource.getConnection()).getConnection() : dataSource.getConnection();
    }

    /**
     * 以下几组方法用于连接资源释放
     */
    public final void close(ResultSet rs, Statement st, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        close(conn);
    }

    public final void close(Statement st, Connection conn) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        close(conn);
    }

    public final void close(Connection conn) {
        if (threadLocal.get() == null) { // in transaction if conn in threadlocal
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new HyacinthException(e);
                }
            }
        }

    }
}
