package com.HIM.common;

import java.util.ArrayList;
import java.util.HashMap;

public class SubGroupManager
{
	private static HashMap<Integer, Bean_subgroup> map_subGroup = null;
	private static ArrayList<Bean_subgroup> subgroups = null;
	private static ArrayList<Bean_friendinfo> allFriendInfos = null;
	private static ArrayList<Integer> friendIDs = null;
	private static HashMap<Integer, Bean_friendinfo> map_friendInfo = null;
	
	public static final ArrayList<Integer> getFriendIDs()
	{
		return friendIDs;
	}

	public static void Init(ArrayList<Bean_subgroup> subgroups,ArrayList<Bean_friendinfo> friendinfos)
	{
		SubGroupManager.map_subGroup = new HashMap<>();
		SubGroupManager.map_friendInfo = new HashMap<>();
		SubGroupManager.friendIDs = new ArrayList<>();
		SubGroupManager.subgroups = subgroups;
		for(Bean_subgroup subgroup : subgroups)
		{
			map_subGroup.put(subgroup.getGroupindex(), subgroup);
			subgroup.setFriendinfos(new ArrayList<>());
		}
		for(Bean_friendinfo friendinfo : friendinfos)
		{
			map_subGroup.get(friendinfo.getGroupIndex()).getFriendinfos().add(friendinfo);
			map_friendInfo.put(friendinfo.getFriendID(), friendinfo);
			friendIDs.add(friendinfo.getFriendID());
		}
		allFriendInfos = friendinfos;
	}
	
	public static void deleteFriend(Integer friendID)
	{
		if ( ! friendIDs.remove(friendID) ) return ;
		Bean_friendinfo friendinfo = map_friendInfo.get(friendID);
		map_subGroup.get(friendinfo.getGroupIndex()).getFriendinfos().remove(friendinfo);
		SubGroupManager.allFriendInfos.remove(friendinfo);		
	}
	
	public static void addFriend(Bean_friendinfo friendinfo,Integer groupIndex)
	{
		friendinfo.setGroupIndex(groupIndex);
		
		Bean_subgroup subgroup = map_subGroup.get(groupIndex);
		if (subgroup != null)
		{
			subgroup.getFriendinfos().add(friendinfo);
			map_friendInfo.put(friendinfo.getFriendID(), friendinfo);
			allFriendInfos.add(friendinfo);			
		}
	}
	
	public static void updateFriendAlias(Integer friendID,String alias)
	{
		Bean_friendinfo friendinfo = map_friendInfo.get(friendID);
		if (friendinfo != null) friendinfo.setAlias(alias);
	}
	
	public static Bean_friendinfo getFriendInfo(Integer friendID)
	{
		return map_friendInfo.get(friendID);
	}
	
	
	public static final ArrayList<Bean_friendinfo> getAllFriendinfos()
	{
		return SubGroupManager.allFriendInfos;
	}

	public static final ArrayList<Bean_subgroup> getSubgroups()
	{
		return SubGroupManager.subgroups;
	}
	
	
}
