package com.HIM.server;


import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Statement;
/*
 * 数据库初始化建表
 * @Author:Zhangt2333
 */
public class Db_Init
{
	//数据库建模
  	public static final String SQL_CreateTable_UserInfo = 
  				"CREATE TABLE `UserInfo`("
		 + "`UserID` int unsigned not null auto_increment primary key,"
		 + "`Passwd` char(16) not null,"
		 + "`Sign` char(30),"
		 + "`PhotoIndex` bigint unsigned not null default 0,"
		 + "`Nickname` char(10) not null,"
		 + "`Sex` tinyint not null,"
		 + "`Birthday` date,"
		 + "`Constellation` tinyint,"
		 + "`ApplyDate` date not null"
		 + ")character set utf8 AUTO_INCREMENT=10000;";
  	
  	public static final String SQL_CreateTable_SubGroup = 
  			"CREATE TABLE `SubGroup`("
  		 + "`GroupIndex` int unsigned not null auto_increment primary key,"
  		 + "`UserID` int unsigned not null,"
  		 + "`GroupName` char(10)"
  		 + ")character set utf8 AUTO_INCREMENT=1;";
  	
//  	public static final String SQL_CreateTable_SubGroupOrder = 
//  			"CREATE TABLE `SubGroupOrder`("
//  		 + "`UserID` int unsigned not null,"
//  		 + "`Order` TEXT not null" 
//  		 + ")character set utf8;";
  	
  	public static final String SQL_CreateTable_UserFriend =
  			"CREATE TABLE `UserFriend`("
  		 + "`UserID` int unsigned not null,"
  		 + "`FriendID` int unsigned not null,"
  		 + "`GroupIndex` int unsigned not null,"
  		 + "`Alias` char(10)"
  		 + ")character set utf8;";
  	
  	public static final String SQL_CreateTable_UserQun =
  			"CREATE TABLE `UserQun`("
  		+ "`Relation` tinyint not null,"
  		+ "`UserID` int unsigned not null,"
  		+ "`QunID` int unsigned not null,"
  		+ "`Alias` char(10),"
  		+ "`JoinTime` date not null"
  		+ ")character set utf8;";
  	
  	public static final String SQL_CreateTable_QunInfo =
  			"CREATE TABLE `QunInfo`("
  		+ "`QunID` int unsigned not null auto_increment primary key,"
  		+ "`QunName` char(10) not null,"
		+ "`PhotoIndex` bigint unsigned not null default 0,"
  		+ "`CreateTime` date not null"
  		+ ")character set utf8 AUTO_INCREMENT=10000;";
  	
  	public static final String SQL_CreateTable_ChatRecord =
  			"CREATE TABLE `ChatRecord`("
  		+ "`Index` bigint unsigned not null auto_increment primary key,"
  		+ "`SendTime` datetime not null,"
  		+ "`Sender` int unsigned not null,"
  		+ "`Type` char(40) not null,"
  		+ "`Receiver` int unsigned not null,"
  		+ "`QunID` int unsigned,"
  		+ "`Content` varchar(255) not null,"
  		+ "`Fonttype` char(10),"
  		+ "`Fontsize` tinyint unsigned,"
  		+ "`Fontcolor` char(13),"
  		+ "`Readed` tinyint not null"
  		+ ")character set utf8 AUTO_INCREMENT=1;";
  	
  	public static final String SQL_CreateTable_loginInfo =
  			"CREATE TABLE `LoginInfo`("
  		+ "`InfoIndex` bigint unsigned not null auto_increment primary key,"
  		+ "`UserID` int unsigned not null,"
  		+ "`IP` char(20) not null,"
  		+ "`Port` int not null,"
  		+ "`LoginTime` datetime not null,"
  		+ "`Status` tinyint not null"
  		+ ")character set utf8 AUTO_INCREMENT=1;";
  	
  	public static final String SQL_CreateTable_PhotoInfo =
  			"CREATE TABLE `PhotoInfo`("
  		+ "`InfoIndex` bigint unsigned not null auto_increment primary key,"
  		+ "`MD5` char(32) not null,"
  		+ "`Path` char(100) not null"
  		+ ")character set utf8 AUTO_INCREMENT=1;";
  	
