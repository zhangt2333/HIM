package com.HIM.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Bean_userinfo implements Serializable
{
	private String passwd;
	private String nickname;
	private int sex;
	private String birthday;
	private int constellation;
	private String sign;
	private int userID;
	private int photoIndex;
	private String applydate;
	
	//注册专用构造方法
	public Bean_userinfo(String passwd,String nickname,int sex,
						 String birthday,int constellation) 
	{
		this.passwd=passwd;
		this.nickname=nickname;
		this.sex=sex;
		this.birthday=birthday;
		this.constellation=constellation;
	}
	
	//更新个人信息专用
	public Bean_userinfo(int userID,String sign,int sex,String nickname,String birthday,int constellation)
	{
		this.userID=userID;
		this.sign=sign;
		this.sex=sex;
		this.nickname=nickname;
		this.birthday=birthday;
		this.constellation=constellation;
	}
	
	//获取个人信息专用
	public Bean_userinfo(int userID,int photoindex,String sign,int sex,String nickname,String birthday,int constellation,String applydate)
	{
		this.userID=userID;
		this.photoIndex=photoindex;
		this.sign=sign;
		this.sex=sex;
		this.nickname=nickname;
		this.birthday=birthday;
		this.constellation=constellation;
		this.applydate=applydate;
	}
	

	
	public final String getPasswd()
	{
		return passwd;
	}

	public final String getNickname()
	{
		return nickname;
	}

	public final int getSex()
	{
		return sex;
	}

	public final String getBirthday()
	{
		return birthday;
	}

	public final int getConstellation()
	{
		return constellation;
	}

	public final String getSign()
	{
		return sign;
	}

	public final int getUserID()
	{
		return userID;
	}

	public final int getPhotoIndex()
	{
		return photoIndex;
	}

	public final String getApplydate()
	{
		return applydate;
	}

	@Override
	public String toString()
	{
		return "Bean_userinfo [passwd=" + passwd + ", nickname=" + nickname + ", sex=" + sex + ", birthday=" + birthday
				+ ", constellation=" + constellation + ", sign=" + sign + ", userID=" + userID + ", photoindex="
				+ photoIndex + ", applydate=" + applydate + "]";
	}
	
}
