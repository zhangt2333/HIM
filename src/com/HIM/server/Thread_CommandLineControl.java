package com.HIM.server;

import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

/*
 * 命令行功能类
 * @Author:Zhangt2333
 */
public class Thread_CommandLineControl extends Thread
{
	static Scanner scan = new Scanner(System.in);	
	static final String BlockLine = "------------------------------------";
	private boolean working = true;
	
	public void run()
	{
		String command = null;
		while(working)
		{
			command = scan.nextLine();
			if (command != null)
			{
				command = command.toLowerCase();
				switch (command)
				{
					case "help":help();break;
					case "showonlineusers":showOnlineUsers();break;
					case "showconnectors":showConnectors();break;
					default:System.out.println("您输入的指令有误!(help获取指令手册)");break;
				}
			}
		}		
	}
	
	private void help()
	{
		String help = "help:\n" + BlockLine + "\n"
					  + "0.help:获取指令手册.\n"
					  + "1.showConnectors:获取当前客户端连接者.\n"
					  + "2.showOnlineUsers:获取当前在线用户.\n"
					  + BlockLine;
		logger.writelog("Admin",help);
	}
	
	private void showOnlineUsers()
	{
		Set<Entry<Integer, String>> items = OnlineUserManager.getMapSet();
		String str = "showOnlineUsers:\n" + BlockLine + "\n";
		int count=0;
		for (Entry<Integer, String> item : items)
		{
			str += item.getKey() + "(" + item.getValue()  + ")\n";
			count++;
		}
		str += "共"+count+"名用户在线!\n" + BlockLine;
		logger.writelog("Admin", str);
	}
	
	private void showConnectors()
	{
		Set<String> items = ServerThreadsManager.getMapKeys();
		String str = "showConnectors:\n" + BlockLine + "\n";
		int count=0;
		for(String item : items)
		{
			str += item + "\n";
			count++;
		}
		str += "共"+count+"个连接!\n" + BlockLine;
		logger.writelog("Admin", str);
	}

	
}
