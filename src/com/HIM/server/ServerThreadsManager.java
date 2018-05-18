package com.HIM.server;

import java.util.HashMap;
import java.util.Set;

/*
 * 在线连接管理类
 * 维护一个<String"IP:Port"->Thread_Server>的Map
 * @Author:Zhangt2333
 */
public class ServerThreadsManager 
{
	private static HashMap<String, Thread_Server> MAP = new HashMap<>();	
	
	public static void addServerThread(Thread_Server t)
	{
		String mapinfo = t.getIP()+":"+t.getPort();
		MAP.put(mapinfo, t);
	}
	
	public static void removeServerThread(Thread_Server t)
	{
		String mapinfo = t.getIP()+":"+t.getPort();
		MAP.remove(mapinfo);
	}
	
	public static Thread_Server getServerThread(String MapInfo)
	{
		
		return (Thread_Server)MAP.get(MapInfo);
	}
	
	public static Set<String> getMapKeys()	
	{
		return MAP.keySet();
	}
	
}
