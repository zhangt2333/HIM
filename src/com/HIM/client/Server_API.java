package com.HIM.client;

import com.HIM.common.*;

import java.util.ArrayList;
import java.util.Collections;
import java.net.*;
import java.io.*;
/*
 * CS通讯类
 * @Author:Zhangt2333
 */
public class Server_API 
{
	private Socket socket = null;
	private String serverIP = null;
	private int serverPort = -1;
//	private String InternetIP = "";
//	private int InternetPort = -1;
	private int receivePort = -1; 
	//输入输出流
	private OutputStream os = null;
	private InputStream is = null;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private FileOutputStream fos = null;
	private FileInputStream fis = null;
	
	public Server_API(String serverIP,int serverPort,Thread_receiver t) 
	{
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		//第三个参数留着写udp通信(暂用tcp+服务端中介代替)
		if (t!=null)
			this.receivePort = t.getInternetPort();
		else
			this.receivePort = -1;
		this.init();
	}
	

	private void init()//使用前必须要初始化
	{
		try
		{
			socket = new Socket(serverIP,serverPort);
			//服务器满员退出时间
			socket.setSoTimeout(3000);
			//服务端和客户端的流建立要逆序，比如服务端建立i,客户端建立o
			os=socket.getOutputStream();
			is=socket.getInputStream();
			dos = new DataOutputStream(os);
			dis = new DataInputStream(is);
			oos = new ObjectOutputStream(os);
			ois = new ObjectInputStream(is);
			//双端验证
			this.Authenticate();
			//读入外网端口
//			this.getInternerPort();
			logger.writelog("Client", "连接服务器成功！");
			socket.setSoTimeout(0);
		}catch (UnknownHostException e)
		{
			logger.writelog("Client", "连接服务器失败！");
		}catch (IOException e)
		{
			logger.writelog("Client", "获取io失败！");
		}
	}
	
	private void Authenticate() throws IOException
	{
		dos.writeUTF("HIM");
	}
	
