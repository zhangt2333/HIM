package com.HIM.test.server;

import java.util.ArrayList;


import com.HIM.common.*;

import com.HIM.server.*;

public class Db_Operate_test
{
	public static void main (String args[])
	{
		com.HIM.server.Main.main(new String[]{"test"});
		
		/*register
		--------------------------------------------------------------*/
//		Bean_userinfo userinfo = new Bean_userinfo("12345", "是我没错了2", 1, "2018-05-20", 1);
//		int newUserID = Db_Operate.register(userinfo);
//		System.out.println(newUserID);
		//--------------------------------------------------------------

		/*login
		--------------------------------------------------------------*/
//		boolean loginOK = Db_Operate.login(10000, "12345", "192.168.0.1", 12345);
//		System.out.println(loginOK);
		//--------------------------------------------------------------
		
		/*querySubgroup
		--------------------------------------------------------------*/
//		ArrayList<Bean_subgroup> subgroups = Db_Operate.querySubgroup(10000);
//		for(Bean_subgroup subgroup : subgroups)
//			System.out.println(subgroup);
		//--------------------------------------------------------------
		
		/*addSubGroup
		--------------------------------------------------------------*/
//		int newSubGroupIndex = Db_Operate.addSubGroup(10000, "My family");
//		System.out.println(newSubGroupIndex);
		//--------------------------------------------------------------
		
		/*deleteSubGroup //同时删除好友
		--------------------------------------------------------------*/
//		boolean deleteSubGroup = Db_Operate.deleteSubGroup(2);
//		System.out.println(deleteSubGroup);
		//--------------------------------------------------------------
		
		/*addFriend
		--------------------------------------------------------------*/
//		boolean addFriendOK = Db_Operate.addFriend(10000, 10001, 1, "好友1");
//		System.out.println(addFriendOK);
		//--------------------------------------------------------------
		
		/*deleteFriend
		--------------------------------------------------------------*/
//		boolean deleteFriendOK = Db_Operate.deleteFriend(10000, 10001);
//		System.out.println(deleteFriendOK);
		//--------------------------------------------------------------
		
		/*moveFriend
		--------------------------------------------------------------*/
//		boolean moveFriendOK = Db_Operate.moveFriend(10000, 10001,2);
//		System.out.println(moveFriendOK);
		//--------------------------------------------------------------
		
		/*queryUserFriends
		--------------------------------------------------------------*/
//		ArrayList<Bean_friendinfo> friendinfos = Db_Operate.queryUserFriends(10000);
//		for (Bean_friendinfo friendinfo : friendinfos)
//			System.out.println(friendinfo);
		//--------------------------------------------------------------
		


		
		
		/*updateFriendAlias
		--------------------------------------------------------------*/
//		boolean updateFriendAliasOK = Db_Operate.updateFriendAlias(10000, 10001, "好友备注啊");
//		System.out.println(updateFriendAliasOK);
		//--------------------------------------------------------------

		
		
		/*queryOwnInfo
		--------------------------------------------------------------*/
//		Bean_userinfo userinfo = Db_Operate.queryOwnInfo(10000);
//		System.out.println(userinfo);
		//--------------------------------------------------------------

		/*queryUser_userID
		--------------------------------------------------------------*/
//		Bean_friendinfo friendinfo = Db_Operate.queryUser_userID(10000);
//		System.out.println(friendinfo);
		//--------------------------------------------------------------

		/*queryUser_nickname
		--------------------------------------------------------------*/
//		ArrayList<Bean_friendinfo> friendinfos = Db_Operate.queryUser_nickname("");
//		for(Bean_friendinfo friendinfo : friendinfos)
//			System.out.println(friendinfo);
		//--------------------------------------------------------------

		/*queryUsers
		--------------------------------------------------------------*/
//		ArrayList<Integer> fIntegers = new ArrayList<>();
//		fIntegers.add(10000);
//		fIntegers.add(10001);
//		ArrayList<Bean_friendinfo> friendinfos = Db_Operate.queryUsers(fIntegers);
//		for(Bean_friendinfo friendinfo : friendinfos)
//			System.out.println(friendinfo);
		//--------------------------------------------------------------

		/*updateSubGroupName
		--------------------------------------------------------------*/
//		boolean updateSubGroupNameOK = Db_Operate.updateSubGroupName(1, "Friendss!");
//		System.out.print(updateSubGroupNameOK);
		//--------------------------------------------------------------

		/*updateOwnInfo
		--------------------------------------------------------------*/
//		Bean_userinfo userinfo = new Bean_userinfo(10000, "希望在努力之后", 0, "还是At", "2018-05-21", 3);
//		boolean updateOwnInfoOK = Db_Operate.updateOwnInfo(userinfo);
//		System.out.println(updateOwnInfoOK);
		//--------------------------------------------------------------

		/*updatePasswd
		--------------------------------------------------------------*/
//		boolean updatePasswdOK = Db_Operate.updatePasswd(10000, "12345", "123456");
//		System.out.println(updatePasswdOK);
		//--------------------------------------------------------------

		/*updatePhotoindex
		--------------------------------------------------------------*/
//		boolean updatePhotoindexOK = Db_Operate.updatePhotoindex(10000, 0);
//		System.out.print(updatePhotoindexOK);
		//--------------------------------------------------------------

		
		
		
		/*queryQunMembers
		--------------------------------------------------------------*/
//		ArrayList<Bean_friendinfo> friendinfos = Db_Operate.queryQunMembers(1);
//		for(Bean_friendinfo friendinfo : friendinfos)
//			System.out.println(friendinfo);
		//--------------------------------------------------------------
		

		/*
		--------------------------------------------------------------*/
		
		//--------------------------------------------------------------

		/*
		--------------------------------------------------------------*/
		
		//--------------------------------------------------------------

		/*
		--------------------------------------------------------------*/
		
		//--------------------------------------------------------------

		/*
		--------------------------------------------------------------*/
		
		//--------------------------------------------------------------

		/*
		--------------------------------------------------------------*/
		
		//--------------------------------------------------------------

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*queryChatRecords
		--------------------------------------------------------------*/
//		ArrayList<Bean_message> messages = Db_Operate.queryChatRecords(10001, 10000);
//		for(Bean_message message : messages)
//			System.out.println(message);
		//--------------------------------------------------------------
		
		
		/*queryQunChatRecords
		--------------------------------------------------------------*/
//		ArrayList<Bean_message> messages = Db_Operate.queryQunChatRecords(10000, 10000);
//		for(Bean_message message : messages)
//			System.out.println(message);
		//--------------------------------------------------------------
		
		/*postMood
		--------------------------------------------------------------*/
//		Bean_mood mood = new Bean_mood(10000, tools.get_nowtime(true), "说说1");
//		boolean postMoodOK = Db_Operate.postMood(mood);
//		System.out.println(postMoodOK);
		//--------------------------------------------------------------
		
		/*queryMoods
		--------------------------------------------------------------*/
//		ArrayList<Integer> posters = new ArrayList<>();
//		posters.add(10000);
//		posters.add(10001);
//		ArrayList<Bean_mood> moods = Db_Operate.queryMoods(posters);
//		for(Bean_mood mood : moods)
//			System.out.println(mood);
		//--------------------------------------------------------------
		
		/*addQunfile
		--------------------------------------------------------------*/
//		boolean addQunfileOK = Db_Operate.addQunfile(10001, 10000, 1);
//		System.out.println(addQunfileOK);
		//--------------------------------------------------------------
		
		/*deleteQunfile
		--------------------------------------------------------------*/
//		boolean deleteQunfileOK = Db_Operate.deleteQunfile(10000, 1);
//		System.out.println(deleteQunfileOK);
		//--------------------------------------------------------------

		/*queryQunfiles
		--------------------------------------------------------------*/
//		ArrayList<Bean_fileinfo> fileinfos = Db_Operate.queryQunfiles(10000);
//		for(Bean_fileinfo fileinfo : fileinfos)
//			System.out.println(fileinfo);
		//--------------------------------------------------------------

		
	}
}
