package com.HIM.server;

import com.HIM.common.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * 数据库操作类
 * @Author:Zhangt2333
 */
public class Db_Operate 
{
	
	public static int register(Bean_userinfo bean)
	{
		Connection conn = null;
	    String sql = "INSERT INTO `UserInfo` "
	    			+ "(`Passwd`,`Nickname`,`Sex`,`Birthday`,`Constellation`,`ApplyDate`) "
	    			+ "VALUES(?,?,?,?,?,?);";
	    PreparedStatement pstmt = null;
	    int UserID=-1;
	    try 
	    {
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	    	String nowtime=tools.get_nowtime(false);
	        pstmt = (PreparedStatement) conn.prepareStatement(sql,
	        							Statement.RETURN_GENERATED_KEYS);
	        pstmt.setString(1, bean.getPasswd());
	        pstmt.setString(2, bean.getNickname());
	        pstmt.setInt(3, bean.getSex());
	        pstmt.setString(4, bean.getBirthday());
	        pstmt.setInt(5, bean.getConstellation());
	        pstmt.setString(6, nowtime);
	        if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        {
	        	//获取刚加入数据库的UserID
	        	ResultSet rs = pstmt.getGeneratedKeys();
	        	if (rs != null && rs.next())   
	        		UserID = rs.getInt(1);
	        	
	        	//添加默认分组
	        	if (UserID != -1)
	        	{
	        		if(pstmt!=null) pstmt.close();
	        		sql = "INSERT INTO `SubGroup` "
	        			+ "(`UserID`,`GroupName`) "
	        			+ "VALUES(?,?);";
        	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
        	        pstmt.setInt(1, UserID);
        	        pstmt.setString(2,config.DbDefaultSubgroupName);
        	        pstmt.executeUpdate();
	        	}
	        	
	        }
	        logger.writelog("db", "register:"+ UserID +","+bean);
	        if(pstmt!=null) pstmt.close();
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return UserID;
	}
	
	public static boolean login(int UserID,String passwd,String ip,int port)
	{
		Connection conn = null;
		String sql = "SELECT `Passwd` "
					+ "FROM `UserInfo` "
					+ "WHERE `UserID`=?;";
	    PreparedStatement pstmt = null;
		boolean ok = false;
	    try 
	    {
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1, UserID);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next() && rs.getString(1).equals(passwd))
	        {
	        	if(pstmt!=null) pstmt.close();
	        	sql = "INSERT INTO `LoginInfo` "
	        		+ "(`UserID`,`IP`,`Port`,`LoginTime`,`Status`) "
	        		+ "VALUES(?,?,?,?,?);";
	        	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        	pstmt.setInt(1,UserID);
	        	pstmt.setString(2,ip);
	        	pstmt.setInt(3, port);
	        	pstmt.setString(4, tools.get_nowtime(true));
	        	pstmt.setInt(5, 1);
	        	if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        		ok=true;
	        	logger.writelog("db", "login:" + UserID + (ok?"ok":"fail"));
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return ok;
	}
	
	public static boolean logout(int UserID)
	{
		Connection conn = null;
		String sql = "UPDATE `LoginInfo` "
					+ "SET `Status`=0 "
					+ "WHERE `UserID`=?;";
		PreparedStatement pstmt = null;
		boolean ok = false;
	    try 
	    {
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1,UserID);
	        if (pstmt.executeUpdate()>=1)//成功操作的信息条数
	        {
	        	ok=true;
	        	logger.writelog("db", "logout:" + UserID + (ok?"ok":"fail"));
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return ok;
	}
	
	public static ArrayList<Bean_subgroup> querySubGroup(int userID)
	{
		ArrayList<Bean_subgroup> ans = new ArrayList<>();
		Connection conn = null;
		String sql = "SELECT `GroupIndex`,`GroupName` "
					+ "FROM `SubGroup` "
					+ "WHERE `UserID`=?;";
		PreparedStatement pstmt = null;
		try
		{
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, userID);
			ResultSet rs = pstmt.executeQuery();
	        while(rs.next())
	        {
	        	ans.add(new Bean_subgroup(userID, rs.getInt(1), rs.getString(2)));
	        }
		} catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ans;
	}
	
	public static int addSubGroup(int userID, String subgroupname)
	{
		int groupindex = -1;
		Connection conn = null;
		String sql = "INSERT INTO `SubGroup` "
					+ "(`UserID`,`GroupName`) "
					+ "VALUES(?,?);";
	    PreparedStatement pstmt = null;
	    try 
	    {
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement) conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, userID);
			pstmt.setString(2, subgroupname);
			if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        {
	        	ResultSet rs = pstmt.getGeneratedKeys();
	        	if (rs != null && rs.next())   
	        		groupindex = rs.getInt(1);
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return groupindex;
	}
	
	
	
	public static boolean deleteSubGroup(int groupindex)
	{
		boolean ok = false;
		Connection conn = null;
		String sql1 = "DELETE FROM `SubGroup` "
					+ "WHERE `GroupIndex`=?;";
		String sql2 = "DELETE FROM `UserFriend` "
					+ "WHERE `GroupIndex`=?;";
	    PreparedStatement pstmt = null;
	    try 
	    {
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement) conn.prepareStatement(sql1);
			pstmt.setInt(1, groupindex);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = (PreparedStatement) conn.prepareStatement(sql2);
			pstmt.setInt(1, groupindex);
			pstmt.executeUpdate();
			ok = true;
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ok;
	}
	
	public static boolean updateSubGroupName(int groupindex,String groupname)
	{
		Connection conn = null;
		String sql = "UPDATE `SubGroup` SET `GroupName`=? WHERE `GroupIndex`=?;";
		PreparedStatement pstmt = null;
		boolean ok = false;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setString(1, groupname);
	        pstmt.setInt(2, groupindex);
	        if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        {
	        	ok=true;
	        	logger.writelog("db", "updateSubGroupName:"+ (ok?"ok":"fail"));
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return ok;
	}
	
	public static boolean deleteFriend(int userID,int friendID)
	{
		boolean ok = false;
		Connection conn = null;
		String sql = "DELETE FROM `UserFriend` "
				   + "WHERE `UserID`=? AND `FriendID`=?;";
	    PreparedStatement pstmt = null;
	    try 
	    {
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, userID);
			pstmt.setInt(2, friendID);
			pstmt.executeUpdate();
			ok = true;
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ok;
	}
	
	public static boolean addFriend(int userID,int friendID,int groupIndex,String alias)
	{
		boolean ok = false;
		Connection conn = null;
		String sql = "INSERT INTO `UserFriend` "
					+ "(`UserID`,`FriendID`,`GroupIndex`,`Alias`) "
					+ "VALUES(?,?,?,?);";
	    PreparedStatement pstmt = null;
	    try 
	    {
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, userID);
			pstmt.setInt(2, friendID);
			pstmt.setInt(3, groupIndex);
			pstmt.setString(4, alias);
			if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        {
	        	ok = true;
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ok;
	}
	
	public static boolean moveFriend(int userID,int friendID,int groupIndex)
	{
		boolean ok = false;
		Connection conn = null;
		String sql = "UPDATE `UserFriend` "
				   + "SET `GroupIndex`=? "
				   + "WHERE `UserID`=? AND `FriendID`=?;";
	    PreparedStatement pstmt = null;
	    try 
	    {
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, groupIndex);
			pstmt.setInt(2, userID);
			pstmt.setInt(3, friendID);
			pstmt.executeUpdate();
			ok = true;
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ok;
	}
	
	public static boolean updateFriendAlias(int userID,int friendID,String alias)
	{
		boolean ok = false;
		Connection conn = null;
		String sql = "UPDATE `UserFriend` "
				   + "SET `Alias`=? "
				   + "WHERE `UserID`=? AND `FriendID`=?;";
	    PreparedStatement pstmt = null;
	    try 
	    {
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, alias);
			pstmt.setInt(2, userID);
			pstmt.setInt(3, friendID);
			if(pstmt.executeUpdate()==1)
				ok = true;
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ok;
	}
	
	
	public static Bean_userinfo queryOwnInfo(int UserID)
	{
		Connection conn = null;
	    String sql1 = "SELECT `PhotoIndex`,`Sign`,`Nickname`,`Sex`,`Birthday`,`Constellation`,`ApplyDate` "
	    			+ "FROM `UserInfo` WHERE `UserID`=?;";
	    PreparedStatement pstmt1 = null;
	    Bean_userinfo bean = null;
	    try 
	    {
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt1 = (PreparedStatement) conn.prepareStatement(sql1);
	        pstmt1.setInt(1, UserID);
	        ResultSet rs1 = pstmt1.executeQuery();
	        if (rs1.next())
	        {
	        	bean = new Bean_userinfo(UserID, rs1.getInt(1),rs1.getString(2), rs1.getInt(4),
	        							rs1.getString(3), rs1.getString(5), rs1.getInt(6),
	        							rs1.getString(7));
	        	logger.writelog("db", "queryOwnInfo:"+UserID);
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt1!=null) pstmt1.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return bean;
	}
	
	public static Bean_friendinfo queryUser_userID(int UserID)
	{
		Bean_friendinfo bean = null;
		Connection conn = null;
	    String sql1 = "SELECT `PhotoIndex`,`Sign`,`Nickname`,`Sex`,`Birthday`,`Constellation`,`ApplyDate` "
	    				+ "FROM `UserInfo` WHERE `UserID`=?;";
	    String sql2 = "SELECT `IP`,`Port` FROM `LoginInfo` WHERE `UserID`=? AND `Status`=1;";
	    PreparedStatement pstmt1 = null;
	    PreparedStatement pstmt2 = null;
	    try 
	    {
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt1 = (PreparedStatement) conn.prepareStatement(sql1);
	        pstmt1.setInt(1, UserID);
	        pstmt2 = (PreparedStatement) conn.prepareStatement(sql2);
	        pstmt2.setInt(1, UserID);
	        ResultSet rs1 = pstmt1.executeQuery();
	        ResultSet rs2 = pstmt2.executeQuery();
	        if (rs1.next())
	        {
	        	bean = new Bean_friendinfo(UserID, rs1.getInt(1), rs1.getString(2), 
		        			rs1.getString(3),rs1.getInt(4), rs1.getString(5), 
		        			rs1.getInt(6),  rs1.getString(7));
	        	if (rs2.next()) bean.setNet(rs2.getString(1), rs2.getInt(2));
	        	logger.writelog("db", "queryUser_UserID:" +UserID );
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt1!=null) pstmt1.close();
				if(pstmt2!=null) pstmt2.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return bean;
	}
	
	public static ArrayList<Bean_friendinfo> queryUser_nickname(String nickname)
	{
		ArrayList<Bean_friendinfo> ans = new ArrayList<Bean_friendinfo>();
		Connection conn = null;
	    String sql1 = "SELECT `PhotoIndex`,`Sign`,`Nickname`,`Sex`,`Birthday`,`Constellation`,`ApplyDate`,`UserID` "
	    				+ "FROM `UserInfo` WHERE `Nickname` like \"%" + nickname + "%\";";
	    String sql2 = "SELECT `IP`,`Port` FROM `LoginInfo` WHERE `UserID`=? AND `Status`=1;";
	    PreparedStatement pstmt1 = null;
	    PreparedStatement pstmt2 = null;
	    Bean_friendinfo bean = null;
	    ResultSet rs1,rs2;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt1 = (PreparedStatement) conn.prepareStatement(sql1);
	        pstmt2 = (PreparedStatement) conn.prepareStatement(sql2);
	        rs1 = pstmt1.executeQuery();
	        while (rs1.next())
	        {
	        	bean = new Bean_friendinfo(rs1.getInt(8), rs1.getInt(1), rs1.getString(2), 
		        			rs1.getString(3),rs1.getInt(4), rs1.getString(5), 
		        			rs1.getInt(6),  rs1.getString(7));
	        	pstmt2.setInt(1, rs1.getInt(8));
	        	rs2 = pstmt2.executeQuery();
	        	if (rs2.next()) bean.setNet(rs2.getString(1), rs2.getInt(2));
	        	ans.add(bean);
	        }
	        logger.writelog("db", "queryUser_nickname:" + nickname);
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt1!=null) pstmt1.close();
				if(pstmt2!=null) pstmt2.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	   return ans;
	}
	
	public static ArrayList<Bean_friendinfo> queryUsers(ArrayList<Integer> list)
	{
		ArrayList<Bean_friendinfo> ans = new ArrayList<Bean_friendinfo>();
		Connection conn = null;
	    String sql1 = "SELECT `PhotoIndex`,`Sign`,`Nickname`,`Sex`,`Birthday`,`Constellation`,`ApplyDate`,`UserID` "
	    				+ "FROM `UserInfo` WHERE `UserID`=?;";
	    String sql2 = "SELECT `IP`,`Port` FROM `LoginInfo` WHERE `UserID`=? AND `Status`=1;";
	    PreparedStatement pstmt1 = null;
	    PreparedStatement pstmt2 = null;
	    Bean_friendinfo bean = null;
	    ResultSet rs1,rs2;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt1 = (PreparedStatement) conn.prepareStatement(sql1);
	        pstmt2 = (PreparedStatement) conn.prepareStatement(sql2);
	        for(Iterator<Integer> item = list.iterator();item.hasNext();)
	        {
	        	pstmt1.setInt(1, item.next());
	        	rs1 = pstmt1.executeQuery();
	 	        while (rs1.next())
	 	        {
	 	        	bean = new Bean_friendinfo(rs1.getInt(8), rs1.getInt(1), rs1.getString(2), 
	 		        			rs1.getString(3),rs1.getInt(4), rs1.getString(5), 
	 		        			rs1.getInt(6),  rs1.getString(7));
	 	        	pstmt2.setInt(1, rs1.getInt(8));
	 	        	rs2 = pstmt2.executeQuery();
	 	        	if (rs2.next()) bean.setNet(rs2.getString(1), rs2.getInt(2));
	 	        	ans.add(bean);
	 	        }
	        }
	        logger.writelog("db", "queryUsers");
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt1!=null) pstmt1.close();
				if(pstmt2!=null) pstmt2.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	   return ans;
	}
	
	public static ArrayList<Bean_friendinfo> queryUserFriends(int userID)
	{
		ArrayList<Bean_friendinfo> ans = new ArrayList<Bean_friendinfo>();
		Connection conn = null;
		String sql1 = "SELECT `FriendID`,`GroupIndex`,`Alias` FROM `UserFriend` WHERE `UserID`=?;";
	    String sql2 = "SELECT `PhotoIndex`,`Sign`,`Nickname`,`Sex`,`Birthday`,`Constellation`,`ApplyDate` "
	    				+ "FROM `UserInfo` WHERE `UserID`=?;";
	    String sql3 = "SELECT `IP`,`Port` FROM `LoginInfo` WHERE `UserID`=? AND `Status`=1;";
	    PreparedStatement pstmt1 = null,pstmt2 = null,pstmt3 = null;
	    Bean_friendinfo bean = null;
	    ResultSet rs1,rs2,rs3;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	    	pstmt1 = (PreparedStatement) conn.prepareStatement(sql1);
	        pstmt2 = (PreparedStatement) conn.prepareStatement(sql2);
	        pstmt3 = (PreparedStatement) conn.prepareStatement(sql3);
	        pstmt1.setInt(1, userID);
	        rs1 = pstmt1.executeQuery();
	        int friendID;
	        while(rs1.next())
	        {
	        	friendID = rs1.getInt(1);
	        	pstmt2.setInt(1, friendID);
	        	rs2 = pstmt2.executeQuery();
	        	if (rs2.next())
	        	{
	        		bean = new Bean_friendinfo(friendID, rs2.getInt(1), rs2.getString(2), 
	        				rs2.getString(3),rs2.getInt(4), rs2.getString(5), 
	        				rs2.getInt(6),  rs2.getString(7));
	        		
	        		pstmt3.setInt(1, friendID);
	        		rs3 = pstmt3.executeQuery();
	        		if (rs3.next()) bean.setNet(rs3.getString(1), rs3.getInt(2));
	        		bean.setGroupIndex(rs1.getInt(2));
	        		bean.setAlias(rs1.getString(3));
	        		ans.add(bean);	        		
	        	}
	        }
	        logger.writelog("db", "queryUserFriends");
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt2!=null) pstmt2.close();
				if(pstmt3!=null) pstmt3.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	   return ans;
	}
	
	public static boolean updateOwnInfo(Bean_userinfo bean)
	{
		Connection conn = null;
		String sql = "UPDATE `UserInfo` SET `Sign`=?,`Sex`=?,`Nickname`=?,"
						+ "`Birthday`=?,`Constellation`=? WHERE `UserID`=?;";
		PreparedStatement pstmt = null;
		boolean ok = false;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setString(1, bean.getSign());
	        pstmt.setInt(2, bean.getSex());
	        pstmt.setString(3, bean.getNickname());
	        pstmt.setString(4, bean.getBirthday());
	        pstmt.setInt(5,bean.getConstellation());
	        pstmt.setInt(6, bean.getUserID());
	        if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        {
	        	ok=true;
	        	logger.writelog("db", "updateOwnInfo:"+ bean + (ok?"ok":"fail"));
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return ok;
	}
	
	public static boolean updatePasswd(int UserID,String oldpasswd,String newpasswd)
	{
		Connection conn = null;
		String sql = "SELECT `Passwd` FROM `UserInfo` WHERE `UserID`=?;";
		String trueoldpasswd = null;
		PreparedStatement pstmt = null;
		boolean ok = false;
		try
		{
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
	    	pstmt.setInt(1, UserID);
	        ResultSet rs = pstmt.executeQuery();
	    	while(rs.next()) trueoldpasswd = rs.getString(1);
		}catch (SQLException e) 
	    {
	        e.printStackTrace();
	    }
		
	    try 
	    {
	    	if (trueoldpasswd.equals(oldpasswd))
	    	{
	    		sql = "UPDATE `UserInfo` SET `Passwd`=? WHERE `UserID`=?;";
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        pstmt.setString(1, newpasswd);
		        pstmt.setInt(2, UserID);
		        if (pstmt.executeUpdate()==1)//成功操作的信息条数
		        {
		        	ok=true;
		        	logger.writelog("db", "updatePasswd:"+ UserID + (ok?"ok":"fail"));
		        }
	    	}
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return ok;
	}
	
	public static boolean updatePhotoindex(int UserID,int newphotoindex)
	{
		Connection conn = null;
		String sql = "UPDATE `UserInfo` SET `PhotoIndex`=? WHERE `UserID`=?;";
		PreparedStatement pstmt = null;
		boolean ok = false;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1, newphotoindex);
	        pstmt.setInt(2, UserID);
	        if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        {
	        	ok=true;
	        	logger.writelog("db", "updatePhotoindex:"+ UserID + (ok?"ok":"fail"));
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return ok;
	}
	
	public static int queryFile_md5(String md5)
	{
		int infoindex = -1;
		Connection conn = null;
		String sql = "SELECT `InfoIndex` FROM `FileInfo` WHERE `MD5`=?;";
		PreparedStatement pstmt = null;		
		try
		{
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement) conn.prepareStatement(sql);	        
			pstmt.setString(1, md5);
	        ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				infoindex = rs.getInt(1);		
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return infoindex;
	}
	
	public static Bean_fileinfo queryFile_index(int infoindex)
	{
		Bean_fileinfo bean = null;
		Connection conn = null;
		String sql = "SELECT `Type`,`Size`,`MD5`,`Name`,`Path` FROM `FileInfo` WHERE `InfoIndex`=?;";
		PreparedStatement pstmt = null;		
		try
		{
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement) conn.prepareStatement(sql);	        
			pstmt.setInt(1, infoindex);
	        ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				bean = new Bean_fileinfo(rs.getString(1),rs.getString(2), 
										rs.getString(3),rs.getString(4),
										rs.getString(5));
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return bean;
	}
	
	public static int queryPhoto_md5(String MD5)
	{
		int infoindex = -1;
		Connection conn = null;
		String sql = "SELECT `InfoIndex` FROM `PhotoInfo` WHERE `MD5`=?;";
		PreparedStatement pstmt = null;		
		try
		{
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement) conn.prepareStatement(sql);	        
			pstmt.setString(1, MD5);
	        ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				infoindex = rs.getInt(1);		
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return infoindex;
	}
	
	public static String queryPhoto_index(int infoindex)
	{
		String AbsolutePath = "";
		Connection conn = null;
		String sql = "SELECT `Path` FROM `PhotoInfo` WHERE `InfoIndex`=?;";
		PreparedStatement pstmt = null;		
		try
		{
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement) conn.prepareStatement(sql);	        
			pstmt.setInt(1, infoindex);
	        ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				AbsolutePath = rs.getString(1);
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return AbsolutePath;
	}
	
	public static boolean addFileInfo(Bean_fileinfo bean) 
	{
		boolean ok = false;
		Connection conn = null;
	    String sql = "INSERT INTO `FileInfo` "
	    			+ "(`Type`,`Size`,`MD5`,`Name`,`Path`) "
	    			+ "VALUES(?,?,?,?,?);";
	    PreparedStatement pstmt = null;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setString(1, bean.getType());
	        pstmt.setString(2, bean.getSize());
	        pstmt.setString(3, bean.getMD5());
	        pstmt.setString(4, bean.getName());
	        pstmt.setString(5, bean.getPath());
	        if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        {
	        	ok=true;
	        	logger.writelog("db", "addFileInfo:" + bean + (ok?"ok":"fail"));
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ok;
	}
	
	
	public static int addPhotoInfo(String MD5,String AbsolutePath)
	{
		int photoindex = Db_Operate.queryPhoto_md5(MD5);
		if (photoindex != -1) return photoindex;
		Connection conn = null;
	    String sql = "INSERT INTO `PhotoInfo` "
	    			+ "(`MD5`,`Path`) "
	    			+ "VALUES(?,?);";
	    PreparedStatement pstmt = null;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql,
	        							Statement.RETURN_GENERATED_KEYS);
	        pstmt.setString(1, MD5);
	        pstmt.setString(2, AbsolutePath);
	        if (pstmt.executeUpdate() == 1)//成功操作的信息条数
	        {
	        	ResultSet rs = pstmt.getGeneratedKeys();
	        	if (rs != null && rs.next())   
	        		photoindex = rs.getInt(1);
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return photoindex;
	}
	
	public static boolean addChatRecord(Bean_message message ,boolean readed)
	{
		boolean ok = false;
		Connection conn = null;
	    String sql = "INSERT INTO `ChatRecord` "
	    			+ "(`Sender`,`SendTime`,`Receiver`,`QunID`,`Type`,`Content`,`Readed`) "
	    			+ "VALUES(?,?,?,?,?,?,?);";
	    PreparedStatement pstmt = null;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1, message.getSender());
	        pstmt.setString(2, message.getTime());
	        pstmt.setInt(3, message.getReceiver());
			pstmt.setInt(4, message.getQunnum());
	        pstmt.setString(5, message.getType());
	        String content = message.getContent();
	        if (content == null) content = "";
	        pstmt.setString(6, content);
	        pstmt.setBoolean(7, readed);
	        if (pstmt.executeUpdate() == 1)//成功操作的信息条数
	        	ok=true;
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ok;
	}
	
	public static ArrayList<Bean_message> queryUnreadedMessages(int receiver)
	{
		ArrayList<Bean_message> list = new ArrayList<>();
		Connection conn = null;
	    String sql1 = "SELECT `Sender`,`QunID`,`SendTime`,`Type`,`Content` "
	    			+ "FROM `ChatRecord` WHERE `Receiver`=? AND `Readed`=0;";
	    PreparedStatement pstmt1 = null;
	    ResultSet rs1;
	    try
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt1 = (PreparedStatement) conn.prepareStatement(sql1);
	        pstmt1.setInt(1, receiver);
	        rs1 = pstmt1.executeQuery();
	        Bean_message bean=null;
	    	while(rs1.next()) 
	    	{
	    		bean = new Bean_message(rs1.getInt(1),receiver,
	    								rs1.getString(4),rs1.getString(3),
	    								rs1.getString(5));
	    		bean.setQunnum(rs1.getInt(2));
	    		list.add(bean);
	    	}
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt1!=null) pstmt1.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static void updateChatRecordReaded(int receiver,String type)
	{
		Connection conn = null;
		String sql = "UPDATE `ChatRecord` SET `Readed`=? WHERE `Receiver`=?;";
		PreparedStatement pstmt = null;
	    try
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setBoolean(1, true);
	        pstmt.setInt(2, receiver);
	        pstmt.executeUpdate();
	       	logger.writelog("db", "updateChatRecordReaded:"+ receiver);
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static Bean_quninfo queryQunInfo_qunID(int QunID)
	{
		Bean_quninfo quninfo = null;
		Connection conn = null;
		String sql = "SELECT `QunName`,`PhotoIndex`,`CreateTime` FROM `QunInfo` WHERE `QunID`=?;";
		PreparedStatement pstmt = null;	
		try
		{
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement) conn.prepareStatement(sql);	        
			pstmt.setInt(1, QunID);
	        ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				quninfo = new Bean_quninfo(QunID, rs.getString(1),rs.getInt(2),rs.getString(3));
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return quninfo;	
	}
	
	
	public static ArrayList<Bean_quninfo> queryQunInfo_qunname(String qunname)
	{
		ArrayList<Bean_quninfo> list = new ArrayList<>();
		Connection conn = null;
	    String sql = "SELECT `QunID`,`QunName`,`PhotoIndex`,`CreateTime` FROM `QunInfo` "
	    			+  "WHERE `QunName` like \"%" + qunname + "%\";";
	    PreparedStatement pstmt=null;
	    ResultSet rs;
	    try
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();
	    	while(rs.next()) 
	    	{
	    		list.add(new Bean_quninfo(rs.getInt(1), rs.getString(2),
	    								  rs.getInt(3), rs.getString(4)));    		
	    	}    	
	    	logger.writelog("db", "queryQunInfo_qunname");
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return list;
	}
	
		
	public static ArrayList<Bean_friendinfo> queryQunMembers(int QunID)
	{
		ArrayList<Bean_friendinfo> ans = new ArrayList<Bean_friendinfo>();
		Connection conn = null;
	    String sql1 = "SELECT `Relation`,`UserID`,`Alias`,`JoinTime` FROM `UserQun` WHERE `QunID`=?;";
	    String sql2 = "SELECT `PhotoIndex`,`Sign`,`Nickname`,`Sex`,`Birthday`,`Constellation`,`ApplyDate` "
	    				+ "FROM `UserInfo` WHERE `UserID`=?;";
	    String sql3 = "SELECT `IP`,`Port` FROM `LoginInfo` WHERE `UserID`=? AND `Status`=1;";
	    PreparedStatement pstmt1 = null;
	    PreparedStatement pstmt2 = null;
	    PreparedStatement pstmt3 = null;
	    ResultSet rs1,rs2,rs3;
	    try
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt1 = (PreparedStatement) conn.prepareStatement(sql1);
	        pstmt2 = (PreparedStatement) conn.prepareStatement(sql2);
	        pstmt3 = (PreparedStatement) conn.prepareStatement(sql3);
	        pstmt1.setInt(1, QunID);
	        rs1 = pstmt1.executeQuery();
	        Bean_friendinfo bean=null;
	    	while(rs1.next()) 
	    	{
	    		pstmt2.setInt(1, rs1.getInt(2));
	    		pstmt3.setInt(1, rs1.getInt(2));
	    		rs2 = pstmt2.executeQuery();
	    		rs3 = pstmt3.executeQuery();
	    		if (rs2.next())
	    		{
	    			bean = new Bean_friendinfo(rs1.getInt(2), rs2.getInt(1), rs2.getString(2), rs2.getString(3)
	    									   , rs2.getInt(4), rs2.getString(5), rs2.getInt(6), rs2.getString(7));
	    			bean.setAlias(rs1.getString(3));
	    		}
	    		if (rs3.next() && bean!=null) 
	    			bean.setNet(rs3.getString(1), rs3.getInt(2));
	    		ans.add(bean);
	    	}    	
	    	logger.writelog("db", "queryQunMembers:" + QunID);
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt1!=null) pstmt1.close();
				if(pstmt2!=null) pstmt2.close();
				if(pstmt3!=null) pstmt3.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ans;
	}
	
	public static ArrayList<Bean_quninfo> queryUserQuns(int UserID)
	{
		ArrayList<Bean_quninfo> list = new ArrayList<>();
		Connection conn = null;
	    String sql1 = "SELECT `QunID` FROM `UserQun` WHERE `UserID`=?;";
	    String sql2 = "SELECT `QunName`,`PhotoIndex`,`CreateTime` FROM `QunInfo` WHERE `QunID`=?;";
	    PreparedStatement pstmt1=null,pstmt2=null;
	    ResultSet rs1,rs2;
	    try
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt1 = (PreparedStatement) conn.prepareStatement(sql1);
	        pstmt1.setInt(1, UserID);
	        rs1 = pstmt1.executeQuery();
	        Bean_quninfo bean=null;
	        pstmt2 = (PreparedStatement) conn.prepareStatement(sql2);
	    	while(rs1.next()) 
	    	{
	    		pstmt2.setInt(1, rs1.getInt(1));
	    		rs2 = pstmt2.executeQuery();
	    		if (rs2.next())
	    		{
	    			bean = new Bean_quninfo(rs1.getInt(1), rs2.getString(1), rs2.getInt(2), rs2.getString(3));
					list.add(bean);
	    		}	    		
	    	}    	
	    	logger.writelog("db", "queryUserQuns:" + UserID);
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt1!=null) pstmt1.close();
				if(pstmt2!=null) pstmt2.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	public static int createQun(int UserID,String qunname)
	{
		int QunID = -1;
		Connection conn = null;
	    String sql1 = "INSERT INTO `QunInfo` "
	    			+ "(`QunName`,`CreateTime`) "
	    			+ "VALUES(?,?);";
	    String sql2 = "INSERT INTO `UserQun` "
	    			+ "(`Relation`,`UserID`,`QunID`,`JoinTime`) "
	    			+ "VALUES(?,?,?,?);";
	    PreparedStatement pstmt1 = null;
	    PreparedStatement pstmt2 = null;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	    	pstmt1 = (PreparedStatement) conn.prepareStatement(sql1,
											Statement.RETURN_GENERATED_KEYS);
	        String nowdate = tools.get_nowtime(false);
	    	pstmt1.setString(1, qunname);
	        pstmt1.setString(2, nowdate);
	        if (pstmt1.executeUpdate()==1)//成功操作的信息条数
	        {
	        	ResultSet rs1 = pstmt1.getGeneratedKeys();
	        	if (rs1 != null && rs1.next())   
	        		QunID = rs1.getInt(1);
	        	if (QunID != -1)
	        	{
	        		pstmt2 = (PreparedStatement) conn.prepareStatement(sql2);
	        		pstmt2.setInt(1, 1);
	        		pstmt2.setInt(2, UserID);
	        		pstmt2.setInt(3, QunID);
	        		pstmt2.setString(4, nowdate);
	        		pstmt2.executeUpdate();
	        	}
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if (pstmt1 != null) pstmt1.close();
				if (pstmt2 != null) pstmt2.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return QunID;
	}
	
	public static boolean addQun(int UserID,int QunID)
	{
		boolean ok = false;
		Connection conn = null;
	    String sql = "INSERT INTO `UserQun` "
	    			+ "(`Relation`,`UserID`,`QunID`,`JoinTime`) "
	    			+ "VALUES(?,?,?,?);";
	    PreparedStatement pstmt = null;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1, 2);
	        pstmt.setInt(2, UserID);
	        pstmt.setInt(3, QunID);
	        pstmt.setString(4, tools.get_nowtime(false));
	        if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        {
	        	ok=true;
	        	logger.writelog("db", "addQun:" +UserID+"->"+QunID + (ok?"ok":"fail"));
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ok;
	}
	
	public static boolean quitQun(int UserID,int QunID)
	{
		boolean ok = false;
		Connection conn = null;
	    String sql = "DELETE FROM `UserQun` "
	    			+ "WHERE `UserID`=? AND `QunID`=?;";
	    PreparedStatement pstmt = null;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1, UserID);
	        pstmt.setInt(2, QunID);
	        if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        {
	        	ok=true;
	        	logger.writelog("db", "quitQun:" +UserID+"->"+QunID + (ok?"ok":"fail"));
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ok;
	}
	
	public static boolean updateQunInfo(Bean_quninfo quninfo)
	{
		boolean ok = false;
		Connection conn = null;
		String sql = "UPDATE `QunInfo` SET `QunName`=?,`PhotoIndex`=?"
					+ " WHERE `QunID`=?;";
		PreparedStatement pstmt = null;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
	    	pstmt.setString(1, quninfo.getQunName());
	    	pstmt.setInt(2,quninfo.getPhotoIndex());
	    	pstmt.setInt(3, quninfo.getQunID());
	        if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        {
	        	ok=true;
	        	logger.writelog("db", "updateQunInfo:"+ quninfo.getQunID() + (ok?"ok":"fail"));
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return ok;		
	}
	
	public static boolean updateUserQun(int UserID,int QunID,String notename)
	{
		boolean ok = false;
		Connection conn = null;
		String sql = "UPDATE `UserQun` SET `Alias`=?"
							+ " WHERE `UserID`=? AND `QunID`=?;";
		PreparedStatement pstmt = null;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
			pstmt = (PreparedStatement)conn.prepareStatement(sql);
	    	pstmt.setString(1, notename);
	    	pstmt.setInt(2,UserID);
	    	pstmt.setInt(3, QunID);
	        if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        {
	        	ok=true;
	        	logger.writelog("db", "updateUserQun:"+ UserID + (ok?"ok":"fail"));
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return ok;
	}
	
	public static ArrayList<Bean_message> queryChatRecords(int UserID,int friendID)
	{
		ArrayList<Bean_message> ans = new ArrayList<>();
		Connection conn = null;
	    String sql = "SELECT `Index`,`Sender`,`Receiver`,`Type`,`SendTime`,`Content` FROM `ChatRecord` "
	    		    + "WHERE ((`Sender`=? AND `Receiver`=?) OR (`Sender`=? AND `Receiver`=?)) AND (`Type`=? OR `Type`=? OR `Type`=?);";
	    PreparedStatement pstmt=null;
	    ResultSet rs;
	    try
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1, UserID);
	        pstmt.setInt(2, friendID);
	        pstmt.setInt(3, friendID);
	        pstmt.setInt(4, UserID);	        
	        pstmt.setString(5, Bean_message.TYPE_SendMessage);
	        pstmt.setString(6, Bean_message.TYPE_SendPicture);
	        pstmt.setString(7, Bean_message.TYPE_SendVideo);
	        rs = pstmt.executeQuery();
	        Bean_message message;
	    	while(rs.next()) 
	    	{
	    		message = new Bean_message(rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), 
	    								   rs.getString(6));
	    		message.setIndex(rs.getInt(1));
	    		ans.add(message);
	    	}
	    	logger.writelog("db", "queryChatRecord:" + UserID + "->" + friendID);
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ans;
	}
	
	public static ArrayList<Bean_message> queryQunChatRecords(int UserID,int QunID)
	{
		ArrayList<Bean_message> ans = new ArrayList<>();
		Connection conn = null;
	    String sql = "SELECT `Index`,`Sender`,`Type`,`SendTime`,`Content` FROM `ChatRecord` "
	    		    + "WHERE `Receiver`=? AND `QunID`=? AND (`Type`=? OR `Type`=? OR `Type`=?);";
	    PreparedStatement pstmt=null;
	    ResultSet rs;
	    try
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1, UserID);
	        pstmt.setInt(2, QunID);
	        pstmt.setString(3, Bean_message.TYPE_Send2Qun);
	        pstmt.setString(4, Bean_message.TYPE_Send2Qun_Picture);
	        pstmt.setString(5, Bean_message.TYPE_Send2Qun_Video);
	        rs = pstmt.executeQuery();
	        Bean_message message;
	    	while(rs.next()) 
	    	{
		    		message = new Bean_message(rs.getInt(2), QunID, 
							 				   rs.getString(3), rs.getString(4), 
							 				   rs.getString(5));
		    		message.setIndex(rs.getInt(1));
		    		ans.add(message);
	    	}
	    	logger.writelog("db", "queryQunChatRecord:" + QunID);
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ans;
	}

	public static boolean postMood(Bean_mood mood)
	{
		boolean ok = false;
		Connection conn = null;
	    String sql = "INSERT INTO `MoodRecord` "
    				+ "(`Poster`,`PostTime`,`Content`) "
    				+ "VALUES(?,?,?);";
	    PreparedStatement pstmt = null;
	    try
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1, mood.getPoster());
	        pstmt.setString(2, mood.getPosttime());
	        pstmt.setString(3, mood.getContent());
	        if (pstmt.executeUpdate() == 1)
	        	ok = true;
	    	logger.writelog("db", "postMood:" + mood.getPoster());
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return ok;
	}
	
	public static ArrayList<Bean_mood> queryMoods(ArrayList<Integer> posters)
	{
		ArrayList<Bean_mood> ans = new ArrayList<>();
		Connection conn = null;
	    String sql = "SELECT `Index`,`PostTime`,`Content` FROM `MoodRecord` WHERE `Poster`=?;";
		PreparedStatement pstmt = null;
		ResultSet rs;
		try
		{
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        Bean_mood mood;
	        int poster;
			for(Iterator<Integer> it = posters.iterator() ; it.hasNext();)
			{
				poster = it.next();
				pstmt.setInt(1, poster);
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					mood = new Bean_mood(rs.getInt(1), poster, rs.getString(2), rs.getString(3));
					ans.add(mood);
				}
			}
		} catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ans;
	}
	
	public static boolean addQunfile(int uploader,int QunID,int fileindex)
	{
		boolean ok = false;
		Connection conn = null;
	    String sql = "INSERT INTO `QunFile` "
    				+ "(`QunID`,`Uploader`,`UploadTime`,`FileIndex`) "
    				+ "VALUES(?,?,?,?);";
	    PreparedStatement pstmt = null;
	    try
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1, QunID);
	        pstmt.setInt(2, uploader);
	        pstmt.setString(3, tools.get_nowtime(false));
	        pstmt.setInt(4, fileindex);
	        if (pstmt.executeUpdate() == 1)
	        	ok = true;
	    	logger.writelog("db", "addQunfile:" + uploader + "->" + QunID + "-"+fileindex);
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	    return ok;
	}
	
	public static boolean deleteQunfile(int QunID,int fileindex)
	{
		boolean ok = false;
		Connection conn = null;
	    String sql = "DELETE FROM `QunFile` "
	    			+ "WHERE `QunID`=? AND `FileIndex`=?;";
	    PreparedStatement pstmt = null;
	    try 
	    {
	    	conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setInt(1, QunID);
	        pstmt.setInt(2, fileindex);
	        if (pstmt.executeUpdate()==1)//成功操作的信息条数
	        {
	        	ok=true;
	        	logger.writelog("db", "deleteQunfile:" +QunID+":"+fileindex);
	        }
	    } catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt!=null) pstmt.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ok;
	}
	
