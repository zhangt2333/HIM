package com.HIM.server;

/*
 * 启动类
 * @Author:Zhangt2333
 */
public class Main 
{
	public static void main (String[] args)
	{
		//配置文件的初始化
		config.Init();
		//日志输出流输出化
		if ( args.length >=1 && args[0].equals("test") )
		{
			new Thread_CommandLineControl().start();
		}else 
		{
//			logger.LaunchLoggerTimer();
		}
		//连接池初始化
		Db_C3P0_ConnectionPoolUtil.InitDBSource();
		Db_Init.init();
		//初始化群消息管理者
		QunMessageManager.GetManagerInstance();
		//主线程迎宾者线程创建
		new Thread_Greeter().start();
	}
}
