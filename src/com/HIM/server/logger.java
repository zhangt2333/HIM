package com.HIM.server;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.HIM.common.tools;
/*
 * 日志功能类，在GMT+8零点重置输出文件
 * @Author:Zhangt2333
 */
public class logger 
{
	private static final String LogFileDirectory = "log";
	public static String TodayDate;
	private static final long PERIOD_DAY = 1000 * 60 * 60 * 24;
	
	private static PrintWriter out;
	
	static 
	{
		//建立日志目录
		new File(LogFileDirectory).mkdir();
	}
	
	public static void LaunchLoggerTimer()
	{
		//新建日历对象
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
	                 calendar.get(Calendar.DATE),0,0,0);
		//设置定时器
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				RefreshOutputFile();
			}
		}, calendar.getTime(),PERIOD_DAY);
	}
	
	private static void RefreshOutputFile()
	{
		logger.write("LoggerTimer", "时钟刷新");
		TodayDate = tools.get_nowtime(false) + ".txt";
		File logFile = new File(LogFileDirectory + File.separatorChar + TodayDate);
		//更新输出流
		try
		{
			if (!logFile.exists())
				logFile.createNewFile();
			logger.write("LoggerTimer" ,"输出文件设置为：" + logFile.getAbsolutePath());
			if (out != null) out.close();
			out = new PrintWriter(new FileWriter(logFile,true),true);
		}catch (IOException e) 
		{
			logger.writelog("LoggerTimer", "更新文件输出流失败 " + e.getMessage());
		}
	}

	
	public static void writelog(String who,String what)
	{
		if (out == null) 
		{
			write(who, what);
			return;
		}
		out.println(tools.get_nowtime(true)+"("+who+"): "+what);
	}
	
	public static void write(String who,String what)
	{
		System.out.println(tools.get_nowtime(true)+"("+who+"): "+what);
	}
	
}