	public static ArrayList<Bean_fileinfo> queryQunfiles(int QunID)
	{
		ArrayList<Bean_fileinfo> ans = new ArrayList<>();
		Connection conn = null;
	    String sql1 = "SELECT `Uploader`,`UploadTime`,`FileIndex` FROM `QunFile` WHERE `QunID`=?;";
	    String sql2 = "SELECT `Type`,`Size`,`MD5`,`Name`,`Path` FROM `FileInfo` WHERE `InfoIndex`=?;";
	    PreparedStatement pstmt1 = null,pstmt2 = null;
		ResultSet rs1,rs2;
		try
		{
			conn = Db_C3P0_ConnectionPoolUtil.getConnection();
	        pstmt1 = (PreparedStatement) conn.prepareStatement(sql1);
	        pstmt2 = (PreparedStatement) conn.prepareStatement(sql2);
	        Bean_fileinfo file;
			pstmt1.setInt(1, QunID);
			rs1 = pstmt1.executeQuery();
			int fileindex;
			while (rs1.next())
			{
				fileindex = rs1.getInt(3);
				pstmt2.setInt(1, fileindex);
				rs2 = pstmt2.executeQuery();
				if (rs2.next())
				{
					file = new Bean_fileinfo(rs2.getString(1),rs2.getString(2), 
											rs2.getString(3),rs2.getString(4),
											rs2.getString(5));
					file.setUploader(rs1.getInt(1));
					file.setUploadTime(rs1.getString(2));
					file.setFileindex(fileindex);
					ans.add(file);
				}
			}
		} catch (SQLException e) 
	    {
	        e.printStackTrace();
	    } finally 
	    {
			try
			{
				if(pstmt1 != null) pstmt1.close();
				if(pstmt2 != null) pstmt1.close();
				if(conn != null) conn.close();
			}catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return ans;
	}
	
	
}
