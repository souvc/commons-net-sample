package com.souvc.common.net;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Test;

import java.io.IOException;

/**
 * FTPClient 配置对象
 */
public class FTPClientConfigTest {




    @Test
    public   void  test(){
        FTPClient ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        //config.setXXX(YYY);  change required options
        // for example config.setServerTimeZoneId("Pacific/Pitcairn")

        ftp.configure(config );
        boolean error = false;
        try {
            int reply;
            String server = "127.0.0.1";
            ftp.setDefaultPort(21);
            ftp.connect(server);
            System.out.println("Connected to " + server + ".");
            ftp.login("userftp1","123456");
            System.out.print(ftp.getReplyString());

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftp.getReplyCode();

            if(!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }

            // transfer files
            FTPFile[] files = ftp.listFiles();//获取该目录下全部的文件
            for (int i = 0; i < files.length; i++) {
                System.out.print(files[i].getName());
            }

            ftp.logout();
        } catch(IOException e) {
            error = true;
            e.printStackTrace();
        } finally {
            if(ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch(IOException ioe) {
                    // do nothing
                }
            }
            System.exit(error ? 1 : 0);
        }
    }
}
