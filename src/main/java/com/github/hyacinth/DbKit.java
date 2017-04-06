package com.github.hyacinth;

import com.github.hyacinth.sql.SqlBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 数据操作工具类
 *
 * Author: luoyong
 * Email: lcrysman@gmail.com
 * Date: 2017/2/8
 * Time: 10:43
 */
public class DbKit {
    /**
     * 主配置
     */
    static Config config = null;

    static SqlBuilder sqlBuilder = null;

    static Config brokenConfig = Config.createBrokenConfig();

    private static Map<Class<? extends Model>, Config> modelToConfig = new HashMap<Class<? extends Model>, Config>();
    private static Map<String, Config> configNameToConfig = new HashMap<String, Config>();

    static final Object[] NULL_PARA_ARRAY = new Object[0];
    public static final String MAIN_CONFIG_NAME = "main";
    public static final int DEFAULT_TRANSACTION_LEVEL = Connection.TRANSACTION_REPEATABLE_READ;

    private DbKit() {
    }

    /**
     * 添加配置（数据源）
     *
     * @param config 配置对象，包含DataSource，Dialect
     */
    public static void addConfig(Config config) {
        if (config == null) {
            throw new IllegalArgumentException("Config can not be null");
        }
        if (configNameToConfig.containsKey(config.getName())) {
            throw new IllegalArgumentException("Config already exists: " + config.getName());
        }

        configNameToConfig.put(config.getName(), config);

        //如果当前的配置对象的配置名为默认的主配置名"main",则替换掉原配置对象
        if (MAIN_CONFIG_NAME.equals(config.getName())) {
            DbKit.config = config;
            DbPro.init(DbKit.config.getName());
        }

        if (DbKit.config == null) {
            DbKit.config = config;
            DbPro.init(DbKit.config.getName());
        }
    }

    public static Config removeConfig(String configName) {
        if (DbKit.config != null && DbKit.config.getName().equals(configName)) {
            // throw new RuntimeException("Can not remove the main config.");
            DbKit.config = null;
        }

        DbPro.removeDbProWithConfig(configName);
        return configNameToConfig.remove(configName);
    }

    static void addModelToConfigMapping(Class<? extends Model> modelClass, Config config) {
        modelToConfig.put(modelClass, config);
    }

    public static Config getConfig() {
        return config;
    }

    public static Config getConfig(String configName) {
        return configNameToConfig.get(configName);
    }

    public static Config getConfig(Class<? extends Model> modelClass) {
        return modelToConfig.get(modelClass);
    }

    static final void close(ResultSet rs, Statement st) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new HyacinthException(e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new HyacinthException(e);
            }
        }
    }

    static final void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new HyacinthException(e);
            }
        }
    }

    public static Set<Map.Entry<String, Config>> getConfigSet() {
        return configNameToConfig.entrySet();
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends Model> getUsefulClass(Class<? extends Model> modelClass) {
        // com.demo.blog.Blog$$EnhancerByCGLIB$$69a17158
        return (Class<? extends Model>) ((modelClass.getName().indexOf("EnhancerByCGLIB") == -1 ? modelClass : modelClass.getSuperclass()));
    }

    public static void setSqlBuilder(SqlBuilder sqlBuilder) {
        DbKit.sqlBuilder = sqlBuilder;
    }
}
