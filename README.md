# HIM

一个纯Java纯TCP协议的即时通讯程序。HIM即`Here I am` or `Here Instant Message`，来自于我的【高级程序设计语言课程设计】课程，对服务端和客户端通信部分开源。

 ![version](https://img.shields.io/badge/version-v1.0-blue.svg)  ![jdk](https://img.shields.io/badge/java-1.8.0-green.svg) ![mysql](https://img.shields.io/badge/mysql-v5.7-brightgreen.svg) [![licence](https://img.shields.io/badge/licence-Apache2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) ![netIO](https://img.shields.io/badge/netIO-TCP/IP-green.svg)



## 目录

* [课设要求](#课设要求)
* [服务端架设](#服务端架设)

  * Windows
  * Linux
* [项目介绍](#项目介绍)

  * 目录结构
  * 服务端原理简单阐述
* [开发历程](#开发历程)

  * 前言
  * 需求分析
  * 数据库建模
  * 数据访问层设计
  * 缓存数据设计
  * 通讯协议层设计

## 课设要求

> **题目名称：Mini QQ**  
> 基本要求：
> 本次设计要求利用Java实现支持2-N人联机的迷你QQ应用程序PC版，支持如下基本需求:  
> - 仿照腾讯QQ开发系统的应用功能（通过模仿和自己创意）。  
> - 必备功能：  
>   - [x] 二人即时聊天  
>
>   - [x] 群聊功能  
>
>   - [x] 传递文件（在线传、离线传）  
>
>   - [x] 实时发送表情与图片、视频  
>
>   - [x] 即时手动画图功能——画者所画，双方实时可见
>
> - 拓展功能：
>
>   - [ ] 在现有腾讯QQ已有功能的启发下，创新开发出更多的应用功能


## 服务端架设

有没有办法能马上用上这个服务端呢？有，所以我把这章节置前了。

 * #### Windows

1. 安装好Java，并配置好环境变量，我的版本是 "JDK1.8.0_131"(在cmd中输入java -version即可显示版本)。

2. [安装好MySQL](http://www.jb51.net/article/23876.htm)，由于喜好精简，于是开发环境版本是"v5.0.96"，当然在linux下"v5.7.21"也成功了。

3. 进入MySQL，建立一个数据库：

   (1). 在命令行输入：`mysql -u root -p`，再输入密码进入数据库

   (2). 输入 `CREATE DATABASE HIM_Db CHARACTER SET UTF8; `

   (3). 关于建表，com.HIM.server.Db_Init是自动建表的类

4. 把本仓库源码下载，使用eclipse对目录建立Java Project，在src/com/HIM/server下有Main.java，即main方法所在地。

5. 在启动服务端前还有一件事就是配置[HIM_server.properties](https://github.com/zhangt2333/HIM/tree/master/HIM_server.properties)文件，该文件在项目目录下，尤其注意"JDBC基本信息"的配置。

6. 先启动服务端，~~再启动客户端~~(昂，客户端没开源，不过我会上传打包好的程序到release的)

 * #### Linux

1. 部署完成后用Putty连接服务器，上来先更新：

   `$ sudo apt-get update`

   `$ sudo apt-get upgrade`

2. 然后是安装Mysql:

   `$ sudo apt-get install mysql-server`

3. 修改它的配置：

   `$ vim /etc/mysql/mysql.conf.d/mysqld.cnf`

    将bind-address = 127.0.0.1这行使用#注释掉，并且在[mysqld]字段后加上 character-set-server=utf8。由此实现开发环境连接生产环境数据库，和解决编码问题。

4. 然后是安装JDK:

   `$ sudo apt-get install default-jdk`

5. 配置好[HIM_server.properties](https://github.com/zhangt2333/HIM/tree/master/HIM_server.properties)文件先，该文件在项目目录下，尤其注意"JDBC基本信息"的配置。

6. 创建一个项目目录，用**SFTP**把项目传上去：

   ```
   psftp> open xx.xx.xxx.xx                               //你的服务器ip
   login as: root                                         //输入用户名和密码了
   root@xx.xx.xxx.xx's password:
   Remote working directory is /root
   psftp> sudo mkdir /var/tttt/HIM_server                  //我创的项目目录
   psftp> put -r t:/桌面/HIM/ /var/tttt/HIM_server/
   ```

7. SSH中后台挂起服务端：

   `$ nohup java -jar /var/tttt/HIM_server/HIM_server.jar >> log/log.txt &`

   若要实时调试则输入：``$ java -jar /var/tttt/HIM_server/HIM_server.jar test `

8. ~~启动客户端~~

## 项目介绍

* #### 目录结构

  ```
  HIM
  ├─src
  │  └─com
  │      └─HIM
  │          ├─client
  │          │      FileTransfer.java              //托管文件传输的类
  │          │      Server_API.java                //与服务端通信的api
  │          │      Thread_receiver.java           //纯接收服务端信息的线程
  │          │      YouDrawMeGuessComm.java        //实时画图数据传输者
  │          │      ...
  │          ├─common                              //一些Bean类和工具类
  │          │      Bean_fileinfo.java
  │          │      Bean_friendinfo.java
  │          │      Bean_message.java
  │          │      Bean_mood.java
  │          │      Bean_picture.java
  │          │      Bean_quninfo.java
  │          │      Bean_subgroup.java
  │          │      Bean_userinfo.java
  │          │      Enum_constellation.java
  │          │      Enum_sex.java
  │          │      logger.java
  │          │      SubGroupManager.java
  │          │      tools.java
  │          └─server
  │                  config.java                          
  │                  Db_C3P0_ConnectionPoolUtil.java      //数据库连接池     
  │                  Db_Init.java                         //数据库初始化类
  │                  Db_Operate.java                      //数据库操作类
  │                  logger.java                          //日志记录类
  │                  Main.java
  │                  OnlineUserManager.java               //在线用户管理类
  │                  QunMessageManager.java               //群功能托管类
  │                  ServerThreadsManager.java            //连接线程管理类
  │                  Thread_CommandLineControl.java       //命令行界面控制类
  │                  Thread_Greeter.java                  //ServerSocket
  │                  Thread_Server.java                   //响应客服端请求
  └─test
      └─com
          └─HIM
              └─test                 
                  ├─client
                  │      Demo_ChatPanel.java
                  │      Demo_test.java
                  │      FileTransfer_test.java
                  │      Server_API_test.java
                  │      TesterConfig.java
                  │      YouDrawMeGuessComm_test.java
                  └─server
                         Db_Operate_test.java
  ```

* #### 服务端原理简单阐述（~~接下来要施展口技了~~）

  ```
  业务处理的实现：（模型:客户端<->TCP<->服务端<->JDBC<->Mysql数据库）
  1.客户端——Socket->服务端
  2.服务端——JDBC->MySQL
  3.MySQL——处理完->服务端
  4.服务端——Socket->客户端
   
  聊天功能的实现：（由于是纯TCP且客户端之前不连接，故客户端间通讯都是通过服务器中介）
  1.客户登录后，把其接收者线程<int UserID->String"IP:Port">信息put进一个管理OnlineUser的HashMap里面。
  2.当客户端发送消息Bean_message后，取出其中的接受者ID，拿到OnlineUser的HashMap中映射一下，有则接而将得到的String"IP:Port"在ServerThread的HashMap映射得到线程，将message通过线程中的Object流写出。
  
  群聊功能的实现：维护一个<群号->群成员数组>的Map，里面就是服务器拿到message后判断为群消息类型进而对群中每一个成员按部就班"施展"聊天功能，批量转发message。
  
            | -> 读取配置文件"HIM_server.properties"成常量设在config类
            | -> 日志功能初始化
            | -> c3p0连接池初始化
  服务端启动 |
            | -> 数据库建表，exist则不建
            | -> 群功能托管类启动
            | -> 主线程while循环中ServerSocket不断接入新的连接，并把它丢入线程池和一个管理ServerThread的<String"IP:Port"->Thread>的HashMap中
           
            | -> 先用Thread_receiver连接上服务端
  客户端启动 | -> 再用Server_API连接上服务端
            | -> 调用Server_API的功能就行了
  ```


​        ~~emmmmm，还有什么要说的，想不到了啊，要不上代码吧~~

## 开发历程

 * #### 前言

   * Thanks for coming here.
   * 从Week2到Week11。作为学习JAVA以来的第一个项目，她的意义已经不再局限于一个课设了，就像教育孩子直到成其可用之才。
   * 很长一段时间我是在不断学习高级技术和推翻自己先前的想法。
   * 对于代码中魔法值肆虐、包结构过于精简、类名过于"神奇"等现象，请给我提Issue。如果能教一下我该怎么修正错误的习惯，感激不尽！
   * 这一章虽说是"开发历程"，其实也是摘抄我实验报告的部分罢了，或所尽口舌之快而不得其髓。
   * 另外，客户端是我的小伙伴写的，所以我无法开源，但已经将程序Demo上传到[release](https://github.com/zhangt2333/HIM/releases)
   * 还有，Gayhub作为最大的~~同性交友平台~~，欢迎来邮件~



 * ####  需求分析

```
	本次课程设计立足于市场上热门的即时通讯软件QQ为原型，要求设计了一套自己的聊天软件模式，实现基本的聊天、数据传输功能，并提供了很大的发散空间，可以让我们添加更多的个性设计。

	于此，需求分析方面很简单：

	客户端，用户的使用部分，提供主要的界面和服务请求，如：登录界面、注册界面、信息查看修改界面、主窗体界面、聊天界面等。通过网络发送请求到服务端，并接受服务端反馈，处理、显示结果。

	服务端，提供服务的开启、关闭功能，负责处理核心的业务逻辑，负责连接数据库，保存和读取数据。通过网络接收客户端发送的请求，间接操作数据库，反馈结果于客户端，并用日志记录客户端请求。

	业务方面，提供业务有：注册、登录、登出、查询个人信息、查询分组列表、查询好友列表、昵称查询用户、ID查询用户、批量查询用户、更新好友列表、更新个人信息、更新头像、更改密码、上传文件、下载文件、上传头像、下载头像、发送消息（个人消息、群消息、图片、文件、“你画我猜”请求）、查询未读消息、群号查询群、群名查询群、查询用户所属群、查询群成员列表、上传群文件、查询群文件、创建群、加入群、退出群、更新群消息、更新个人的群名片、发表说说、查询好友说说。
```

 * #### [数据库建模](https://github.com/zhangt2333/HIM/tree/master/src/com/HIM/server/Db_Init.java)

```
1.UserInfo(UserID,Passwd,Sign,PhotoIndex,Nickname,Sex,Birthday,Constellation,ApplyDate)
2.ChatRecord(Index,SendTime,Sender,Type,Receiver,QunID,Content,Fonttype,Fontsize,Fontcolor,Readed)
3.FileInfo(InfoIndex,Type,Size,MD5,Name,Path)
4.LoginInfo(InfoIndex,UserID,IP,Port,LoginTime,Status)
5.MoodRecord(Index,Poster,PostTime,Content)
6.PhotoInfo(InfoIndex,MD5,Path)
7.QunFile(Index,QunID,Uploader,UploadTime,FileIndex)
8.QunInfo(QunID,QunName,PhotoIndex,CreateTime)
9.SubGroup(GroupIndex,UserID,GroupName)
10.UserEmoticon(Index,Fileindex,UserID)
11.UserFriend(UserID,FriendID,GroupIndex,Alias)
12.UserQun(Relation,UserID,QunID,Alias,JoinTime)
```

 * #### 数据访问层设计

```
	数据访问层，即将数据库写出读出的相关及其支持方法归分到相应的数据库操作类，在这里，主要是Db_Operate类和Db_Init类，里面提供了类似于register、login、addFriend等写入数据的静态方法，和queryUser、queryQuninfo等查询数据的静态方法。
	
    此外，数据库连接的池化管理对数据访问的效率和系统的稳定性有些极大的作用。引入连接池，即对于一个服务端，初始化之时创建初始数量的Connection，每次请求时就直接从连接池拿取，用完放回，拿取之前，连接池对拿取前进行Connection可效性检验，失效或当前无可用连接则新建连接后返回。 
    
    JDK没有提供连接池。我使用了Java比较主流的四大连接池之一C3P0连接池，一个开源的JDBC连接池。使用起来还是蛮简单的。只要封装一个方法，方法里面对配置好的ComboPooledDataSource使用.getConnectons()方法就行了。
```

 * #### [缓存数据设计](https://github.com/zhangt2333/HIM/tree/master/src/com/HIM/common/)

```
	在客户端与客户端通讯的时候，不同功能传输的数据量及类型不一，考虑到数据的存储和项目的可维护性，设计了一些Bean类，在网络IO的时候就可以直接传输对象。设计的Bean类有如下：
	Bean_userinfo、Bean_friendinfo、Bean_subgroup、Bean_quninfo、Bean_fileinfo、Bean_message，类之用途如其名。
```

 * #### 通讯协议层设计

```
	计算机之间数据传输的常用通讯协议有两种，即TCP和UDP。TCP是可靠的面向连接的通信协议，UDP是不可靠的面向无连接的通信协议。
	
	在服务端与客户端通讯中，由于多是处理像登录、加好友、查询群等交互的业务，故采用了TCP协议，长连接维护数据流和对象流，从而实现一次连接，随时业务。
	
    在客户端与客户端的通讯中，起初在实现聊天功能是时候设想采用UDP协议，客户端从服务端拿到好友们IP和UDP端口，服务端起到一个维护在线用户信息的作用，聊天交给客户端们自己实现。但其网络条件较差时常出现掉包现象和无固定外网ip情况下将无法通讯情况，于是退而继续采用TCP协议，利用”转发”，实现客户端与客户端的连接。
```