package com.HIM.server;

import com.HIM.common.*;


import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import java.io.*;

/*
 * 服务线程类
 * 读取客户端请求功能并执行功能：
 * 1.......
 * @Author:Zhangt2333
 */

public class Thread_Server implements Runnable
{
	private Socket socket = null;
	//输入输出流
	private InputStream is = null;
	private OutputStream os = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private FileOutputStream fos = null;
	private FileInputStream fis = null;
	//工作与否
	private boolean working = true;
	//记录登陆者
	private int userID = -1;
	private String ip = null;
	private int linkPort = -1;
	private int receivePort = -1;
	private String linkinfo;
	
	//peer2peers专用
	private String YouDrawMeGuessPlayerOwner;
	private int YouDrawMeGuessPlayerUserID = -1;
	Set<Thread_Server> peers;
	
	Thread_Server(Socket socket)
	{
		this.socket = socket;
		this.ip = socket.getInetAddress().getHostAddress();
		this.linkPort = socket.getPort();
		this.linkinfo = userID + "("+ ip + ":" + linkPort + ")";
	}
	
	public Socket getSocket() {return this.socket;}
	public String getIP() {return this.ip;}
	public int getPort() {return this.linkPort;}
		
	private void InitStream() throws IOException
	{
		//输入输出流初始化
		if (socket == null) throw new IOException("socket not exist!");
		//服务端和客户端的流建立要逆序，比如服务端建立i,客户端建立o
		is = socket.getInputStream();
		os = socket.getOutputStream();
		dis = new DataInputStream(is);
		dos = new DataOutputStream(os);
		ois = new ObjectInputStream(is);
		oos = new ObjectOutputStream(os);
	}
	
	private void Authenticate() throws IOException
	{
		socket.setSoTimeout(1000);
		String captcha = dis.readUTF();
		if ( !captcha.equals("HIM") )
			throw new IOException("Authenticate Fail！");
		socket.setSoTimeout(0);
	}
	
	public void run()
	{
		try 
		{	
			//流初始化
			InitStream();
			//通信验证
			Authenticate();
			logger.writelog("Server", "线程:" + Thread.currentThread().getName());
			while(working)
			{
				String command = dis.readUTF();
				logger.writelog("server",command + " <- " + linkinfo);
				switch (command) 
				{
					case "end":working=false;break;
					case "getInternetIP":getInternetIP();break;
					case "getInternerPort":getInternerPort();break;
					case "register":register();break;
					case "login": login();break;
					case "logout": logout();break;
					case "queryOwnInfo": queryOwnInfo();break;
					case "queryUser_nickname": queryUser_nickname();break;
					case "queryUser_userID": queryUser_userID();break;
					case "queryUsers":queryUsers();break;
					case "updateOwnInfo": updateOwnInfo();break;
					case "updatePasswd":updatePasswd();break;
					case "updatePhotoindex":updatePhotoindex();break;
					case "uploadFile":uploadFile();break;
					case "downloadFile_index":downloadFile_index();break;
					case "downloadFile_md5":downloadFile_md5();break;
					case "uploadPhoto":uploadPhoto();break;
					case "downloadPhoto":downloadPhoto();break;
					case "sendMessage":sendMessage();break;
					case "queryUnreadedMessages":queryUnreadedMessages();break;
					case "queryQunInfo_qunID":queryQunInfo_qunID();break;
					case "queryQunInfo_qunname":queryQunInfo_qunname();break;
					case "queryQunMembers":queryQunMembers();break;
					case "queryUserQuns":queryUserQuns();break;
					case "createQun":createQun();break;
					case "addQun":addQun();break;
					case "quitQun":quitQun();break;
					case "updateQunInfo":updateQunInfo();break;
					case "updateUserQun":updateUserQun();break;
					case "queryChatRecords":queryChatRecords();break;
					case "queryQunChatRecords":queryQunChatRecords();break;
					case "peer2peer_FileTransfer":peer2peer_FileTransfer();break;
					case "peer2peers_YouDrawMeGuess":peer2peers_YouDrawMeGuess();break;
					case "postMood":postMood();break;
					case "queryMoods":queryMoods();break;
					case "addQunfile":addQunfile();break;
					case "deleteQunfile":deleteQunfile();break;
					case "queryQunfiles":queryQunfiles();break;
					case "addSubGroup":addSubGroup();break;
					case "deleteSubGroup":deleteSubGroup();break;
					case "querySubGroup":querySubGroup();break;
					case "addFriend":addFriend();break;
					case "deleteFriend":deleteFriend();break;
					case "moveFriend":moveFriend();break;	
					case "updateFriendAlias":updateFriendAlias();break;
					case "queryUserFriends":queryUserFriends();break;
				}
			}
		} catch (SocketException | EOFException e)
		{
			logger.writelog("Server", linkinfo + "连接中断!");
		} catch (IOException e) 
		{
			logger.writelog("Server", linkinfo + e.getMessage());
		} catch (Exception e) 
		{
			logger.writelog("Server", linkinfo + e.getMessage());
		} finally
		{
			this.close();
		}
	}
	
