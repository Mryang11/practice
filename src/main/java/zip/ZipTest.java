package zip;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created on 2017/6/7
 * Author: youxingyang.
 */
public class ZipTest {
    private static int DEFAULT_BUFFER_SIZE = 16 * 1024;
    public static void main(String[] args) {
        //要压缩的路径
        String zipPath = "E://TESTZIP//新建文件夹";

        //生成的压缩文件位置
        String tmpFileName = "test.zip";
        String strZipPath = "E://TESTZIP" + File.separator + tmpFileName;

        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(strZipPath));
            File file = new File(zipPath);
            zip(out, file, file.getName());
            out.close();
            if (new File(strZipPath).length() > 0) {
                System.out.println("success zipped");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件或者文件夹-worker
     * @param zOut    zip输出流
     * @param file    文件或者文件夹
     * @param name    文件或者文件夹名称
     */
    public static void zip(ZipOutputStream zOut, File file, String name) {
        try {
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                name += "/";
                zOut.putNextEntry(new ZipEntry(name));
                if (listFiles != null) {
                    for (File listFile : listFiles) {
                        zip(zOut, listFile, name + listFile.getName());
                    }
                }
            } else {
                zOut.putNextEntry(new ZipEntry(name));
                writeFile(zOut,file);
            }
            zOut.setEncoding("GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 写文件到out
     * @param zOut
     * @param file
     * @throws IOException
     */
    public static void writeFile(ZipOutputStream zOut,File file) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(file);
        int len;
        while ((len = in.read(buffer)) > 0) {
            zOut.write(buffer, 0, len);
            zOut.flush();
        }
        in.close();
    }
}
