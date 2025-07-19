package com.itheima.cold.druid.common;

import java.sql.*;
import java.util.Properties;

/**
 * Druid助手类，用于管理Druid数据源的连接和关闭
 */
public class DruidHelper {

	// Druid数据库连接URL
	private String url = "jdbc:avatica:remote:url=http://172.17.0.143:8888/druid/v2/sql/avatica/";
	// 数据库连接配置属性
	private Properties conf = new Properties();
	// 数据库连接对象
	private Connection connection;

	/**
	 * 获得Druid连接
	 * 如果连接对象为空，则尝试建立新的连接
	 * @return Connection 数据库连接对象
	 */
	public Connection getConnection() {
		try {
			if (null == connection) {
				connection = DriverManager.getConnection(url, conf);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 关闭Druid连接
	 * 负责关闭ResultSet、Statement和Connection对象
	 * @param connection 数据库连接对象
	 * @param st Statement对象
	 * @param rs ResultSet对象
	 */
	public void close(Connection connection, Statement st, ResultSet rs) {
		try {
			if (rs!=null) {
				rs.close();
			}
			if (st!=null) {
				st.close();
			}
			if (connection!=null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
