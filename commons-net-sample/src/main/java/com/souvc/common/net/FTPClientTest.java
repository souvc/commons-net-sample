package com.souvc.common.net;

import org.apache.commons.net.ftp.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.SocketException;

/**
 * FTPClient--FTP操作帮助类，上传下载，文件，目录操作
 * FTPClient封装了所有必要的功能来存储和检索从FTP服务器上的文件。
 */
public class FTPClientTest {

    FTPClient ftp = new FTPClient();//创建一个FTPClient对象

    /***
     * #################登陆ftp服务器相关的方法##################################################################################
     */


    /**
     * 通过用户名和密码登陆并获取文件
     * @throws IOException
     */
     @Before
     public  void  login() throws IOException {
         ftp.connect("127.0.0.1");//填写服务器， 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
         ftp.login("userftp1", "123456");//填写用户名和密码
         //client.login("anonymous", "密码任意设置");
     }

    /**
     * connect 可以设置端口号
     * @throws IOException
     */
    @Test
    public  void  connect() throws IOException {
        ftp.connect("127.0.0.1",21);//填写服务器,填写端口号
        ftp.login("userftp1", "123456");//填写用户名和密码
        FTPListParseEngine engine = ftp.initiateListParsing("/");
        FTPFile[] files= engine.getFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.print(files[i].getName());
        }
    }


    /**
     * 判断ftp是否连接成功
     */
    @Test
    public  void  isPositiveCompletion(){
        try {
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {//获取返回码，判断是否连接
                System.out.println("未连接到FTP，用户名或密码错误。");
                ftp.disconnect();
            } else {
                System.out.println("FTP连接成功。");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FTP的端口错误,请正确配置。");
        }

    }



    /**
     * 退出登陆
     */
    @After
    public  void  logout() throws IOException {
        ftp.logout();//退出登陆
        ftp.disconnect();//安全退出 // 关闭FTP服务器的连接
    }


    /**
     * et timeout to 5 minutes
     */
    @Test
    public  void  setControlKeepAliveTimeout() throws IOException {
       ftp.setControlKeepAliveTimeout(300); // set timeout to 5 minutes
    }



    /***
     * #################ftp服务器目录操作相关的方法#################################################################
     */


    /**
     * 读取全部目录
     */
    @Test
    public void listDirectories() throws IOException {
        FTPFile[] files = ftp.listDirectories();
        for (int i = 0; i < files.length; i++) {
            System.out.print(files[i].getName()+",");
        }
    }


    /**
     * 列出指定目录下的目录
     */
    @Test
    public void  listDirectories2() throws IOException {
        FTPFile[] files = ftp.listDirectories("/user");
        for (int i = 0; i < files.length; i++) {
            System.out.print(files[i].getName()+",");
        }
    }


    /**
     * 进入到服务器的某个目录下（绝对路径）
     */
    @Test
    public void changeWorkingDirectory() throws IOException {
            ftp.changeWorkingDirectory("/user");
            FTPFile[] files = ftp.listFiles();
            for (int i = 0; i < files.length; i++) {
                System.out.print(files[i].getName());
            }
    }


    /**
     *进入到服务器的某个目录下（相对路径）
     */
    @Test
    public void changeWorkingDirectory2() throws IOException {
        ftp.changeWorkingDirectory("/user");
        ftp.changeWorkingDirectory("user2");
        FTPFile[] files = ftp.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.print(files[i].getName());
        }
    }

    /**
     * 返回到上一层目录
     */
    @Test
    public  void changeToParentDirectory() throws IOException {
        ftp.changeWorkingDirectory("/user");
        FTPFile[] files = ftp.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.print(files[i].getName()+",");
        }
        System.out.println();
        ftp.changeToParentDirectory();
        FTPFile[] files2 = ftp.listFiles();
        for (int i = 0; i < files2.length; i++) {
            System.out.print(files2[i].getName()+",");
        }
    }


    /**
     * 当前工作的目录位置
     */
    @Test
    public  void printWorkingDirectory() throws IOException {
        ftp.changeWorkingDirectory("/user/user1");
        String path=ftp.printWorkingDirectory();
        System.out.print(path);
    }


    /**
     * 创建目录
     */
    @Test
    public  void  makeDirectory() throws IOException {
        Boolean flag=ftp.makeDirectory("ftpdir1");
        Boolean flag2=ftp.makeDirectory("ftpdir2");
        System.out.println(flag);
        System.out.println(flag2);
    }


    /**
     * 删除空目录
     */
    @Test
    public  void  removeDirectory() throws IOException {
        Boolean flag=ftp.removeDirectory("ftpdir");
        System.out.print(flag);
    }


    /**
     * 重命名
     */
    @Test
    public  void  rename() throws IOException {
        //直接用文件名
        boolean renameflag = ftp.rename("ftpdir1" , "ftpdir3");
        System.out.println(renameflag);

        //加上路径名字
        boolean renameflag2 = ftp.rename("/ftpdir2" , "/ftpdir4");
        System.out.println(renameflag2);

        //多级目录（注:需要先跳转到文件所在目录。）
        boolean renameflag3 = ftp.rename("/user/user1" , "/user/user2");
        System.out.println(renameflag3);

    }


    /***
     * #################ftp服务器文件操作相关的方法#################################################################
     */


    /**
     * 设置传输文件的类型[文本文件或者二进制文件  设置传输模式  设置文件传输类型
     */
    @Test
    public  void setFileType() throws IOException {
         ftp.setFileType(FTP.BINARY_FILE_TYPE);
         ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
    }

    /**
     *  获取指定目录下全部文件夹以及文件
     */
    @Test
    public  void   getFiles() throws IOException {
        FTPListParseEngine engine = ftp.initiateListParsing("/");
        FTPFile[] files= engine.getFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.print(files[i].getName()+",");
        }
    }

    /**
     * 列出目录下全部文件
     */
    @Test
    public  void  listFiles() throws IOException {
        FTPFile[] files = ftp.listFiles();//获取该目录下全部的文件
        for (int i = 0; i < files.length; i++) {
            System.out.print(files[i].getName()+",");
            // System.out.print( new String(files[i].getName().getBytes("GBK"),"iso-8859-1"));// 转换后的目录名或文件名。
        }
    }

    /**
     * 列出指定目录下全部文件
     */
    @Test
    public  void  listFiles2() throws IOException {
        FTPFile[] files = ftp.listFiles("/user");//获取该目录下全部的文件
        for (int i = 0; i < files.length; i++) {
            System.out.print(files[i].getName()+",");
            // System.out.print( new String(files[i].getName().getBytes("GBK"),"iso-8859-1"));// 转换后的目录名或文件名。
        }

    }


    /**
     * 指定解析类
     */
    @Test
    public  void   getNext() throws IOException {
        FTPListParseEngine engine = ftp.initiateListParsing("com.whatever.YourOwnParser","/");
        // while (engine.hasNext()) {
        FTPFile[] files = engine.getNext(3);  // "page size" you want
        //do whatever you want with these files, display them, etc.
        //expensive FTPFile objects not created until needed.
        for (int i = 0; i < files.length; i++) {
            System.out.print(files[i].getName());
        }
        //  }
    }

    /**
     * 指定显示数量
     */
    @Test
    public  void   getNext2() throws IOException {
        FTPListParseEngine engine = ftp.initiateListParsing("/");
        // while (engine.hasNext()) {
        FTPFile[] files = engine.getNext(3);  // "page size" you want
        //do whatever you want with these files, display them, etc.
        //expensive FTPFile objects not created until needed.
        for (int i = 0; i < files.length; i++) {
            System.out.print(files[i].getName());
        }
        //  }
    }




    /**
     * 上传文件
     */
    @Test
    public  void  storeFile() throws IOException {
        FileInputStream in=new FileInputStream(new File("D:/jedis-2.5.3.jar"));
        ftp.storeFile("jedis-2.5.3.jar",in);
    }


    /**
     * 上传文件设置参数
     */
    @Test
    public  void   storeFile2() throws IOException {
        File srcFile = new File("F:\\QQ20161123201137.png");
        FileInputStream fis  = new FileInputStream(srcFile);
        //设置上传目录
        ftp.changeWorkingDirectory("/admin/pic");
        ftp.setBufferSize(1024);
        ftp.setControlEncoding("UTF-8");//这里设置编码
        //设置文件类型（二进制）
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        boolean temp = ftp.storeFile(new String("QQ20161123201137.png".getBytes("UTF-8"),"iso-8859-1"), fis);//编码转换
        System.out.println("temp-------"+temp);
    }

    /**
     * 上传文件
     */
    @Test
    public  void  storeFileDir() throws IOException {
        String localAbsoluteFile="d:/abcd/event.txt";//本地绝对路径
        String remoteAbsoluteFile="abc.txt"; //ftp目录路径
        InputStream input = new FileInputStream(new File(localAbsoluteFile));
        ftp.storeFile(remoteAbsoluteFile, input);// 处理传输
    }


    /**
     * 获取系统帮助信息从服务器返回的字符串。
     */
    @Test
    public  void  listHelp() throws IOException {
        String listHelp=ftp.listHelp();
        System.out.print(listHelp);
    }


    /**
     * 列出主目录下全部文件夹以及文件名称
     */
    @Test
    public  void  listNames() throws IOException {
        String[] listNames=ftp.listNames();
        for (int i = 0; i < listNames.length; i++) {
            System.out.print(listNames[i]+",");
        }
    }


    /**
     * 列出主目录下全部文件夹以及文件路径
     */
    @Test
    public  void  listNames2() throws IOException {
        String[] listNames=ftp.listNames("/mulu");
        for (int i = 0; i < listNames.length; i++) {
            System.out.print(listNames[i]+",");
        }
    }

    /**
     * 删除文件
     */
    @Test
    public  void  deleteFile() throws IOException {
        Boolean flag=ftp.deleteFile("/mulu/Chrysanthemum.jpg");
        System.out.print(flag);
    }


    /**
     * 批量删除文件
     */
    @Test
    public  void   delFiles() throws IOException {
        String[] delFiles = ftp.listNames("/user");
        for (String s : delFiles) {
            ftp.deleteFile(s);
        }
    }



    /**
     * 去 服务器的FTP路径下上读取文件
     */
    @Test
    public void readConfigFileForFTP() throws IOException {
            ftp.setControlEncoding("UTF-8"); // 中文支持
            //ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //ftp.enterLocalPassiveMode();
            //ftp.changeWorkingDirectory("/");

            InputStream in = ftp.retrieveFileStream("/user/event2.txt");//ftp服务器路径
            byte[] inOutb = new byte[in.available()]; //通过available方法取得流的最大字符数
            in.read(inOutb);  //读入流,保存在byte数组

            FileOutputStream outStream = new FileOutputStream("d:/abcd/event3.txt");//本地路径
            outStream.write(inOutb);

            in.close();
            outStream.close();

        }

}
