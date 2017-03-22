package com.github.hyacinth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * SqlReporter.
 */
public class SqlReporter implements InvocationHandler {

    private Connection conn;

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlReporter.class);

    SqlReporter(Connection conn) {
        this.conn = conn;
    }

    Connection getConnection() {
        Class clazz = conn.getClass();
        return (Connection) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{Connection.class}, this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("prepareStatement")) {
            String info = "Sql: " + args[0];
            LOGGER.debug(info);
        }
        return method.invoke(conn, args);
    }

}




