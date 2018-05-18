package com.HIM.test.client;

import com.HIM.client.*;
import com.HIM.common.Bean_fileinfo;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Demo_ChatPanel
{
	private JFrame frame;
	private JTextPane ChatOutout;
	private JTextPane ChatInput;
	private StyledDocument input_doc;
	private StyledDocument output_doc;
	private Server_API robot;
	public static final String facePath = "T:/桌面/java课设/UI样例oim-fx-ui/Resources/Images/Face/";
	public static final String filePath = "T:/桌面/java课设/";
	
	public static void main(String args[])
	{
//		com.HIM.server.Main.main(new String[] {"text"});
		new Demo_ChatPanel();

	
	}
	
	public Demo_ChatPanel()
	{
		this.robot = new Server_API(TesterConfig.server_ip, TesterConfig.server_port, null);
		LaunchFrame();
	}
	
	private void LaunchFrame()
	{
		this.frame = new JFrame();
		this.ChatInput = new JTextPane();
		this.ChatOutout = new JTextPane();
		
		
		this.input_doc = this.ChatInput.getStyledDocument();
		this.output_doc = this.ChatOutout.getStyledDocument();
		
		JButton jb1 = new JButton("send");
		JButton jb2 = new JButton("face");
		JButton jb3 = new JButton("pic");
		jb1.addActionListener( e -> sendMessage() );
		jb2.addActionListener( e -> insertIcon(2) );
		jb3.addActionListener( e -> insertPicture("T:/桌面/Pencil.jpg") );
		
		frame.setLayout(new BorderLayout());
		frame.add(this.ChatOutout, BorderLayout.CENTER);
		frame.add(this.ChatInput,BorderLayout.SOUTH);
		frame.add(jb1,BorderLayout.EAST);
		frame.add(jb2,BorderLayout.NORTH);
		frame.add(jb3,BorderLayout.WEST);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	private void sendMessage()
	{
		String content = this.ChatInput.getText();
		if (!content.equals(""))
		{
			//把输出改为包装成   robot.sendMessage(message);
			System.out.println(content);
			this.ChatInput.setText("");
			OutputMessage(content);
			this.ChatInput.requestFocusInWindow();
		}
	}
	
	
	
	
	private void insertIcon(int value)
	{	 
	    Style style = input_doc.addStyle("face", null);
	    StyleConstants.setIcon(style, new ImageIcon(facePath + value + ".gif"));
	    try
		{
	    	//加个希腊字母魔法值作表情分割
			input_doc.insertString(this.ChatInput.getCaretPosition(), "φ" + "α" + value + "φ" , style);
			this.ChatInput.requestFocusInWindow();
		} catch (BadLocationException e)
		{
			e.printStackTrace();
		}
	}
	
	private void insertPicture(String path)
	{
		//先发送到服务器得到文件编号
		Bean_fileinfo file = new Bean_fileinfo("picture", new File(path));
		file.setName(file.getMD5());
		int index = robot.uploadFile(file);
		if (index == -1) return;
		Style style = input_doc.addStyle("picture", null);
	    StyleConstants.setIcon(style, new ImageIcon(path));
	    try
		{
	    	//加个希腊字母魔法值作表情分割
			input_doc.insertString(this.ChatInput.getCaretPosition(), "φ" + "β" + file.getMD5() + "φ" , style);
			this.ChatInput.requestFocusInWindow();
		} catch (BadLocationException e)
		{
			e.printStackTrace();
		}		
	}
	
	/*private String getInputContent() 
	{
		StringBuilder sb = new StringBuilder("");
		int len = this.ChatInput.getText().length();
		//遍历jtextpane找出所有的文字，图片信息封装成指定格式
		for(int i = 0; i < len ;)
		{
			Element element = input_doc.getCharacterElement(i);
			int s = element.getStartOffset();
			int e = element.getEndOffset();
			String text = "";
			try
			{
				text = input_doc.getText(s, e-s);
			} catch (BadLocationException e1)
			{
				e1.printStackTrace();
			}
			sb.append(text);
			i = e;
		 }
		 return sb.toString();
	}*/
	
	
	public void OutputMessage(String content)
	{
		String strs[] = content.split("φ");
		for(String str : strs)
		{
			if (!str.equals(""))
			{
				try
				{
					if (str.charAt(0) == 'α') //自带表情
					{
						String index = str.substring(1);
						Style style = output_doc.addStyle("face", null);
					    StyleConstants.setIcon(style, new ImageIcon(facePath + index + ".gif"));
					    output_doc.insertString(output_doc.getLength(), "φ" + "α" + index + "φ" , style);
					}else if (str.charAt(0) == 'β') //图片
					{
						String md5 = str.substring(1);
						File picture = new File(filePath + md5);
						if (!picture.exists())
						{
							robot.downloadFile_md5(md5, picture);
						}
						if (picture.exists()) 
						{
							Style style = output_doc.addStyle("picture", null);
							StyleConstants.setIcon(style, new ImageIcon(filePath + md5));//填下载了的表情目录
							output_doc.insertString(output_doc.getLength(), "φ" + "β" + md5 + "φ" , style);							
						}
					}else 
					{
						output_doc.insertString(output_doc.getLength(), str , null);
					}	
				}catch (BadLocationException e) 
				{
					e.printStackTrace();
				}
			}
		}
		//输出一个换行
		try
		{
			output_doc.insertString(output_doc.getLength(), "\n" , null);
		} catch (BadLocationException e)
		{
			e.printStackTrace();
		}
	}
	

	
}
