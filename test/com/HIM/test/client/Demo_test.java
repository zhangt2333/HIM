package com.HIM.test.client;

import com.HIM.client.*;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.HIM.common.Bean_message;
import com.HIM.common.tools;

public class Demo_test
{

	private JFrame frame;
	public Server_API robot;
	public Thread_receiver receiver;
	private JTextField textField_qq;
	private JTextField textField_passwd;
	private JTextField textField_sendTo;
	private JTextField textField_qunnum;
	private int qq;

	public static void main(String[] args)
	{
		Demo_test Tester = new Demo_test();
		Tester.LaunchFrame();
	}
	
	public void LaunchFrame()
	{
		this.frame = new JFrame();
		this.frame.setLocationRelativeTo(null);
		GridLayout layout = new GridLayout(3, 3);
		JButton b1 = new JButton("Init");
		JButton b2 = new JButton("Close");
		JButton b3 = new JButton("Login");
		JButton b4 = new JButton("Logout");
		JButton b5 = new JButton("sendMessage");
		JButton b6 = new JButton("sendQunMessage");		
		
		b1.addActionListener( e -> Init() );
		b2.addActionListener( e -> close() );
		b3.addActionListener( e -> login() );
		b4.addActionListener( e -> logout() );
		b5.addActionListener( e -> sendMessage() );
		b6.addActionListener( e -> sendQunMessage() );

		this.textField_qq = new JTextField("请输入账号");
		this.textField_passwd = new JTextField("请输入密码");
		
		
		this.frame.setLayout(layout);
		this.frame.add(this.textField_qq);
		this.frame.add(this.textField_passwd);
		this.frame.add(b1);
		this.frame.add(b2);
		this.frame.add(b3);
		this.frame.add(b4);
		this.frame.add(b5);
		this.frame.add(b6);
		this.frame.pack();
		this.frame.setVisible(true);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	}
	
	
	public void Init()
	{
		this.receiver = new Thread_receiver("127.0.0.1", 6666);
		new Thread(this.receiver).start();
		this.robot = new Server_API("127.0.0.1", 6666,this.receiver);

		
//		this.receiver = new Thread_receiver("45.32.23.9", 6666);
//		new Thread(this.receiver).start();
//		this.robot = new Server_API("45.32.23.9", 6666,this.receiver);	

	}
	
	public void close()
	{
		this.robot.close();
		this.receiver.close();
	}
	
	public void login()
	{
		this.robot.login(Integer.parseInt(this.textField_qq.getText()), "12345");
	}
	
	public void logout()
	{
		this.robot.logout(Integer.parseInt(this.textField_qq.getText()));
	}
	
	public void sendMessage()
	{
		Bean_message message;
		message = new Bean_message(Integer.parseInt(this.textField_qq.getText()), this.qq==10000?10001:10000, Bean_message.TYPE_SendMessage
								   , tools.get_nowtime(true), "测试发送个人");
		this.robot.sendMessage(message);
	}
	
	public void sendQunMessage()
	{
		Bean_message message;
		message = new Bean_message(Integer.parseInt(this.textField_qq.getText()), 10000, Bean_message.TYPE_Send2Qun
								   , tools.get_nowtime(true), "测试发送群");
		this.robot.sendMessage(message);
	}
	
}
