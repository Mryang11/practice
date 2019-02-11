package utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import java.io.*;

/**
 * @author youxingyang
 * @Date 2017-11-23 上午9:36:13
 */
public class ZipUtil {


    /**
     * 把单个PDF压缩进OUT,得同步
     *
     * @param file
     * @param out
     */
    public static synchronized void zip(File file, ZipOutputStream out) {

        byte[] buffer = new byte[16 * 1024];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            out.putNextEntry(new ZipEntry(file.getName()));
            // 设置压缩文件内的字符编码，不然会变成乱码
            out.setEncoding("GBK");
            int len;
            // 读入需要下载的文件的内容，打包到zip文件
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.closeEntry();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件或者文件夹-worker
     *
     * @param zOut zip输出流
     * @param file 文件或者文件夹
     * @param name 文件或者文件夹名称
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
                FileUtil.writeFile(zOut, file);
            }
            zOut.setEncoding("GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
