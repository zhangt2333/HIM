package com.HIM.test.client;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.HIM.client.Server_API;
import com.HIM.client.Thread_receiver;
import com.HIM.client.YouDrawMeGuessComm;
import com.HIM.common.Bean_picture;

public class YouDrawMeGuessComm_test
{
	static Thread_receiver thread_receiver;
	
	public static void main(String[] args)
	{
		com.HIM.server.Main.main(new String[]{"test"});
		
		YouDrawMeGuessComm_test.thread_receiver = new Thread_receiver(TesterConfig.server_ip,TesterConfig.server_port);
		
		new Thread(thread_receiver).start();
		Server_API robot1 = new Server_API(TesterConfig.server_ip,TesterConfig.server_port,YouDrawMeGuessComm_test.thread_receiver);
		
		Thread_receiver t2 = new Thread_receiver(TesterConfig.server_ip,TesterConfig.server_port);
		new Thread(t2).start();
		Server_API robot2 = new Server_API(TesterConfig.server_ip,TesterConfig.server_port,t2);
		
		robot1.login(10000, "12345");
		robot2.login(10001, "12345");
		
		YouDrawMeGuessComm comm = new YouDrawMeGuessComm(TesterConfig.server_ip,TesterConfig.server_port);
		
//		comm.InvitePlayer(10001, 10000);
		comm.InviteQun(10000, 1);

		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		JButton button1 = new JButton();
		button1.addActionListener(e->offerTest(comm));
		frame.add(button1);
		JButton button2 = new JButton();
		button2.addActionListener(e->quit());
		frame.add(button2);
		frame.pack();
		frame.setVisible(true);
		
		
	}
	
	public static void offerTest(YouDrawMeGuessComm comm)
	{
		comm.offerPictrue(new Bean_picture(10001, 10001, 1, 2, 1, 1));

	}
	
	public static void quit()
	{
		YouDrawMeGuessComm_test.thread_receiver.close();
	}
}
