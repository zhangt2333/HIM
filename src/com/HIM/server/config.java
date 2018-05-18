package com.HIM.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/*
 * 魔法值大合集
 * @Author:Zhangt2333
 */
public class config 
{
	private static final String configFileName = "HIM_server.properties";
	
	private static Properties prop;
	
	private static int ServerPort;
	private static int ServerThreadMaxSize;
	private static String DbDriverClass = "driverClass";
	private static String DbjdbcUrl = "jdbcUrl";
	private static String Dbusername = "Dbusername";
	private static String Dbpasswd = "Dbpasswd";
	
	private static File DbFileDirectory;
	private static File DbPhotoDirectory;
	
	private static int DbConnPool_initialPoolSize;
	private static int DbConnPool_minPoolSize;
	private static int DbConnPool_maxPoolSize;
	private static int DbConnPool_maxStatements;
	private static int DbConnPool_acquireIncrement;
	private static int DbConnPool_acquireRetryAttempts;
	private static int DbConnPool_acquireRetryDelay;
	private static boolean DbConnPool_autoCommitOnClose;
	private static int DbConnPool_checkoutTimeout;
	private static int DbConnPool_idleConnectionTestPeriod;
	private static int DbConnPool_maxIdleTime;
	private static boolean DbConnPool_testConnectionOnCheckin;
	private static int DbConnPool_unreturnedConnectionTimeout;
	private static int DbConnPool_maxIdleTimeExcessConnections;
	private static int DbConnPool_maxConnectionAge;
	
	
	public static final String DbDefaultSubgroupName = "My Friends";
			

	public static void Init()
	{
		prop = new Properties();
		String now = "";
		try
		{
			//System.out.println(config.class.getResource("").getPath());
			//System.out.println(config.class.getResource("/").getPath());
			//System.out.println(config.class.getClassLoader().getResource("").getPath());
			//System.out.println(System.getProperty("user.dir"));
			File configFile = new File(System.getProperty("user.dir") + File.separator + configFileName);
			if (!configFile.exists())
			{
				logger.writelog("config", "配置文件"+configFile.getAbsolutePath()+"不存在！程序退出！");
				System.exit(-1);
			}
			prop.load( (InputStream)new FileInputStream(configFile) );
			now = "ServerPort";
			config.ServerPort = Integer.parseInt(prop.getProperty("ServerPort"));
			now = "ServerThreadMaxSize";
			config.ServerThreadMaxSize =  Integer.parseInt(prop.getProperty("ServerThreadMaxSize"));
			now = "driverClass";
			config.DbDriverClass = prop.getProperty("driverClass");
			now = "jdbcUrl";
			config.DbjdbcUrl = prop.getProperty("jdbcUrl");
			now = "root";
			config.Dbusername = prop.getProperty("Dbusername");
			now = "Dbpasswd";
			config.Dbpasswd = prop.getProperty("Dbpasswd");
			now = "DbFileDirectory";
			config.DbFileDirectory = new File(prop.getProperty("DbFileDirectory"));
			now = "DbPhotoDirectory";
			config.DbPhotoDirectory = new File(prop.getProperty("DbPhotoDirectory"));
			//连接池部分
			now = "连接池部分";
			config.DbConnPool_initialPoolSize = Integer.parseInt(prop.getProperty("C3P0initialPoolSize"));
			config.DbConnPool_minPoolSize = Integer.parseInt(prop.getProperty("C3P0minPoolSize"));
			config.DbConnPool_maxPoolSize = Integer.parseInt(prop.getProperty("C3P0maxPoolSize"));
			config.DbConnPool_maxStatements = Integer.parseInt(prop.getProperty("C3P0maxStatements"));
			config.DbConnPool_acquireIncrement = Integer.parseInt(prop.getProperty("C3P0acquireIncrement"));
			config.DbConnPool_acquireRetryAttempts = Integer.parseInt(prop.getProperty("C3P0acquireRetryAttempts"));
			config.DbConnPool_acquireRetryDelay = Integer.parseInt(prop.getProperty("C3P0acquireRetryDelay"));
			config.DbConnPool_autoCommitOnClose = Boolean.parseBoolean(prop.getProperty("C3P0autoCommitOnClose"));
			config.DbConnPool_checkoutTimeout = Integer.parseInt(prop.getProperty("C3P0checkoutTimeout"));
			config.DbConnPool_idleConnectionTestPeriod = Integer.parseInt(prop.getProperty("C3P0idleConnectionTestPeriod"));
			config.DbConnPool_maxIdleTime = Integer.parseInt(prop.getProperty("C3P0maxIdleTime"));
			config.DbConnPool_testConnectionOnCheckin = Boolean.parseBoolean(prop.getProperty("C3P0testConnectionOnCheckin"));
			config.DbConnPool_unreturnedConnectionTimeout = Integer.parseInt(prop.getProperty("C3P0unreturnedConnectionTimeout"));
			config.DbConnPool_maxIdleTimeExcessConnections = Integer.parseInt(prop.getProperty("C3P0maxIdleTimeExcessConnections"));
			config.DbConnPool_maxConnectionAge = Integer.parseInt(prop.getProperty("C3P0maxConnectionAge"));	
			
		} catch (IOException | NumberFormatException | NullPointerException e)
		{
			logger.writelog("config", "读取配置" + now + "失败，程序退出！\n"+e.getMessage());
			System.exit(-1);
		}
		/*System.out.println(ServerPort);
		System.out.println(DbDriverClass);
		System.out.println(DbjdbcUrl);
		System.out.println(Dbusername);
		System.out.println(Dbpasswd);
		System.out.println(DbConnPool_acquireIncrement);
		System.out.println(DbConnPool_acquireRetryAttempts);
		System.out.println(DbConnPool_acquireRetryDelay);*/
	}

