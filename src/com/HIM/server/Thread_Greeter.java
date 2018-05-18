package com.HIM.server;


import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

/*	
 * 迎宾线程类，启动ServerSocket
 * 功能：
 * 1.从config读入端口号，生成ServerSocket
 * 2.等待用户的连接请求，并为之生成一个Thread_server，同时加入管家的映射中
 * @Author:Zhangt2333
 */
public class Thread_Greeter extends Thread
{
	private boolean working = true;
	public Thread_Greeter() {}
	public void pause() {this.working=false;}
	public void restart() {this.working=true;}
	
	public void run() 
	{
		try
		{
			ServerSocket greeter = new ServerSocket(config.getServerPort());
			//新建线程池
			ExecutorService ThreadPool = Executors.newFixedThreadPool(config.getServerThreadMaxSize());
			logger.writelog("greeter",InetAddress.getLocalHost().getHostAddress()
									  +":"+greeter.getLocalPort());
			logger.writelog("greeter","系统正在等待客户请求中...");
			Thread_Server newUser = null;
			while(working)
			{
				//请入用户
				Socket userSocket = greeter.accept();
				//输出日志
				logger.writelog("greeter","Connection accept:"+userSocket);
				//用户在管家处登记，申请侍者，待重写
				newUser = new Thread_Server(userSocket);
				//提交到侍者线程池
				ThreadPool.execute(newUser);
				ServerThreadsManager.addServerThread(newUser);
			}
			greeter.close();
			ThreadPool.shutdown();
		}catch (BindException e) 
		{
			logger.writelog("greeter","端口号已被用，服务端退出!");
			System.exit(-1);
		}catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
}