	public void close()
	{
		try
		{
			if (!socket.isClosed()) dos.writeUTF("end");
			if (os!=null) os.close();
			if (is!=null) is.close();
			if (dos!=null) dos.close();
			if (dis != null) dis.close();
			if (oos!=null) oos.close();
			if (ois!=null) ois.close();
			if (socket!=null) socket.close();
			logger.writelog("Client", "断开连接成功！");
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean sendMessage(Bean_message message)
	{
		boolean ok = true;
		try
		{
			dos.writeUTF("sendMessage");
			oos.writeObject(message);
		} catch (IOException e) 
		{
			e.printStackTrace();
			ok = false;
		}
		return ok;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bean_message> queryUnreadedMessages(int receiver)
	{
		ArrayList<Bean_message> list = null;
		try
		{
			dos.writeUTF("queryUnreadedMessages");
			dos.writeInt(receiver);
			list = (ArrayList<Bean_message>)ois.readObject();
		} catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return list;
	}
	
//	public void getInternetIP()
//	{
//		try
//		{
//			dos.writeUTF("getInternetIP");
//			this.InternetIP = dis.readUTF();
//		}catch (IOException e) 
//		{
//			e.printStackTrace();
//		}
//	}
	
//	public void getInternerPort()
//	{
//		try
//		{
//			dos.writeUTF("getInternerPort");
//			this.InternetPort = dis.readInt();
//		}catch (IOException e) 
//		{
//			e.printStackTrace();
//		}
//	}
	
	public int register(Bean_userinfo bean)
	{
		int userID = -1;
		try 
		{
			dos.writeUTF("register");
			oos.writeObject(bean);
			userID = dis.readInt();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return userID;
	}
	
	
	public boolean login(int userID,String passwd)
	{
		boolean ok=false;
		try
		{
			dos.writeUTF("login");
			dos.writeInt(userID);
			dos.writeUTF(passwd);
			dos.writeInt(this.receivePort);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return ok;
	}
	
	
	public boolean logout(int userID)
	{
		boolean ok=false;
		try
		{
			dos.writeUTF("logout");
			dos.writeInt(userID);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public Bean_userinfo queryOwnInfo(int userID)
	{
		Bean_userinfo bean=null;
		try
		{
			dos.writeUTF("queryOwnInfo");
			dos.writeInt(userID);
			bean = (Bean_userinfo)ois.readObject();
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return bean;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bean_friendinfo> queryUser_nickname(String nickname)
	{
		
		ArrayList<Bean_friendinfo> ans=null;
		try
		{
			dos.writeUTF("queryUser_nickname");
			dos.writeUTF(nickname);
			ans = (ArrayList<Bean_friendinfo>) ois.readObject();
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		return ans;
	}
	
	public Bean_friendinfo queryUser_userID(int userID)
	{
		Bean_friendinfo ans = null;
		try
		{
			dos.writeUTF("queryUser_userID");
			dos.writeInt(userID);
			ans = (Bean_friendinfo) ois.readObject();
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return ans;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bean_friendinfo> queryUsers(ArrayList<Integer> list)
	{
		ArrayList<Bean_friendinfo> ans=null;
		try
		{
			dos.writeUTF("queryUsers");
			oos.writeObject(list);
			ans = (ArrayList<Bean_friendinfo>) ois.readObject();
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return ans;
	}
	
	public boolean updateOwnInfo(Bean_userinfo bean)
	{
		//userID=?,sign=?,sex=?,nickname=?,birthday=?,constellation=?  有以上属性
		
		boolean ok=false;
		try
		{
			dos.writeUTF("updateOwnInfo");
			oos.writeObject(bean);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	
	public boolean updatePasswd(int userID,String oldpasswd,String newpasswd)
	{
		boolean ok=false;
		try
		{
			dos.writeUTF("updatePasswd");
			dos.writeInt(userID);
			dos.writeUTF(oldpasswd);
			dos.writeUTF(newpasswd);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return ok;
	}
	
	
	public boolean updatePhotoindex(int userID,int newphotoindex)
	{
		boolean ok=false;
		try
		{
			dos.writeUTF("updatePhotoindex");
			dos.writeInt(userID);
			dos.writeInt(newphotoindex);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	public int uploadFile(Bean_fileinfo bean)
	{
		int infoindex = -1;
		try
		{
			File file = new File(bean.getPath());
			if (file.exists())
			{
				dos.writeUTF("uploadFile");
				fis = new FileInputStream(file);
				//传文件bean
				oos.writeObject(bean);
				//判断文件在服务端已经存在
				infoindex = dis.readInt();
				if (infoindex == -1)
				{
					logger.writelog("Client", "文件传输开始!");
					byte[] bytes = new byte[1024];
					int length = 0;
					long sumlenth = bean.getLength();
					int percent = 0;
					long progress = 0;
					int temp = 0;
					while( (length = fis.read(bytes,0,bytes.length)) != -1 )
					{
						dos.write(bytes,0,length);
						progress += length;
/*需要被重写的进度显示部分
------------------------------------------------------*/ 
temp = (int)(100*progress/sumlenth);
if (temp > percent)
{
	percent = temp;
	System.out.print(percent+"% ");				
}
//-------------------------------------------------------
					}
					logger.writelog("Client", "\n文件传输完毕!");
					//获取新的文件编号
					infoindex = dis.readInt();
				}
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		} finally 
		{
			try 
			{
				if (fis!=null) fis.close();
			}catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		return infoindex;
	}
	
	public int uploadPhoto(File file)
	{
		int infoindex = -1;
		try
		{
			if (file.exists())
			{
				dos.writeUTF("uploadPhoto");
				dos.writeUTF(tools.getFileMD5(file));
				dos.writeUTF(file.getName());
				dos.writeLong(file.length());
				fis = new FileInputStream(file);
				logger.writelog("Client", "头像传输开始!");
				byte[] bytes = new byte[1024];
				int length = 0;
				while( (length = fis.read(bytes,0,bytes.length)) != -1 )
				{
					dos.write(bytes,0,length);
				}
				infoindex = dis.readInt();
				logger.writelog("Client", "头像传输完毕!");
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		} finally 
		{ 
			try
			{
				if (fis!=null) fis.close();
			}catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return infoindex;
	}
	
	//文件编号，想存在哪，返回一个文件bean，可能为null
	public Bean_fileinfo downloadFile_index(int infoindex, File file)
	{
		Bean_fileinfo bean = null;
		try
		{
			dos.writeUTF("downloadFile_index");
			dos.writeInt(infoindex);
			bean = (Bean_fileinfo) ois.readObject();
			bean = downloadFile(bean, file);
		} catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
			bean = null;
		}
		return bean;
	}
	
	public Bean_fileinfo downloadFile_md5(String md5, File file)
	{
		Bean_fileinfo bean = null;
		try
		{
			dos.writeUTF("downloadFile_md5");
			dos.writeUTF(md5);
			bean = (Bean_fileinfo) ois.readObject();
			bean = downloadFile(bean, file);
		} catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
			bean = null;
		}
		return bean;
	}
	
	private Bean_fileinfo downloadFile(Bean_fileinfo bean,File file)
	{
		try
		{
			if (bean != null)
			{
				fos = new FileOutputStream(file);
				//接受文件
				logger.writelog("Client", "接受文件中：" + file.getName());
				byte[] bytes = new byte[1024];
				int length = 0;
				long sumlenth = bean.getLength();
				int percent = 0;
				long progress = 0;
				int temp = 0;
				while(true)
				{
					length = dis.read(bytes,0,bytes.length);
					fos.write(bytes,0,length);
					progress += length;
					temp = (int)(100*progress/sumlenth);
					if (temp > percent)
					{
						percent = temp;
						logger.writelog("Client", "|" + percent + "%|");						
					}
					if (progress >= sumlenth || length == -1) break;
				}
				bean.setFile(file);
				logger.writelog("Client", "接受文件完毕：" + file.getName());
			}
		} catch (SocketException e) 
		{
			if (e instanceof SocketException)
			{
				logger.writelog("Client", "连接中断(downloadFile)!");
				try
				{
					if(fos != null) fos.close();
				} catch (IOException ex)
				{
					e.printStackTrace();
				}
				file.delete();
				bean = null;
			}
			else
				e.printStackTrace();
		} catch (IOException  e) 
		{
			e.printStackTrace();
		} finally 
		{
			try
			{
				if(fos != null) fos.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return bean;
	}
	
	//头像编号，本地文件库目录，返回bool
	public boolean downloadPhoto(int infoindex,File directory)
	{
		boolean ok = false;
		File photofile = null;
		try
		{
			dos.writeUTF("downloadPhoto");
			dos.writeInt(infoindex);
			String photoname = dis.readUTF();
			if (!photoname.equals(""))
			{
				long sumlenth = dis.readLong();	
				photoname = infoindex + ""; //tools.getFileExtensionName(photoname);
				photofile = new File(directory.getAbsolutePath() + File.separatorChar + photoname);
				fos = new FileOutputStream(photofile);
				//接受文件
				logger.writelog("Client", "接受头像中：" + photoname);
				byte[] bytes = new byte[1024];
				int length = 0;
				while(true)
				{
					length = dis.read(bytes,0,bytes.length);
					fos.write(bytes,0,length);
					sumlenth -= length;
					if (sumlenth <= 0 || length == -1) break;
				}
				ok = true;
				logger.writelog("Client", "接受头像完毕：" + photoname);
			}
		} catch (SocketException e) 
		{
			if (e instanceof SocketException)
			{
				logger.writelog("Client", "连接中断(downloadPhoto)!");
				try
				{
					if(fos != null) fos.close();
				} catch (IOException ex)
				{
					e.printStackTrace();
				}
				photofile.delete();
			}
			else
				e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		} finally 
		{
			try
			{
				if(fos != null) fos.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return ok;
	}
	
	
	public Bean_quninfo queryQunInfo_qunID(int qunID)
	{
		Bean_quninfo bean=null;
		try
		{
			dos.writeUTF("queryQunInfo_qunID");
			dos.writeInt(qunID);
			bean = (Bean_quninfo)ois.readObject();
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return bean;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bean_quninfo> queryQunInfo_qunname(String qunname)
	{
		ArrayList<Bean_quninfo> list = null;
		try
		{
			dos.writeUTF("queryQunInfo_qunname");
			dos.writeUTF(qunname);
			list = (ArrayList<Bean_quninfo>)ois.readObject();			
		} catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bean_friendinfo> queryQunMembers(int qunID)
	{
		ArrayList<Bean_friendinfo> list = null;
		try
		{
			dos.writeUTF("queryQunMembers");
			dos.writeInt(qunID);
			list = (ArrayList<Bean_friendinfo>)ois.readObject();
		} catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bean_quninfo> queryUserQuns(int userID)
	{
		ArrayList<Bean_quninfo> list = null;
		try
		{
			dos.writeUTF("queryUserQuns");
			dos.writeInt(userID);
			list = (ArrayList<Bean_quninfo>)ois.readObject();
		} catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public int createQun(int userID,String qunname)
	{
		int qunID = -1;
		try
		{
			dos.writeUTF("createQun");
			dos.writeInt(userID);
			dos.writeUTF(qunname);
			qunID = dis.readInt();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return qunID;
	}
	
	public boolean addQun(int userID,int qunID)
	{
		boolean ok = false;
		try
		{
			dos.writeUTF("addQun");
			dos.writeInt(userID);
			dos.writeInt(qunID);
			ok = dis.readBoolean();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	public boolean quitQun(int userID,int qunID)
	{
		boolean ok = false;
		try
		{
			dos.writeUTF("quitQun");
			dos.writeInt(userID);
			dos.writeInt(qunID);
			ok = dis.readBoolean();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	public boolean updateQunInfo(Bean_quninfo quninfo)
	{
		boolean ok = false;
		try
		{
			dos.writeUTF("updateQunInfo");
			oos.writeObject(quninfo);
			ok = dis.readBoolean();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	public boolean updateUserQun(int userID,int qunID,String notename)
	{
		boolean ok = false;
		try
		{
			dos.writeUTF("updateUserQun");
			dos.writeInt(userID);
			dos.writeInt(qunID);
			dos.writeUTF(notename);
			ok = dis.readBoolean();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	
	//带有魔法值的方法，待优化
	public void sendMessage_ip(Bean_message message)
	{
		try
		{
			dos.writeUTF("sendMessage_ip");
			oos.writeObject(message);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Bean_message> queryChatRecords(int userID,int friendID)
	{
		ArrayList<Bean_message> ans = null;
		try
		{
			dos.writeUTF("queryChatRecords");
			dos.writeInt(userID);
			dos.writeInt(friendID);
			ans = (ArrayList<Bean_message>) ois.readObject();
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		//排序
		if (ans != null) Collections.sort(ans);
		return ans;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bean_message> queryQunChatRecords(int userID,int qunID)
	{
		ArrayList<Bean_message> ans = null;
		try
		{
			dos.writeUTF("queryQunChatRecords");
			dos.writeInt(userID);
			dos.writeInt(qunID);
			ans = (ArrayList<Bean_message>) ois.readObject();
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		//排序
		if (ans != null) Collections.sort(ans);
		return ans;
	}
	
	public boolean postMood(Bean_mood mood)
	{
		boolean ok = false;
		try
		{
			dos.writeUTF("postMood");
			oos.writeObject(mood);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bean_mood> queryMoods(ArrayList<Integer> posters)
	{
		ArrayList<Bean_mood> moods = null;
		try
		{
			dos.writeUTF("queryMoods");
			oos.writeObject(posters);
			moods = (ArrayList<Bean_mood>) ois.readObject();
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		//排序
		if (moods != null) Collections.sort(moods);
		return moods;
	}
		
	public boolean addQunfile(int uploader,int qunID,int fileindex)
	{
		boolean ok = false;
		try
		{
			dos.writeUTF("addQunfile");
			dos.writeInt(uploader);
			dos.writeInt(qunID);
			dos.writeInt(fileindex);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok; 
	}
	
	public boolean deleteQunfile(int qunID,int fileindex)
	{
		boolean ok = false;
		try
		{
			dos.writeUTF("deleteQunfile");
			dos.writeInt(qunID);
			dos.writeInt(fileindex);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bean_fileinfo> queryQunfiles(int qunID)
	{
		ArrayList<Bean_fileinfo> files = null;
		try
		{
			dos.writeUTF("queryQunfiles");
			dos.writeInt(qunID);
			files = (ArrayList<Bean_fileinfo>) ois.readObject();
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return files;
	}
	
	
	public int addSubGroup(int userID, String subgroupname)
	{
		int groupindex = -1;
		try
		{
			dos.writeUTF("addSubGroup");
			dos.writeInt(userID);
			dos.writeUTF(subgroupname);
			groupindex = dis.readInt();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return groupindex;
	}

	
	//这个方法会把该组的好友都删
	public boolean deleteSubGroup(int groupindex)
	{
		boolean ok = false;
		try
		{
			dos.writeUTF("deleteSubGroup");
			dos.writeInt(groupindex);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bean_subgroup> querySubGroup(int userID)
	{
		ArrayList<Bean_subgroup> ans = null;
		try
		{
			dos.writeUTF("querySubGroup");
			dos.writeInt(userID);
			ans = (ArrayList<Bean_subgroup>) ois.readObject();
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return ans;
	}
	
	public boolean addFriend(int userID,int friendID,int groupIndex,String alias)
	{
		boolean ok = false;
		try
		{
			dos.writeUTF("addFriend");
			dos.writeInt(userID);
			dos.writeInt(friendID);
			dos.writeInt(groupIndex);
			dos.writeUTF(alias);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	
	public boolean deleteFriend(int userID,int friendID)
	{
		boolean ok = false;
		try
		{
			dos.writeUTF("deleteFriend");
			dos.writeInt(userID);
			dos.writeInt(friendID);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	public boolean moveFriend(int userID,int friendID,int groupIndex)
	{
		boolean ok = false;
		try
		{
			dos.writeUTF("moveFriend");
			dos.writeInt(userID);
			dos.writeInt(friendID);
			dos.writeInt(groupIndex);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	public boolean updateFriendAlias(int userID,int friendID,String alias)
	{
		boolean ok = false;
		try
		{
			dos.writeUTF("updateFriendAlias");
			dos.writeInt(userID);
			dos.writeInt(friendID);
			dos.writeUTF(alias);
			ok = dis.readBoolean();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bean_friendinfo> queryUserFriends(int userID)
	{
		ArrayList<Bean_friendinfo> ans=null;
		try
		{
			dos.writeUTF("queryUserFriends");
			dos.writeInt(userID);
			ans = (ArrayList<Bean_friendinfo>) ois.readObject();
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return ans;
	}
	
}
