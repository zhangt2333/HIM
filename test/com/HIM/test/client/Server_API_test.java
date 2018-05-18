package com.HIM.test.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.HIM.client.*;
import com.HIM.common.*;


public class Server_API_test
{

	public static void main (String args[])
	{
		com.HIM.server.Main.main(new String[] {"test"});
		Thread_receiver thread_receiver = new Thread_receiver(TesterConfig.server_ip,TesterConfig.server_port);
		new Thread(thread_receiver).start();
		Server_API robot = new Server_API(TesterConfig.server_ip,TesterConfig.server_port,thread_receiver);
		
//		Thread_receiver thread_receiver = new Thread_receiver(config.SEVER_IP,config.port);
//		new Thread(thread_receiver).start();
//		Server_API robot = new Server_API(config.SEVER_IP,config.port,thread_receiver);
		
		/*
		发送信息.
		传参： bean_message对象
		返回：boolean
		--------------------------------------------------------------*/
//		Bean_message message = new Bean_message(10000,10001, "消息类型"
//								, tools.get_nowtime(true), "消息内容2");				
//		boolean sendOK = robot.sendMessage(message);
//		System.out.println(sendOK);
		//--------------------------------------------------------------

		/*
		接受未读消息.
		传参：int receiver
		返回：ArrayList<Bean_message>
		--------------------------------------------------------------*/
//		ArrayList<Bean_message> unreadedMessages = robot.getUnreadedMessages(10001);
//		for(Iterator<Bean_message> it=unreadedMessages.iterator();it.hasNext();)
//			System.out.println(it.next());
		//--------------------------------------------------------------

		
		/*
		创建群.
		传参：int qq, String qunname
		返回：int qunnum
		--------------------------------------------------------------*/
//		int qunnum = robot.createQun(10001, "他的群二号");
//		System.out.println("新创的群:"+qunnum);
		//--------------------------------------------------------------

		
		/*
		加入群.
		传参：int qq, int qunnum
		返回：boolean
		--------------------------------------------------------------*/
//		boolean addqunOK = robot.addQun(10001, 10000);
//		System.out.println(addqunOK);
		//--------------------------------------------------------------
		
		
		/*
		退出群.
		传参：int qq, int qunnum
		返回：boolean
		--------------------------------------------------------------*/
//		boolean quitqunOK = robot.quitQun(10001, 10000);
//		System.out.println(quitqunOK);
		//--------------------------------------------------------------
		
		/*
		更改群信息（群名、群头像编号）.
		传参：Bean_quninfo
		返回：boolean
		--------------------------------------------------------------*/
//		Bean_quninfo quninfo = new Bean_quninfo(10000, "新的群名1", 0);
//		boolean updateQunInfoOK = robot.updateQunInfo(quninfo);
//		System.out.println(updateQunInfoOK);
		//--------------------------------------------------------------

				
		/*
		更改用户群昵称.
		传参：int qq, int qunnum ,String qunname
		返回：boolean
		--------------------------------------------------------------*/
//		boolean updateUserQunOK = robot.updateUserQun(10001, 10000, "群备注啊");
//		System.out.println(updateUserQunOK);
		//--------------------------------------------------------------
		
		/*
		群号查询群.
		传参：int qunnum
		返回：bean_quninfo
		--------------------------------------------------------------*/
//		Bean_quninfo quninfo = robot.queryQunInfo_qunnum(10000);
//		System.out.println(quninfo);
		//--------------------------------------------------------------
	
		
		/*
		群名查询群.
		传参：String qunname,为空时候搜索全部
		返回：ArrayList<Bean_quninfo>
		--------------------------------------------------------------*/
//		ArrayList<Bean_quninfo> quninfos = robot.queryQunInfo_qunname("");
//		for(Iterator<Bean_quninfo> it=quninfos.iterator();it.hasNext();)
//			System.out.println(it.next());
		//--------------------------------------------------------------

		/*
		查询用户加入的群.
		传参：int qq
		返回：ArrayList<bean_quninfo>
		--------------------------------------------------------------*/
//		ArrayList<Bean_quninfo> quns = robot.queryUserQuns(10000);
		//--------------------------------------------------------------
		
		/*
		查询一个群中的成员.
		传参：int qunnum
		返回：ArrayList<bean_friendinfo>
		--------------------------------------------------------------*/
//		ArrayList<Bean_friendinfo> quns = robot.queryQunMembers(10000);
		//--------------------------------------------------------------
		
		
		/*
		文件上传.
		传参： bean_fileinfo对象    （参数一:"file"or"picture"or"video")
		返回：服务端文件编号
		--------------------------------------------------------------*/
//		Bean_fileinfo afile = new Bean_fileinfo("file",new File("t:/桌面/测试小文件.txt"));
//		int fileindex = robot.uploadFile(afile);
//		logger.writelog("Client", "uploadFile: " + fileindex);
		//--------------------------------------------------------------
		
		/*
		文件下载.
		传参： 服务端文件编号，File存储对象
		返回： bean_fileinfo对象（可能为null）
		--------------------------------------------------------------*/
		//robot.downloadFile(2, new File("t:/桌面/abc.txt"));
		//--------------------------------------------------------------
		
		/*
		头像上传.
		传参： File对象(头像)
		返回： 头像文件编号
		--------------------------------------------------------------*/
		//int photoindex = robot.uploadPhoto(new File("t:/桌面/打字姿势.jpg"));
		//logger.writelog("Client", "uploadphoto ok: " + photoindex);	
		//--------------------------------------------------------------
		
		/*
		头像下载.	
		传参： 头像编号，File对象(头像库目录)
		返回： boolean
		--------------------------------------------------------------*/
//		boolean photodownOK = robot.downloadPhoto(1, new File("t:/桌面/java课设"));
//		logger.writelog("Client", "downloadphoto : " + (photodownOK?"ok":"fail"));	
		//--------------------------------------------------------------
		
		/*
		获取个人信息.
		传参： int qq
		返回： Bean_userinfo对象（可能为null),(仅有photoindex,sign,nickname,sex,birthday,constellation,applydate)
		--------------------------------------------------------------*/
		//Bean_userinfo userinfo = robot.queryOwnInfo(10000);
		//--------------------------------------------------------------

		/*
		注册账号.
		传参： Bean_userinfo对象一个，其构造方法(String passwd,String nickname,int sex,String birthday,int constellation)
		返回：int qq（可能为-1)
		--------------------------------------------------------------*/
//		for (int i=1;i<=2;i++)
//		{
//			Bean_userinfo bean = new Bean_userinfo("12345", "测试注册"+i, 1, "2018-01-01", i);
//			int qq = robot.register(bean);
//			logger.writelog("API",qq+"");
//		}
		//--------------------------------------------------------------

		/*
		登录账号.
		传参： int qq,String passwd
		返回： boolean
		--------------------------------------------------------------*/
		//boolean loginOK = robot.login(10000, "12345");
		//if (loginOK) logger.writelog("Client","login ok");
		//--------------------------------------------------------------
		
		/*
		登出账号.
		传参： int qq
		返回： boolean
		--------------------------------------------------------------*/
//		boolean logoutOK = robot.logout(10000);
//		if (logoutOK)logger.writelog("Client","logout ok");
		//--------------------------------------------------------------
		
		/*
		查询分组列表.
		传参： int qq
		返回：  ArrayList<Bean_subgroup>
		--------------------------------------------------------------*/
//		String subgroup_json = robot.querySubgroup(10000);
//		Bean_subgroup.InitSubgroup(subgroup_json);
//		int groupnum = Bean_subgroup.getGroupNum();
//		for (int i=0;i<groupnum;i++)
//		{
//			int fnum = Bean_subgroup.getFriendNum(i);
//			for (int j=0;j<fnum;j++)
//				System.out.print(Bean_subgroup.getAFriendqq(i, j) + 
//								 Bean_subgroup.getAFriendNotename(i, j));
//			System.out.println();
//		}
		//--------------------------------------------------------------
		
		/*
		更新分组列表.
		传参：  int qq , 前提是Bean_Subgroup必须初始化过
		返回：  boolean
		--------------------------------------------------------------*/
		//boolean updateSubgroupOK = robot.updateSubgroup(10000);
		//System.out.println(updateSubgroupOK);
		//--------------------------------------------------------------
		
		/*
		添加好友、删除好友等方法通过 Bean_subgroup类实现，然后update一下分组信息.
		传参： 见Bean_subgroup
		返回： 见Bean_subgroup
		--------------------------------------------------------------*/
//		String subgroup_json = robot.querySubgroup(10000);
//		Bean_subgroup.InitSubgroup(subgroup_json);
//		Bean_subgroup.AddAFriend(10001, "备注名1", 0);
//		Bean_subgroup.AddAFriend(10002, "备注名2", 1);
//		boolean updateSubgroupOK = robot.updateSubgroup(10000);
//		System.out.println(updateSubgroupOK);
		//其他操作具体见类Bean_subgroup
		//--------------------------------------------------------------
		
		/*
		通过qq查询他人
		传参：  int qq
		返回：  ArrayList<Bean_friendinfo>
		--------------------------------------------------------------*/
//		Bean_friendinfo f = robot.queryUser_qq(10000);
//		System.out.println(f);
		//--------------------------------------------------------------
		
		
		/*
		通过昵称查询他人
		传参：  String 昵称
		返回：  ArrayList<Bean_friendinfo>
		--------------------------------------------------------------*/
//		ArrayList<Bean_friendinfo> array1 = robot.queryUser_nickname("注册");
//		for(Iterator<Bean_friendinfo> it=array1.iterator();it.hasNext();)
//			System.out.println(it.next());
		//--------------------------------------------------------------
		
		/*
		批量查询他人
		传参：  ArrayList<Integer>
		返回：  ArrayList<Bean_friendinfo>
		--------------------------------------------------------------*/
//		ArrayList<Integer> list = new ArrayList<>();
//		list.add(10001);
//		list.add(10002);
//		ArrayList<Bean_friendinfo> array11 = robot.queryUsers(list);
//		for(Iterator<Bean_friendinfo> it=array11.iterator();it.hasNext();)
//			System.out.println(it.next());
		//--------------------------------------------------------------
		
		
		/*
		更新信息(不需要密码).
		传参：  Bean_userinfo对象(int qq,String sign,int sex,String nickname,String birthday,int constellation)
		返回：  boolean
		--------------------------------------------------------------*/
//		Bean_userinfo newuserinfo = new Bean_userinfo(10000, "我的签名", 1, "改过名字的用户", "2018-01-02", 6);
//		boolean updateOwninfoOK = robot.updateOwnInfo(newuserinfo);
//		System.out.println(updateOwninfoOK);
		//--------------------------------------------------------------
		
		/*
		修改密码.
		传参：  int qq,String oldpasswd,String newpasswd
		返回：  boolean
		--------------------------------------------------------------*/
//		boolean updatePasswdOK = robot.updatePasswd(10000, "123456", "12345678");
//		System.out.println(updatePasswdOK);
		//--------------------------------------------------------------
		
		/*
		更新头像.
		传参：  int qq, int photoindex (即要先上传头像获得新头像的编码)
		返回：  boolean
		--------------------------------------------------------------*/
//		boolean updatePhotoindexOK = robot.updatePhotoindex(10000, 1);
//		System.out.println(updatePhotoindexOK);
		//--------------------------------------------------------------
			
		
		/*
		查询聊天记录.
		传参：  int qq, int fqq 
		返回：  ArrayList<Bean_message>
		--------------------------------------------------------------*/
//		ArrayList<Bean_message> messages1 = robot.queryChatRecords(10001, 10000);
//		for(Bean_message message : messages1)
//			System.out.println(message);
		//--------------------------------------------------------------
		
		/*
		查询群聊天记录.
		传参：  int qq, int qunnum 
		返回：  ArrayList<Bean_message>
		--------------------------------------------------------------*/
//		ArrayList<Bean_message> messages2 = robot.queryQunChatRecords(10000, 10000);
//		for(Bean_message message : messages2)
//			System.out.println(message);
		//--------------------------------------------------------------

		/*
		发表说说.
		传参：  Bean_mood 
		返回：  boolean
		--------------------------------------------------------------*/
//		Bean_mood mood = new Bean_mood(10000, tools.get_nowtime(true), "说说1");
//		boolean postMoodOK = robot.postMood(mood);
//		System.out.println(postMoodOK);
		//--------------------------------------------------------------

		/*
		查询说说.
		传参：  ArrayList<Integer> posters 
		返回：  ArrayList<Bean_mood> moods
		--------------------------------------------------------------*/
//		ArrayList<Integer> posters = new ArrayList<>();
//		posters.add(10000);
//		posters.add(10001);
//		ArrayList<Bean_mood> moods = robot.queryMoods(posters);
//		for(Bean_mood mood : moods)
//			System.out.println(mood);
		//--------------------------------------------------------------

		
		/*
		添加群文件.(结合上传文件得到的fileindex用)
		传参：  int uploader, int qunnum, int fileindex
		返回：  boolean
		--------------------------------------------------------------*/
//		boolean addQunfileOK = robot.addQunfile(10000, 10000, 1);
//		System.out.println(addQunfileOK);
		//--------------------------------------------------------------

		/*
		删除群文件.(结合上传文件得到的fileindex用)
		传参：  int qunnum, int fileindex
		返回：  boolean
		--------------------------------------------------------------*/
//		boolean deleteQunfileOK = robot.deleteQunfile(10000, 1);
//		System.out.println(deleteQunfileOK);
		//--------------------------------------------------------------

		/*
		查询群文件.
		传参：  int qunnum
		返回：  ArrayList<Bean_fileinfo> fileinfos
		--------------------------------------------------------------*/
//		ArrayList<Bean_fileinfo> fileinfos = robot.queryQunfiles(10000);
//		for(Bean_fileinfo fileinfo : fileinfos)
//			System.out.println(fileinfo);.
		//--------------------------------------------------------------

		/*
		查询用户的好友分组.
		传参：  int userID
		返回：  ArrayList<Bean_subgroup> 
		--------------------------------------------------------------*/
//		ArrayList<Bean_subgroup> subgroups = robot.querySubGroup(10000);
//		for(Bean_subgroup subgroup : subgroups)
//			System.out.println(subgroup);
		//--------------------------------------------------------------
		
		/*
		查询用户的好友.
		传参：  int userID
		返回：  ArrayList<Bean_friendinfo> 
		--------------------------------------------------------------*/
//		ArrayList<Bean_friendinfo> friendinfos = robot.queryUserFriends(10000);
//		for(Bean_friendinfo friendinfo : friendinfos)
//			System.out.println(friendinfo);
		//--------------------------------------------------------------

		
		
	}
	

}
