package utils;

import jcifs.UniAddress;
import jcifs.smb.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

/**
 * @Author youxingyang
 * @date 2017-7-31 下午5:05:24
 */
public final class SambaUtil {

    private SambaUtil() {
    }

    /**
     * 从服务器上下载指定的文件到本地目录
     *
     * @param remoteFileUrl Samba服务器远程文件的路径
     * @param localDir      本地目录的路径
     */
    public static void downloadFileFromSamba(String remoteFileUrl, String localDir) {
        if ((remoteFileUrl == null) || ("".equals(remoteFileUrl.trim()))) {
            System.out.println("Samba服务器远程文件路径不可以为空");
            return;
        }
        if ((localDir == null) || ("".equals(localDir.trim()))) {
            System.out.println("本地目录路径不可以为空");
            return;
        }

        InputStream in = null;
        OutputStream out = null;

        try {
            //创建一个File对象对应远程服务器上的File
            SmbFile remoteSmbFile = new SmbFile(remoteFileUrl);

            //获取远程文件的文件名,这个的目的是为了在本地的目录下创建一个同名文件
            String remoteSmbFileName = remoteSmbFile.getName();

            //本地文件由本地目录，路径分隔符，文件名拼接而成
            File localFile = new File(localDir + File.separator + remoteSmbFileName);
            // 如果路径不存在,则创建
            File parentFile = localFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            //打开文件输入流，指向远程的smb服务器上的文件，特别注意，这里流包装器包装了SmbFileInputStream
            in = new BufferedInputStream(new SmbFileInputStream(remoteSmbFile));
            //打开文件输出流，指向新创建的本地文件，作为最终复制到的目的地
            out = new BufferedOutputStream(new FileOutputStream(localFile));

            //缓冲内存
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) {
                out.write(buffer);
                buffer = new byte[1024];
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从服务器上下载指定的文件到本地目录
     *
     * @param remoteSmbFile Samba服务器远程文件
     * @param localDir      本地目录的路径
     * @throws SmbException
     */

    public static void downloadFileFromSamba(SmbFile remoteSmbFile, String localDir) throws SmbException {

        //入参检查
        if (!remoteSmbFile.exists()) {
            System.out.println("Samba服务器远程文件不存在");
            return;
        }
        //入参检查
        if ((localDir == null) || ("".equals(localDir.trim()))) {
            System.out.println("本地目录路径不可以为空");
            return;
        }

        InputStream in = null;
        OutputStream out = null;

        try {
            //获取远程文件的文件名,这个的目的是为了在本地的目录下创建一个同名文件
            String remoteSmbFileName = remoteSmbFile.getName();

            //本地文件由本地目录，路径分隔符，文件名拼接而成
            File localFile = new File(localDir + File.separator + remoteSmbFileName);
            // 如果路径不存在,则创建
            File parentFile = localFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            //打开文件输入流，指向远程的smb服务器上的文件，特别注意，这里流包装器包装了SmbFileInputStream
            in = new BufferedInputStream(new SmbFileInputStream(remoteSmbFile));
            //打开文件输出流，指向新创建的本地文件，作为最终复制到的目的地
            out = new BufferedOutputStream(new FileOutputStream(localFile));

            //缓冲内存
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) {
                out.write(buffer);
                buffer = new byte[1024];
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传本地文件到Samba服务器指定目录
     *
     * @param remoteDirUrl  Samba服务器远程目录的路径
     * @param localFilePath 本地文件路径
     */
    public static void uploadFileToSamba(String remoteDirUrl, String localFilePath) {

        //入参检查
        if ((remoteDirUrl == null) || ("".equals(remoteDirUrl.trim()))) {
            System.out.println("Samba服务器远程目录路径不可以为空");
            return;
        }

        //入参检查
        if ((localFilePath == null) || ("".equals(localFilePath.trim()))) {
            System.out.println("本地文件路径不可以为空");
            return;
        }

        InputStream in = null;
        OutputStream out = null;

        try {
            //创建一个本地文件对象
            File localFile = new File(localFilePath);

            //获取本地文件的文件名，这个名字用于在远程的Samba服务器上指定目录创建同名文件
            String localFileName = localFile.getName();

            //创建远程服务器上Samba文件对象
            SmbFile remoteSmbFile = new SmbFile(remoteDirUrl + File.separator + localFileName);

            //打开一个文件输入流执行本地文件，要从它读取内容
            in = new BufferedInputStream(new FileInputStream(localFile));

            //打开一个远程Samba文件输出流，作为复制到的目的地
            out = new BufferedOutputStream(new SmbFileOutputStream(remoteSmbFile));

            //缓冲内存
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) {
                out.write(buffer);
                buffer = new byte[1024];
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传本地文件到Samba服务器指定目录
     *
     * @param url           URL
     * @param auth          auth
     * @param localFilePath 本地文件路径
     * @throws MalformedURLException
     * @throws SmbException
     */
    public static void uploadFileToSamba(String url, NtlmPasswordAuthentication auth, String localFilePath) throws MalformedURLException, SmbException {
        //入参检查
        if ((localFilePath == null) || ("".equals(localFilePath.trim()))) {
            System.out.println("本地文件路径不可以为空");
            return;
        }

        //检查远程父路径，不存在则创建
        SmbFile remoteSmbFile = new SmbFile(url, auth);
        String parent = remoteSmbFile.getParent();
        SmbFile parentSmbFile = new SmbFile(parent, auth);
        if (!parentSmbFile.exists()) {
            parentSmbFile.mkdirs();
        }

        InputStream in = null;
        OutputStream out = null;

        try {
            File localFile = new File(localFilePath);

            //打开一个文件输入流执行本地文件，要从它读取内容
            in = new BufferedInputStream(new FileInputStream(localFile));

            //打开一个远程Samba文件输出流，作为复制到的目的地
            out = new BufferedOutputStream(new SmbFileOutputStream(remoteSmbFile));

            //缓冲内存
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) {
                out.write(buffer);
                buffer = new byte[1024];
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param args
     * @throws UnknownHostException
     * @throws SmbException
     * @throws MalformedURLException
     */
    public static void main(String[] args) throws UnknownHostException, SmbException, MalformedURLException {
        String host = "192.168.60.40";
        String username = "publicxxxx@xxx.inc";
        String password = "123456a";
        String filePath = "E:\\abc\\test.pdf";
        //Demo1: 演示从Samba服务器上下载指定的文件到本地
        System.out.println("Demo1: Downloading File from Samba Server to Local");
        //"samba:samba_password@192.168.71.43/samba/demo1/testFile1.jpg";
        String demo1RemoteSambaFileUrl = "smb://" + username + ":" + password + "@" + host + filePath;
        String demo1LocalDir = "E:\\test\\samba";

        UniAddress ua = UniAddress.getByName(host);
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(host, username, password);
        //验证是否能够成功登录
        SmbSession.logon(ua, auth);
        //创建Smb文件,地址一定要使用smb://
        SmbFile remoteSmbFile = new SmbFile("smb://" + host + filePath, auth);
        SambaUtil.downloadFileFromSamba(remoteSmbFile, demo1LocalDir);
        System.out.println("download success");

        //Demo2: 演示上传文件到Samba服务器指定目录
        System.out.println("Demo2:Uploading File from Local to Samba Server");
        String demo2LocalFile = "E:\\test\\samba\\test.pdf";

        String sambaDir = "E:\\abc";
        String demo2RemoteSambaDirUrl = "smb://" + username + ":" + password + "@" + host + sambaDir;

        String filePathUpload = sambaDir + "/" + new File(demo2LocalFile).getName();
        String url = "smb://" + host + filePathUpload;
        SambaUtil.uploadFileToSamba(url, auth, demo2LocalFile);
        System.out.println("upload success");
    }

}
