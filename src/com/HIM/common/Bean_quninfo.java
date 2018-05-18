package com.HIM.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Bean_quninfo implements Serializable
{
	private int qunID;
	private String qunName;
	private int photoIndex;
	private String createTime;
	
	//查询专用构造方法
	public Bean_quninfo(int qunID,String qunname,int photoindex,String createtime) 
	{
		this.qunID = qunID;
		this.qunName = qunname;
		this.photoIndex = photoindex;
		this.createTime = createtime;
	}
	
	//更新专用构造方法
	public Bean_quninfo(int qunnum,String qunname,int photoindex) 
	{
		this.qunID = qunnum;
		this.qunName = qunname;
		this.photoIndex = photoindex;
	}

	public final int getQunID()
	{
		return qunID;
	}

	public final String getQunName()
	{
		return qunName;
	}

	public final int getPhotoIndex()
	{
		return photoIndex;
	}

	public final String getCreateTime()
	{
		return createTime;
	}

	public final void setQunID(int qunID)
	{
		this.qunID = qunID;
	}

	public final void setQunName(String qunName)
	{
		this.qunName = qunName;
	}

	public final void setPhotoIndex(int photoIndex)
	{
		this.photoIndex = photoIndex;
	}

	public final void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	@Override
	public String toString()
	{
		return "Bean_quninfo [qunID=" + qunID + ", qunName=" + qunName + ", photoIndex=" + photoIndex + ", createTime="
				+ createTime + "]";
	}
	
	
 	
}