	private void close()
	{
		working = false;
		if (userID != -1)
		{
			Db_Operate.logout(userID);
			OnlineUserManager.removeUser(userID);
		}
		ServerThreadsManager.removeServerThread(this);
		try
		{
			if (YouDrawMeGuessPlayerOwner != null)
			{
				Thread_Server t = ServerThreadsManager.getServerThread(YouDrawMeGuessPlayerOwner);
				t.dos.writeUTF("removePlayer");
				t.dos.writeInt(YouDrawMeGuessPlayerUserID);
			}
			if (is!=null) is.close();
			if (os!=null) os.close();
			if (dis!=null) dis.close();
			if (dos!=null) dos.close();
			if (ois!=null) ois.close();
			if (oos!=null) oos.close();
			if (socket!=null) socket.close();
		}
		catch (IOException ex) 
		{
			logger.writelog("Server", linkinfo + ex.getMessage());
		}
	}
	
	public void getInternetIP()
	{
		try
		{
			dos.writeUTF(this.ip);
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void getInternerPort()
	{
		try
		{
			dos.writeInt(this.linkPort);
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void register()
	{
		try 
		{
			Bean_userinfo b = (Bean_userinfo)ois.readObject();
			int userID = Db_Operate.register(b);
			dos.writeInt(userID);
		} catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void login()
	{
		try
		{
			int userID = dis.readInt();
			String passwd = dis.readUTF();
			this.receivePort = dis.readInt();
			boolean ok = Db_Operate.login(userID, passwd, ip,this.receivePort);
			dos.writeBoolean(ok);
			if (ok)
			{
				OnlineUserManager.addUser(userID,ip+":"+this.receivePort);
				this.userID = userID;
				this.linkinfo = userID + "("+ ip + ":" + linkPort + ")";
			}
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void logout()
	{
		try
		{
			int userID = dis.readInt();
			boolean ok = Db_Operate.logout(userID);
			dos.writeBoolean(ok);
			OnlineUserManager.removeUser(userID);
			this.userID = -1;
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	

	
	public void queryOwnInfo()
	{
		try
		{
			oos.writeObject(Db_Operate.queryOwnInfo(dis.readInt()));
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void queryUser_nickname()
	{
		ArrayList<Bean_friendinfo> ans = null;
		try
		{
			String nickname = dis.readUTF();
			ans = Db_Operate.queryUser_nickname(nickname);
			oos.writeObject(ans);	
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void queryUser_userID()
	{
		Bean_friendinfo ans = null;
		try
		{
			int userID = dis.readInt();
			ans = Db_Operate.queryUser_userID(userID);
			oos.writeObject(ans);	
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void queryUsers()
	{
		ArrayList<Bean_friendinfo> ans = null;
		try
		{
			ArrayList<Integer> list = (ArrayList<Integer>)ois.readObject();
			ans = Db_Operate.queryUsers(list);
			oos.writeObject(ans);	
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	
	public void updateOwnInfo()
	{
		try
		{
			boolean ok = Db_Operate.updateOwnInfo((Bean_userinfo)ois.readObject());
			dos.writeBoolean(ok);
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void updatePasswd()
	{
		try
		{
			boolean ok = Db_Operate.updatePasswd(dis.readInt(), dis.readUTF()
												 , dis.readUTF());
			dos.writeBoolean(ok);
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void updatePhotoindex()
	{
		try
		{
			boolean ok = Db_Operate.updatePhotoindex(dis.readInt(), dis.readInt());
			dos.writeBoolean(ok);
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void uploadFile()
	{
		File newfile = null;
		int infoindex = -1;
		try 
		{
			Bean_fileinfo bean = (Bean_fileinfo) ois.readObject();
			//搜索md5判是否存在，存在则返回
			infoindex = Db_Operate.queryFile_md5(bean.getMD5()); 
			dos.writeInt(infoindex);
			if (infoindex == -1)
			{				
				File directory = config.getDbFileDirectory();
				newfile = new File(directory.getAbsolutePath() 
						+ File.separatorChar + bean.getMD5());
				fos = new FileOutputStream(newfile);
				//接受文件
				logger.writelog("server", "接受文件中：" + bean.getName());
				byte[] bytes = new byte[1024];
				int length = 0;
				long sumlenth = bean.getLength();
				while(true)
				{
					length = dis.read(bytes,0,bytes.length);
					fos.write(bytes,0,length);
					sumlenth -= length;
					if (sumlenth <= 0 || length == -1) break;
				}
				bean.setFile(newfile);
				Db_Operate.addFileInfo(bean);
				infoindex = Db_Operate.queryFile_md5(bean.getMD5());
				dos.writeInt(infoindex);
				logger.writelog("server", "文件接受完毕：" + bean.getName());
			}
		} catch (IOException | ClassNotFoundException e) 
		{
			if (e instanceof SocketException)
			{
				logger.writelog("Server", "连接中断(uploadFile)!");
				try
				{
					if(fos != null) fos.close();
				} catch (IOException ex)
				{
					e.printStackTrace();
				}
				newfile.delete();
			}
			else
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
	}
	
	public void uploadPhoto()
	{
		File newphoto = null;
		try 
		{
			String MD5 = dis.readUTF();
			String filename = dis.readUTF();
			long sumlenth = dis.readLong();
			newphoto = new File(config.getDbPhotoDirectory().getAbsolutePath() 
					+ File.separatorChar + MD5 + tools.getFileExtensionName(filename));
			//接受文件
			fos = new FileOutputStream(newphoto);
			logger.writelog("server", "接受头像中：" + newphoto.getName());
			byte[] bytes = new byte[1024];
			int length = 0;
			while(true)
			{
				length = dis.read(bytes,0,bytes.length);
				fos.write(bytes,0,length);
				sumlenth -= length;
				if (sumlenth <= 0 || length == -1) break;
			}
			dos.writeInt(Db_Operate.addPhotoInfo(MD5, newphoto.getAbsolutePath()));
			logger.writelog("server", "头像接受完毕：" + newphoto.getName());
			
		} catch (IOException e) 
		{
			if (e instanceof SocketException)
			{
				logger.writelog("Server", "连接中断(uploadFile)!");
				try
				{
					if(fos != null) fos.close();
				} catch (IOException ex)
				{
					e.printStackTrace();
				}
				newphoto.delete();
			}
			else
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
	}
	
	public void downloadFile_index()
	{
		try
		{
			int infoindex = dis.readInt();
			Bean_fileinfo bean = Db_Operate.queryFile_index(infoindex);
			oos.writeObject(bean);
			if (bean != null)
			{
				downloadFile(bean);
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void downloadFile_md5()
	{
		try
		{
			String md5 = dis.readUTF();
			int index = Db_Operate.queryFile_md5(md5);
			Bean_fileinfo bean = null;
			if (index != -1) bean = Db_Operate.queryFile_index(index);
			oos.writeObject(bean);
			if (bean != null)
			{
				downloadFile(bean);
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void downloadFile(Bean_fileinfo bean)
	{
		try
		{
			if (bean != null)
			{
				fis = new FileInputStream(new File(bean.getPath()));
				logger.writelog("Server", "文件传输开始!");
				byte[] bytes = new byte[1024];
				int length = 0;
				while( (length = fis.read(bytes,0,bytes.length)) != -1 )
				{
					dos.write(bytes,0,length);
				}
				logger.writelog("Server", "文件传输完毕!");
			}
		} catch (SocketException e) 
		{
			logger.writelog("server", "download cancel");
		}catch (IOException e) 
		{
			e.printStackTrace();
		} finally 
		{
			try 
			{
				if (fis!=null) fis.close();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}

	}
	
	public void downloadPhoto()
	{
		try
		{
			int infoindex = dis.readInt();
			String photopath = Db_Operate.queryPhoto_index(infoindex);
			dos.writeUTF(photopath);
			if (!photopath.equals(""))
			{
				File photofile = new File(photopath);
				dos.writeLong(photofile.length());
				fis = new FileInputStream(photofile);
				logger.writelog("Server", "头像传输开始!");
				byte[] bytes = new byte[1024];
				int length = 0;
				while( (length = fis.read(bytes,0,bytes.length)) != -1 )
				{
					dos.write(bytes,0,length);
				}
				logger.writelog("Server", "头像传输完毕!");
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		} finally 
		{
			try 
			{
				if (fis!=null) fis.close();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	
	}

	public void sendMessage()
	{
		try
		{
			boolean ok = false;
			Bean_message message = (Bean_message)ois.readObject();
			//群消息分支
			if (message.getType().equals(Bean_message.TYPE_Send2Qun)
			  ||message.getType().equals(Bean_message.TYPE_Send2Qun_Picture)
			  ||message.getType().equals(Bean_message.TYPE_Send2Qun_Video))
			{
				QunMessageManager.GetManagerInstance().relayMessage(message);
			}
			else 
			{
				//个人消息分支
				int userID = message.getReceiver();
				if (OnlineUserManager.isContainsUser(userID))
				{
					Thread_Server t = ServerThreadsManager.getServerThread(
										OnlineUserManager.getUser(userID));
					ok = t.ralayMessage(message);
				}
				Db_Operate.addChatRecord(message,ok);
			}
		} catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	
	public void queryUnreadedMessages()
	{
		try
		{
			int receiver = dis.readInt();
			ArrayList<Bean_message> list = Db_Operate.queryUnreadedMessages(receiver);
			oos.writeObject(list);
			Db_Operate.updateChatRecordReaded(receiver, null);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	//消息转发到客户端
	public boolean ralayMessage(Bean_message message)
	{
		//仅传输一个数据就好，多数据需要考虑上锁
		boolean ok = false;
		try
		{
			oos.writeObject(message);
			ok = true;
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return ok;
	}

	
	public void queryQunInfo_qunID()
	{
		try
		{
			int qunID = dis.readInt();
			oos.writeObject(Db_Operate.queryQunInfo_qunID(qunID));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void queryQunInfo_qunname()
	{
		try
		{
			String qunname = dis.readUTF();
			oos.writeObject(Db_Operate.queryQunInfo_qunname(qunname));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void queryQunMembers()
	{
		try 
		{
			int qunID = dis.readInt();
			oos.writeObject(Db_Operate.queryQunMembers(qunID));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void queryUserQuns()
	{
		try
		{
			int userID = dis.readInt();
			oos.writeObject(Db_Operate.queryUserQuns(userID));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}		
	}
	
	public void createQun()
	{
		try
		{
			int userID = dis.readInt();
			String qunname = dis.readUTF();
			int qunID = Db_Operate.createQun(userID, qunname);
			dos.writeInt(qunID);
			QunMessageManager.GetManagerInstance().createNewQun(qunID);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void addQun()
	{
		try
		{
			int userID = dis.readInt();
			int qunID = dis.readInt();
			dos.writeBoolean(Db_Operate.addQun(userID, qunID));
			QunMessageManager.GetManagerInstance().addMember(userID, qunID);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void quitQun()
	{
		try
		{
			int userID = dis.readInt();
			int qunID = dis.readInt();
			dos.writeBoolean(Db_Operate.quitQun(userID, qunID));
			QunMessageManager.GetManagerInstance().quitQun(userID, qunID);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void updateQunInfo()
	{
		try
		{
			Bean_quninfo quninfo = (Bean_quninfo)ois.readObject();
			dos.writeBoolean(Db_Operate.updateQunInfo(quninfo));
		} catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public void updateUserQun()
	{
		try
		{
			int userID = dis.readInt();
			int qunID = dis.readInt();
			String notename = dis.readUTF();
			dos.writeBoolean(Db_Operate.updateUserQun(userID, qunID, notename));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void queryChatRecords()
	{
		try
		{
			int userID = dis.readInt();
			int friendID = dis.readInt();
			oos.writeObject(Db_Operate.queryChatRecords(userID, friendID));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void queryQunChatRecords()
	{
		try
		{
			int userID= dis.readInt();
			int qunID = dis.readInt();
			oos.writeObject(Db_Operate.queryQunChatRecords(userID,qunID));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//方法带有魔法值，临时实现功能用
public void peer2peer_FileTransfer()
{
	Thread_Server t = null;
	String ipinfo = null;
	String commandInfo = null;
	try
	{
		commandInfo = dis.readUTF();
		ipinfo = dis.readUTF();
		if (commandInfo.equals("receiveFile"))
		{
			t = ServerThreadsManager.getServerThread(ipinfo);
			t.dos.writeBoolean(true);
			t.dos.writeUTF(this.ip+":"+this.linkPort);
			//然后再客户端写接收文件
		}else if (commandInfo.equals("refuseFile"))
		{
			ServerThreadsManager.getServerThread(ipinfo).dos.writeBoolean(false);
		}else if (commandInfo.equals("sendFile"))
		{
			t = ServerThreadsManager.getServerThread(ipinfo);
			this.dis.readUTF(); //吃掉客户端的dos.writeUTF("uploadFile");
			Bean_fileinfo fileinfo = (Bean_fileinfo)this.ois.readObject();
			t.oos.writeObject(fileinfo);
			byte[] bytes = new byte[1024];
			long sumlenth = fileinfo.getLength();
			int length = 0;
			while(true)
			{
				length = dis.read(bytes,0,bytes.length);
				t.dos.write(bytes,0,length);
				sumlenth -= length;
				if (sumlenth <= 0 || length == -1) break;
			}
		}
	}catch (IOException | ClassNotFoundException | ArrayIndexOutOfBoundsException e) 
	{
		logger.writelog("server-p2p", ipinfo + "-To-" + ip + ":" + linkPort + "  Over");
	}catch (Exception e) 
	{
		e.printStackTrace();
		logger.writelog("server-p2p", e.getMessage());
	}finally
	{
		//由发送端掌控断开
		if (!commandInfo.equals("receiveFile"))
		{
			if (t != null) t.close();
			this.close();
		}
	}
}
	
	//方法带有魔法值，临时实现功能用
	public void peer2peers_YouDrawMeGuess()
	{
		Thread_Server t = null;
		String commandInfo = null;
		try
		{
			commandInfo = dis.readUTF();
			if (commandInfo.equals("sendInvitation"))
			{
				//新建其接收者数组
				peers = new HashSet<>();
				//接收、转发邀请
				Bean_message message = (Bean_message) ois.readObject();
				if (message.getType().equals(Bean_message.TYPE_YouDrawMeGuess))
				{
					t = ServerThreadsManager.getServerThread(OnlineUserManager.getUser(message.getReceiver()));
					if (t != null) t.ralayMessage(message);
				}else if (message.getType().equals(Bean_message.TYPE_Send2Qun_YouDrawMeGuess))
				{
					QunMessageManager.GetManagerInstance().relayMessage(message);
				}
				//进入不断接收、转发图像信息循环
				Object object;
				while (true)
				{
					object = ois.readObject();
					for(Thread_Server tt : peers)
					{
						if (tt.socket.isClosed())
						{
							this.dos.writeUTF("removePlayer");
							this.dos.writeInt(tt.YouDrawMeGuessPlayerUserID);
							ServerThreadsManager.removeServerThread(tt);
							this.peers.remove(tt);
						}
						else 
						{
							tt.oos.writeObject(object);
						}
					}
				}
			}else if (commandInfo.equals("acceptInvitation")) 
			{
				//接收发送者的信息，加入其接收者队列
				Bean_message message = (Bean_message) ois.readObject();
				if (message.getType().equals(Bean_message.TYPE_YouDrawMeGuess)||
					message.getType().equals(Bean_message.TYPE_Send2Qun_YouDrawMeGuess))
				{
					t = ServerThreadsManager.getServerThread(message.getContent());
					this.YouDrawMeGuessPlayerOwner = message.getContent();
					if (t != null) 
					{
						t.peers.add(this);
						t.dos.writeUTF("addPlayer");
						//是YouDrawMeGuessPlayeruserID而不是userID,因为userID是维护登录状态的，仅允许Login,logout调用
						this.YouDrawMeGuessPlayerUserID = message.getReceiver();
						t.dos.writeInt(this.YouDrawMeGuessPlayerUserID);
					}
					//登记完后客户端方面就可以不断接收Object了
				}
			}
		}catch (IOException | ClassNotFoundException | ArrayIndexOutOfBoundsException e) 
		{
			logger.writelog("server-p2ps", e.getMessage());
		}catch (Exception e) 
		{
			e.printStackTrace();
			logger.writelog("server-p2ps", e.getMessage());
		}finally
		{
			if (commandInfo.equals("sendInvitation"))
			{
				for(Thread_Server tt : peers)
				{
					if (tt != null) tt.close();
				}
				peers = null;
				this.close();
			}
		}	
	}
	
	public void postMood()
	{
		try
		{
			Bean_mood mood = (Bean_mood)ois.readObject();
			dos.writeBoolean(Db_Operate.postMood(mood));
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void queryMoods()
	{
		try
		{
			ArrayList<Integer> posters = (ArrayList<Integer>) ois.readObject();
			oos.writeObject(Db_Operate.queryMoods(posters));
		}catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public void addQunfile()
	{
		try
		{
			int uploader = dis.readInt();
			int qunID = dis.readInt();
			int fileindex = dis.readInt();
			dos.writeBoolean(Db_Operate.addQunfile(uploader, qunID, fileindex));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void deleteQunfile()
	{
		try
		{
			int qunID = dis.readInt();
			int fileindex = dis.readInt();
			dos.writeBoolean(Db_Operate.deleteQunfile(qunID, fileindex));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void queryQunfiles()
	{
		try
		{
			int qunID = dis.readInt();
			oos.writeObject(Db_Operate.queryQunfiles(qunID));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void addSubGroup()
	{
		try
		{
			int userID = dis.readInt();
			String subGroupName = dis.readUTF();
			dos.writeInt(Db_Operate.addSubGroup(userID, subGroupName));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void deleteSubGroup()
	{
		try
		{
			int groupindex = dis.readInt();
			dos.writeBoolean(Db_Operate.deleteSubGroup(groupindex));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void querySubGroup()
	{
		try
		{
			int userID = dis.readInt();
			oos.writeObject(Db_Operate.querySubGroup(userID));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void addFriend()
	{
		try
		{
			int userID = dis.readInt();
			int friendID = dis.readInt();
			int groupIndex = dis.readInt();
			String alias = dis.readUTF();
			if (alias == null) alias = "";
			dos.writeBoolean(Db_Operate.addFriend(userID, friendID, groupIndex, alias));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void deleteFriend()
	{
		try
		{
			int userID = dis.readInt();
			int friendID = dis.readInt();
			dos.writeBoolean(Db_Operate.deleteFriend(userID, friendID));
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void moveFriend()
	{
		try
		{
			int userID = dis.readInt();
			int friendID = dis.readInt();
			int groupIndex = dis.readInt();
			dos.writeBoolean(Db_Operate.moveFriend(userID, friendID, groupIndex));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void updateFriendAlias()
	{
		try
		{
			int userID = dis.readInt();
			int friendID = dis.readInt();
			String alias = dis.readUTF();
			dos.writeBoolean(Db_Operate.updateFriendAlias(userID, friendID, alias));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public void queryUserFriends()
	{
		try
		{
			int userID = dis.readInt();
			oos.writeObject(Db_Operate.queryUserFriends(userID));
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	
	
}