	public static final int getServerPort()
	{
		return ServerPort;
	}

	public static final String getDbDriverClass()
	{
		return DbDriverClass;
	}

	public static final String getDbjdbcUrl()
	{
		return DbjdbcUrl;
	}

	public static final String getDbusername()
	{
		return Dbusername;
	}

	public static final String getDbpasswd()
	{
		return Dbpasswd;
	}


	public static final File getDbFileDirectory()
	{
		return DbFileDirectory;
	}

	public static final File getDbPhotoDirectory()
	{
		return DbPhotoDirectory;
	}
	
	public static final int getDbConnPool_initialPoolSize()
	{
		return DbConnPool_initialPoolSize;
	}

	
	public static final int getDbConnPool_minPoolSize()
	{
		return DbConnPool_minPoolSize;
	}

	public static final int getDbConnPool_maxPoolSize()
	{
		return DbConnPool_maxPoolSize;
	}

	public static final int getDbConnPool_maxStatements()
	{
		return DbConnPool_maxStatements;
	}
	
	public static final int getDbConnPool_acquireIncrement()
	{
		return DbConnPool_acquireIncrement;
	}

	public static final int getDbConnPool_acquireRetryAttempts()
	{
		return DbConnPool_acquireRetryAttempts;
	}

	public static final int getDbConnPool_acquireRetryDelay()
	{
		return DbConnPool_acquireRetryDelay;
	}

	public static final boolean getDbConnPool_autoCommitOnClose()
	{
		return DbConnPool_autoCommitOnClose;
	}

	public static final int getDbConnPool_checkoutTimeout()
	{
		return DbConnPool_checkoutTimeout;
	}

	public static final int getDbConnPool_idleConnectionTestPeriod()
	{
		return DbConnPool_idleConnectionTestPeriod;
	}

	public static final int getDbConnPool_maxIdleTime()
	{
		return DbConnPool_maxIdleTime;
	}

	public static final boolean getDbConnPool_testConnectionOnCheckin()
	{
		return DbConnPool_testConnectionOnCheckin;
	}

	public static final int getServerThreadMaxSize()
	{
		return ServerThreadMaxSize;
	}

	public static final int getDbConnPool_unreturnedConnectionTimeout()
	{
		return DbConnPool_unreturnedConnectionTimeout;
	}

	public static final int getDbConnPool_maxIdleTimeExcessConnections()
	{
		return DbConnPool_maxIdleTimeExcessConnections;
	}

	public static final int getDbConnPool_maxConnectionAge()
	{
		return DbConnPool_maxConnectionAge;
	}

}

