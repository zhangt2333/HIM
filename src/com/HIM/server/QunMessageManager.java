package com.HIM.server;

import com.HIM.common.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/*
 * 群消息托管类
 * @Author:Zhangt2333
 */

//单例模式，管理群、群员以及消息的转发
public class QunMessageManager
{
	//单例对象
	public static QunMessageManager instance = null;
	
	class qun
	{
		public int qunID;
		public ArrayList<Integer> qunmember;
		public Boolean working = false;
		public qun(int qunID) 
		{
			this.qunID = qunID;
			this.qunmember = new ArrayList<>();
		}
	}
	
	private HashMap<Integer,qun> quns = new HashMap<>();
	
	public QunMessageManager() {}
	
	public static QunMessageManager GetManagerInstance()
	{
		if (instance == null)
		{
			instance = new QunMessageManager();
			instance.Init();
		}
		return instance;
	}
	
	public void Init()
	{
		quns.clear();
		ArrayList<Bean_quninfo> list = Db_Operate.queryQunInfo_qunname("");
		ArrayList<Bean_friendinfo> members;
		qun oneQun = null;
		for(Iterator<Bean_quninfo> item = list.iterator();item.hasNext();)
		{
			oneQun = new qun(item.next().getQunID());
			members = Db_Operate.queryQunMembers(oneQun.qunID);
			for(Iterator<Bean_friendinfo> u = members.iterator();u.hasNext();)
			{
				oneQun.qunmember.add(u.next().getFriendID());
			}
			quns.put(oneQun.qunID, oneQun);
		}
	}
	
	public void createNewQun(int qunID)
	{
		qun oneQun = new qun(qunID);
		ArrayList<Bean_friendinfo> members = Db_Operate.queryQunMembers(qunID);
		for(Iterator<Bean_friendinfo> u = members.iterator();u.hasNext();)
		{
			oneQun.qunmember.add(u.next().getFriendID());
		}
		quns.put(qunID, oneQun);
	}
	
	public void addMember(int userID,int qunID)
	{
		quns.get(qunID).qunmember.add(userID);
	}
	
	public void quitQun(int userID,int qunID)
	{
		quns.get(qunID).qunmember.remove(new Integer(userID));
	}
	
	public void relayMessage(Bean_message message)
	{
		if (!quns.containsKey(message.getQunnum()))
			return;
		qun oneQun = quns.get(message.getQunnum());
		synchronized(oneQun.working)
		{
			int sender = message.getSender();
			Iterator<Integer> members = oneQun.qunmember.iterator();
			Integer member;
			oneQun.working = true;
			while(members.hasNext())
			{
				member = members.next();
				message.setReceiver(member);
				if (member == sender) 
					continue;
				boolean ok = false;
				if (OnlineUserManager.isContainsUser(member))
				{
					Thread_Server t = ServerThreadsManager.getServerThread(
										OnlineUserManager.getUser(member));
					ok = t.ralayMessage(message);
					System.out.println("转发一个消息给" + member + message);
				}
				Db_Operate.addChatRecord(message,ok);
			}	
			oneQun.working = false;
		}	
	}
}