  	public static final String SQL_CreateTable_FileInfo =
  			"CREATE TABLE `FileInfo`("
  		+ "`InfoIndex` bigint unsigned not null auto_increment primary key,"
  		+ "`Type` char(10) not null,"
  		+ "`Size` char(10) not null,"
  		+ "`MD5` char(32) not null,"
  		+ "`Name` char(50) not null,"
  		+ "`Path` char(100) not null"
  		+ ")character set utf8 AUTO_INCREMENT=1;";
  	
  	public static final String SQL_CreateTable_UserEmoticon = 
  			"CREATE TABLE `UserEmoticon`("
  		+ "`Index` bigint unsigned not null auto_increment primary key,"
  		+ "`Fileindex` bigint unsigned not null,"
  		+ "`UserID` int unsigned not null"
  		+ ")character set utf8 AUTO_INCREMENT=1;";
  	
  	public static final String SQL_CreateTable_MoodRecord = 
  			"CREATE TABLE `MoodRecord`("
  		+ "`Index` bigint unsigned not null auto_increment primary key,"
  		+ "`Poster` int unsigned not null,"
  		+ "`PostTime` datetime not null,"
  		+ "`Content` varchar(255) not null"
  		+ ")character set utf8 AUTO_INCREMENT=1;";
  	
  	public static final String SQL_CreateTable_QunFile = 
  			"CREATE TABLE `QunFile`("
  		+ "`Index` bigint unsigned not null auto_increment primary key,"
  		+ "`QunID` int unsigned not null,"		
  		+ "`Uploader` int unsigned not null,"
  		+ "`UploadTime` datetime not null,"
  		+ "`FileIndex` bigint unsigned not null"
  		+ ")character set utf8 AUTO_INCREMENT=1;";
  	
  	public static void init() //初始化，建表table
	{
		Statement stmt = null;
		Connection conn = null;
		try
		{
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			stmt = conn.createStatement();
		}catch (SQLException e)
		{
			e.printStackTrace();
			try
			{
				if (conn != null) conn.close();
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}	
		}
		
		try{stmt.executeUpdate(SQL_CreateTable_UserInfo);}
		catch (SQLException e){logger.writelog("db",e.getMessage());/*e.printStackTrace();*/}
		try{stmt.executeUpdate(SQL_CreateTable_SubGroup);}
		catch (SQLException e){logger.writelog("db",e.getMessage());/*e.printStackTrace();*/}
		try{stmt.executeUpdate(SQL_CreateTable_UserFriend);}
		catch (SQLException e){logger.writelog("db",e.getMessage());/*e.printStackTrace();*/}
		try{stmt.executeUpdate(SQL_CreateTable_UserQun);}
		catch (SQLException e){logger.writelog("db",e.getMessage());/*e.printStackTrace();*/}
		try{stmt.executeUpdate(SQL_CreateTable_QunInfo);}
		catch (SQLException e){logger.writelog("db",e.getMessage());/*e.printStackTrace();*/}
		try{stmt.executeUpdate(SQL_CreateTable_ChatRecord);}
		catch (SQLException e){logger.writelog("db",e.getMessage());/*e.printStackTrace();*/}
		try{stmt.executeUpdate(SQL_CreateTable_loginInfo);}
		catch (SQLException e){logger.writelog("db",e.getMessage());/*e.printStackTrace();*/}
		try{stmt.executeUpdate(SQL_CreateTable_PhotoInfo);}
		catch (SQLException e){logger.writelog("db",e.getMessage());/*e.printStackTrace();*/}
		try{stmt.executeUpdate(SQL_CreateTable_FileInfo);}
		catch (SQLException e){logger.writelog("db",e.getMessage());/*e.printStackTrace();*/}
		try{stmt.executeUpdate(SQL_CreateTable_UserEmoticon);}
		catch (SQLException e){logger.writelog("db",e.getMessage());/*e.printStackTrace();*/}
		try{stmt.executeUpdate(SQL_CreateTable_MoodRecord);}
		catch (SQLException e){logger.writelog("db",e.getMessage());/*e.printStackTrace();*/}
		try{stmt.executeUpdate(SQL_CreateTable_QunFile);}
		catch (SQLException e){logger.writelog("db",e.getMessage());/*e.printStackTrace();*/}	
		
		//创建文件目录
		if (!config.getDbFileDirectory().exists())
		{
			config.getDbFileDirectory().mkdir();
		}
		if (!config.getDbPhotoDirectory().exists())
		{
			config.getDbPhotoDirectory().mkdir();
		}
		
		try
		{
			if (conn != null) conn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
