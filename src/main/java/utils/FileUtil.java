package utils;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @Author: youxingyang
 * @date: 2018/3/30 14:46
 */
public final class FileUtil {
    private FileUtil() {
    }

    private static final int BUFFER_SIZE = 16 * 1024;
    private static final int MD5_RABIX = 16;

    /**
     * 统计文件夹下的文件总数
     *
     * @param path 文件的绝对路径
     * @return
     */
    public static long getNumFromFile(String path) {
        long res = 0;
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    if (file1.isDirectory()) {
                        res += getNumFromFile(file1.getAbsolutePath());
                    } else {
                        res++;
                    }
                }
            } else {
                if (file.isFile()) {
                    res++;
                }
            }
        }
        return res;
    }

    /**
     * 获取文件夹下的所有文件的大小
     *
     * @param path 文件夹绝对路径
     * @return
     */
    public static long getAllSizeFromFile(String path) {
        long res = 0;
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    if (file1.isDirectory()) {
                        res += getAllSizeFromFile(file1.getAbsolutePath());
                    } else {
                        res += file1.length();
                    }
                }
            } else {
                if (file.isFile()) {
                    res = file.length();
                }
            }
        }
        return res;
    }

    /**
     * 以适当方式显示文件大小.
     *
     * @param size 单位字节
     * @return
     */
    public static String getFormatSize(long size) {
        long kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte(s)";
        }

        long megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        long gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        long teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 创建一个目录
     *
     * @param filePath
     * @return
     */
    public static boolean createDir(String filePath) {
        boolean res = false;
        File file = new File(filePath);
        // 判断目录是否存在
        if (file.isDirectory()) {
            System.out.println("目标目录已存在" + filePath);
            res = true;
        }
        // 判断文件是否为目录
        if (filePath.endsWith(File.separator)) {
            System.out.println("目标文件不能为目录！");
            res = true;
        }
        // 判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            // 如果目标文件所在的文件夹不存在，则创建父文件夹
            System.out.println("目标文件所在目录不存在，准备创建它！");
            // 判断创建目录是否成功
            if (file.getParentFile().mkdirs()) {
                System.out.println("创建目标文件所在的目录失败！");
            } else {
                res = true;
            }
        } else {
            res = true;
        }
        return res;
    }

    /**
     * 创建一个文件
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean createFile(String filePath) {
        boolean res = false;
        File file = new File(filePath);
        // 判断文件是否存在
        if (file.exists()) {
            System.out.println("目标文件已存在" + filePath);
        }
        // 判断文件是否为目录
        if (filePath.endsWith(File.separator)) {
            System.out.println("目标文件不能为目录！");
        }
        // 判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            // 如果目标文件所在的文件夹不存在，则创建父文件夹
            System.out.println("目标文件所在目录不存在，准备创建它！");
            // 判断创建目录是否成功
            if (!file.getParentFile().mkdirs()) {
                System.out.println("创建目标文件所在的目录失败！");
            }
        }
        try {
            // 创建目标文件
            if (file.createNewFile()) {
                System.out.println("创建文件成功:" + filePath);
                res = true;
            } else {
                System.out.println("创建文件失败！");
            }
            // 捕获异常
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建文件失败！" + e.getMessage());
        }
        return res;
    }

    /**
     * 判断该文件夹下至少有一个文件夹
     *
     * @param files 多个文件
     * @return
     */
    public static boolean containDir(File[] files) {
        boolean res = false;
        for (File file : files) {
            if (file.isDirectory()) {
                res = true;
                break;
            }
        }
        return res;
    }

    /**
     * 获得md5
     *
     * @param file 文件
     * @return
     * @throws FileNotFoundException
     */
    public static String getFileMd5(File file) throws FileNotFoundException {
        FileInputStream in = new FileInputStream(file);
        return md5(in);
    }

    /**
     * 获得md5
     *
     * @param fileName 文件名
     * @return
     * @throws FileNotFoundException
     */
    public static String getFileMd5(String fileName) throws FileNotFoundException {
        return getFileMd5(new File(fileName));
    }

    /**
     * 获得md5
     *
     * @param inputStream 流
     * @return
     */
    public static String md5(InputStream inputStream) {
        String value = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] byteBuffer = new byte[BUFFER_SIZE];
            int readLength = inputStream.read(byteBuffer);
            while (readLength != -1) {
                md5.update(byteBuffer, 0, readLength);
                readLength = inputStream.read(byteBuffer);
            }
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(MD5_RABIX);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;

    }
}
