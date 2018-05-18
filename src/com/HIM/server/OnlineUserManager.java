package com.HIM.server;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
/*
 * 在线用户管理类
 * 维护一个<userID->String"IP:Port">的Map
 * @Author:Zhangt2333
 */
public class OnlineUserManager 
{
	//userID->MapInfo的映射
	private static HashMap<Integer,String> users = new HashMap<>();
	
	public static void addUser(int userID,String MapInfo)
	{
		if (users.containsKey(userID))
			users.remove(userID);
		users.put(userID, MapInfo);	
	}
	
	public static String getUser(int userID)
	{
		if (users.containsKey(userID))
			return users.get(userID);
		return null;
	}
	
	public static void removeUser(Integer userID)
	{
		if (users.containsKey(userID)) 
		{
			users.remove(userID);
		}
	}
	
	public static boolean isContainsUser(int userID)
	{
		if (users.containsKey(userID))
			return true;
		return false;
	}
		
	public static Set<Entry<Integer, String>> getMapSet()
	{
		return users.entrySet();
	}

}
