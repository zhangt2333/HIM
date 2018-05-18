package com.HIM.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Bean_friendinfo implements Serializable
{
	private int userID;
	private int friendID;
	private int photoindex;
	private String nickname;
	private String alias;
	private int sex;
	private String birthday;
	private int constellation;
	private String applydate;
	private String sign;
	private int status = 0;
	private String ip;
	private int port;
	private int GameNum;
	private int groupIndex;
	
	//加好友专用构造
	public Bean_friendinfo(int friendID,String alias,int userID)
	{
		this.friendID=friendID;
		this.alias=alias;
		this.userID=userID;
	}
	
	
	//查询好友列表专用构造
	public Bean_friendinfo(int userID,int friendID,int photoindex,String sign,String nickname,
						   String alias,int sex,String birthday,int constellation,String applydate)
	{
		this.userID=userID;
		this.friendID=friendID;
		this.photoindex=photoindex;
		this.sign=sign;
		this.nickname=nickname;
		this.alias=alias;
		this.sex=sex;
		this.birthday=birthday;
		this.constellation=constellation;
		this.applydate=applydate;	
	}
	
	//查询用户构造
	public Bean_friendinfo(int friendID,int photoindex,String sign,String nickname,
					int sex,String birthday,int constellation,String applydate)
	{
		this.friendID=friendID;
		this.photoindex=photoindex;
		this.sign=sign;
		this.nickname=nickname;
		this.sex=sex;
		this.birthday=birthday;
		this.constellation=constellation;
		this.applydate=applydate;
	}
	
	public void setNet(String ip,int port)
	{
		this.ip=ip;
		this.port=port;
		this.status=1;
	}
	
	public int getUserID() {return userID;}
	public int getFriendID() {return friendID;}
	public int getPhotoindex() {return photoindex;}
	public String getNickname() {return nickname;}
	public String getAlias() {return alias;}
	public int getstatus() {return status;}
	public int getSex() {return sex;}
	public String getBirthday() {return birthday;}
	public int getConstellation() {return constellation;}
	public String getApplydate() {return applydate;}
	public String getSign() { return sign;}
	public String getIP() { return ip;}
	public int getPort() {return port;}
	public int getGameNum() { return GameNum; }


	public final int getGroupIndex()
	{
		return groupIndex;
	}


	public void setGameNum(int gameNum) { GameNum = gameNum; }
	public void setUserID(int userID) {this.userID=userID;}
	public void setFriendID(int friendID) {this.friendID=friendID;}
	public void setPhotoindex(int photoindex) {this.photoindex=photoindex;}
	public void setNickname(String nickname) {this.nickname=nickname;}
	public void setAlias(String alias) {this.alias=alias;}
	public void setSex(int sex) {this.sex=sex;}
	public void setBirthday(String birthday) {this.birthday=birthday;}
	public void setConstellation(int constellation) {this.constellation=constellation;}
	public void setSign(String sign) {this.sign=sign;}

	

	public final void setGroupIndex(int groupIndex)
	{
		this.groupIndex = groupIndex;
	}


	@Override
	public String toString()
	{
		return "Bean_friendinfo [userID=" + userID + ", friendID=" + friendID + ", photoindex=" + photoindex
				+ ", nickname=" + nickname + ", alias=" + alias + ", sex=" + sex + ", birthday=" + birthday
				+ ", constellation=" + constellation + ", applydate=" + applydate + ", sign=" + sign + ", status="
				+ status + ", ip=" + ip + ", port=" + port + ", GameNum=" + GameNum + ", groupIndex=" + groupIndex
				+ "]";
	}


	
	
	
	
}
