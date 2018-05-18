package com.HIM.server;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
 * C3P0连接池类
 * @Author:Zhangt2333
 */
public class Db_C3P0_ConnectionPoolUtil
{
	private static ComboPooledDataSource cpds = null;
	
	//static { InitDBSource(); }
	
	public static final void InitDBSource()
	{
		cpds = new ComboPooledDataSource();
		try
		{
			cpds.setDriverClass(config.getDbDriverClass());
			cpds.setJdbcUrl(config.getDbjdbcUrl());
			cpds.setUser(config.getDbusername());//设置连接数据库的用户名  
            cpds.setPassword(config.getDbpasswd());//设置连接数据库的密码  
            cpds.setInitialPoolSize(config.getDbConnPool_initialPoolSize());//设置连接池的初始连接数  
            cpds.setMinPoolSize(config.getDbConnPool_minPoolSize());//设置连接池的最小连接数  
            cpds.setMaxPoolSize(config.getDbConnPool_maxPoolSize());//设置连接池的最大连接数  
            cpds.setMaxStatements(config.getDbConnPool_maxStatements());//设置连接池的缓存Statement的最大数    
            cpds.setAcquireIncrement(config.getDbConnPool_acquireIncrement());
            cpds.setAcquireRetryAttempts(config.getDbConnPool_acquireRetryAttempts());
            cpds.setAcquireRetryDelay(config.getDbConnPool_acquireRetryDelay());
            cpds.setAutoCommitOnClose(config.getDbConnPool_autoCommitOnClose());
            cpds.setCheckoutTimeout(config.getDbConnPool_checkoutTimeout());
            cpds.setIdleConnectionTestPeriod(config.getDbConnPool_idleConnectionTestPeriod());
            cpds.setMaxIdleTime(config.getDbConnPool_maxIdleTime());
            cpds.setTestConnectionOnCheckin(config.getDbConnPool_testConnectionOnCheckin());
            cpds.setUnreturnedConnectionTimeout(config.getDbConnPool_unreturnedConnectionTimeout());
            cpds.setMaxIdleTimeExcessConnections(config.getDbConnPool_maxIdleTimeExcessConnections());
            cpds.setMaxConnectionAge(config.getDbConnPool_maxConnectionAge());
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection()
	{  
		try 
		{
			return cpds.getConnection();
		} catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
	
	public static void release (Connection conn, PreparedStatement pst, ResultSet rs)
	{
		if (conn != null) 
		{
			try
			{
				conn.close();
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
}
