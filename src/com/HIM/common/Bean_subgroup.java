package com.HIM.common;

import java.io.Serializable;
import java.util.ArrayList;

public class Bean_subgroup implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int userID;
	private int groupIndex;
	private String groupName;
	
	private ArrayList<Bean_friendinfo> friendinfos;
	
	public Bean_subgroup(int userID, int groupindex, String groupname)
	{
		super();
		this.userID = userID;
		this.groupIndex = groupindex;
		this.groupName = groupname;
	}
	
	
	public final int getUserID()
	{
		return userID;
	}
	public final int getGroupindex()
	{
		return groupIndex;
	}
	public final String getGroupname()
	{
		return groupName;
	}


	public final ArrayList<Bean_friendinfo> getFriendinfos()
	{
		return friendinfos;
	}


	public final void setFriendinfos(ArrayList<Bean_friendinfo> friendinfos)
	{
		this.friendinfos = friendinfos;
	}


	@Override
	public String toString()
	{
		return "[" + groupIndex + "]" + "." + userID + ":" +groupName;
	}
	
	
	
	
	
}
