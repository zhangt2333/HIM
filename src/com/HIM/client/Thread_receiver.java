package com.HIM.client;

import java.io.*;
import java.net.*;

import com.HIM.common.Bean_message;
import com.HIM.common.logger;

public class Thread_receiver implements Runnable {
	private Socket socket = null;
	//输入输出流
	private InputStream is = null;
	private OutputStream os = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	//private FileOutputStream fos = null;
	//private FileInputStream fis = null;
	//工作与否
	private boolean working = true;
	private String serverIP;
	private int serverPort;
	private int InternetPort;


	public Thread_receiver(String serverIP, int serverPort) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.Init();
	}

	private void Init()//使用前必须要初始化
	{
		try {
			socket = new Socket(serverIP, serverPort);
			//服务器满员退出时间
			socket.setSoTimeout(3000);
			//服务端和客户端的流建立要逆序，比如服务端建立i,客户端建立o
			os = socket.getOutputStream();
			is = socket.getInputStream();
			dos = new DataOutputStream(os);
			dis = new DataInputStream(is);
			oos = new ObjectOutputStream(os);
			ois = new ObjectInputStream(is);
			//双端验证
			this.Authenticate();
			//读入外网端口
			dos.writeUTF("getInternerPort");
			this.InternetPort = dis.readInt();
			logger.writelog("ClientReceiver", "连接服务器成功！");
			socket.setSoTimeout(0);
		} catch (UnknownHostException e) {
			logger.writelog("ClientReceiver", "连接服务器失败！");
		} catch (IOException e) {
			logger.writelog("ClientReceiver", "获取io失败！");
		}
	}

	private void Authenticate() throws IOException {
		dos.writeUTF("HIM");
	}

	public final int getInternetPort() {
		return InternetPort;
	}

	public void close() {
		try {
			if (!socket.isClosed()) dos.writeUTF("end");
			if (os != null) os.close();
			if (is != null) is.close();
			if (dos != null) dos.close();
			if (dis != null) dis.close();
			if (oos != null) oos.close();
			if (ois != null) ois.close();
			if (socket != null) socket.close();
			logger.writelog("ClientReceiver", "断开连接成功！");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void run() {
		Object message = null;
		try {
			while (working) {
				System.out.println("接受信息中...");
				message = ois.readObject();
				if (message != null && message instanceof Bean_message) {
					handleMessage((Bean_message) message);
				}
			}
		} catch (IOException e) {
			logger.writelog("ClientReceiver", e.getMessage());
		} catch (Exception e) {
			logger.writelog("ClientReceiver", e.getMessage());
		} finally {
			close();
		}
	}

	public Socket getSocket() {
		return this.socket;
	}


	public void handleMessage(Bean_message message) 
	{
//		MessageHandler.handleMessage(message);
	}
}


	
	
	

