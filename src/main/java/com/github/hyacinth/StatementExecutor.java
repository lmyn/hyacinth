package com.github.hyacinth;

import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: LY
 * Email: lcrysman@gmail.com
 * Date: 2017-10-17
 * Time: 11:11
 */
public interface StatementExecutor {

    <T> T execute(Connection conn);
}
