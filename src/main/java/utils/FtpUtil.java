package main.java.utils;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * @Author: youxingyang
 * @date: 2018/4/2 16:35
 */
public final class FtpUtil {
    private FtpUtil() {}

    /**
     * 连接sftp服务器
     * @param host
     * @param port
     * @param username
     * @param password
     * @return
     */
    public static ChannelSftp connect(String host, int port, String username, String password) {

        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            System.out.println("Session created.");

            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            System.out.println("Session connected.");
            System.out.println("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + host + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sftp;
    }

    /**
     * 上传文件
     * @param directory 上传的目录
     * @param uploadFile 要上传的文件
     * @param sftp
     */
    public static void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     * @param directory 上传的目录
     * @param uploadFile 要上传的文件
     * @param sftp
     */
    public static void upload(InputStream is, String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(is, file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     * @param directory 下载目录
     * @param downloadFile 下载的文件
     * @param saveFile 存在本地的路径
     * @param sftp
     */
    public static void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            sftp.get(downloadFile, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     * @param directory 要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @param sftp
     */
    public static void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 列出目录下的文件
     * @param directory 要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    public static Vector<?> listFiles(String directory, ChannelSftp sftp) throws SftpException {
        return sftp.ls(directory);
    }

    protected static void mkdir(String directory, ChannelSftp sftp) {
        try {
            sftp.ls(directory);
        } catch (SftpException e) {
            try {
                sftp.mkdir(directory);
            } catch (SftpException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *
     * @param directory
     * @param sftp
     */
    public static void mkdirs(String directory, ChannelSftp sftp){
        List<String> fileNames = getFileNames(directory);
        StringBuffer buf = new StringBuffer();
        if (fileNames != null && fileNames.size() > 0) {
            for (String fileName : fileNames) {
                buf.append("/");
                buf.append(fileName);
                mkdir(buf.toString(), sftp);
            }
        }
    }

    protected static List<String> getFileNames(String dir){
        File file = new File(dir);
        List<String> list = new ArrayList<String>();
        if (file.getParentFile() != null) {
            List<String> fileNames = getFileNames(file.getParentFile().getPath());
            for (String fileName : fileNames) {
                if (fileName != null && !"".equals(fileName)) {
                    list.add(fileName);
                }
            }
        }
        if (!"".equals(file.getName())) {
            list.add(file.getName());
        }
        return list;
    }

    /**
     * 从ftp上下载文件到localPath
     * @param fileName
     * @param localPath
     * @return
     */
    public boolean download(String fileName, String localPath) {
        boolean success = false;
        try {
            String filename = "/config.properties";
            Properties props = new Properties();
            ChannelSftp sftp = null;
            try {
                InputStream in = this.getClass().getResourceAsStream(filename);
                props.load(in);
                String host = props.getProperty("sftp.host");
                String port = props.getProperty("sftp.port");
                String username = props.getProperty("sftp.username");
                String password = props.getProperty("sftp.password");
                String path = props.getProperty("sftp.path2");

                sftp = connect(host, Integer.parseInt(port), username, password);

                if (!"".equals(fileName)) {
                    download(path, fileName, localPath, sftp);
                    success = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                File file = new File(localPath);
                if (file.length() == 0) {
                    System.out.println(file.delete());
                }
            } finally {
                if (sftp != null) {
                    sftp.quit();
                    sftp.getSession().disconnect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        String host = "101.201.238.148";
        int port = 10022;
        String username = "root";
        String password = "Axxxxxx!";
        String directory = "/samples/a/b/c/d/e/f/g/h/i";
        String uploadFile = "d:\\test.jpg";
        String downloadFile = "/samples/test.jpg";
        String saveFile = "c:\\test.jpg";
        ChannelSftp sftp = connect(host, port, username, password);

        mkdirs(directory, sftp);

        download(directory, downloadFile, saveFile, sftp);

        try {
            upload(directory, uploadFile, sftp);
            System.out.println("finished");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sftp.quit();
            try {
                sftp.getSession().disconnect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }
    }
}
