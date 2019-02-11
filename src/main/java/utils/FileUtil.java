package utils;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     *
     * @param is 流
     * @return  一行一行的数据list集合
     * @Description: 读取文件的每一行
     * @author jiangpeng
     * @date 2016-4-21 上午11:42:57
     * @version V1.0
     */
    public static List<String> readFileLine(InputStream is){
        List<String> fileLineStr= new ArrayList<>();
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr =new InputStreamReader(is,"utf-8");
            br =new BufferedReader(isr);
            while (br.ready()) {
                String tempStr = br.readLine();
                if ("".equals(tempStr)) {
                    continue;
                }
                fileLineStr.add(tempStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                br.close();
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return fileLineStr;
    }


    /**
     *
     * @param filename 目标文件
     */
    public static void readInverse(String filename) {
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(filename, "r");
            long len = rf.length();
            long start = rf.getFilePointer();
            long nextend = start + len - 1;
            String line;
            rf.seek(nextend);
            int c;
            while (nextend > start) {
                c = rf.read();
                if (c == '\n' || c == '\r') {
                    line = rf.readLine();
                    if (line != null) {
                        System.out.println(new String(line.getBytes("ISO-8859-1"), "utf-8"));
                    }
                    nextend--;
                }
                nextend--;
                rf.seek(nextend);
                if (nextend == 0) {
                    System.out.println(new String(rf.readLine().getBytes("ISO-8859-1"), "utf-8"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rf != null) {
                    rf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param file 目标文件
     * @param prefixOut 目标文件前缀
     */
    public static void writeInverse(File file, String prefixOut) {
        RandomAccessFile rf = null;
        BufferedWriter bw = null;
        String fileName = file.getName();
        File out = new File(prefixOut + fileName);
        if (out.exists()) {
            System.out.println("文件已存在:" + out.getAbsolutePath());
        } else {
            try {
                rf = new RandomAccessFile(file, "r");
                createFile(prefixOut + fileName);
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out, true), "utf-8"));
                long len = rf.length();
                long start = rf.getFilePointer();
                long nextend = start + len - 1;
                String line = "";
                rf.seek(nextend);
                int c;
                while (nextend > start) {
                    c = rf.read();
                    if (c == '\n' || c == '\r') {
                        line = rf.readLine();
                        if (!StringUtils.isBlank(line)) {
                            bw.write(line.substring(0, line.indexOf(';')));
                            bw.write(" on duplicate key update genotype=values(genotype), dna_alle=values(dna_alle);");
                            bw.write("\n");
                        }
                        nextend--;
                    }
                    nextend--;
                    rf.seek(nextend);
                    if (nextend == 0) {
                        bw.write(line.substring(0, line.indexOf(';')));
                        bw.write(" on duplicate key update genotype=values(genotype), dna_alle=values(dna_alle);");
                        bw.write("\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }
                    if (rf != null) {
                        rf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断指定目录下文件大小是否变化
     * @param dir       监控的目录绝对路径
     * @param period    统计时间间隔,ms
     * @return          -1 error;   0 no change;    1 change
     */
    public static int isChange(String dir, long period) {
        return isChange(dir, period);
    }

    /**
     * 判断指定目录下文件大小是否变化
     * @param file      监控的文件
     * @param period    统计时间间隔,ms
     * @return          -1 error;   0 no change;    1 change
     */
    public static int isChange(File file, long period) throws InterruptedException {
        int res = -1;
        if (file.exists()) {
            long sizeA = getAllSize4File(file);
            if (sizeA > 0) {
                Thread.sleep(period);
                long sizeB = getAllSize4File(file);
                long result = sizeB - sizeA;
                if (result == 0) {
                    res = 0;
                } else {
                    res = 1;
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
    public static long getAllSize4File(String path) {
        return getAllSize4File(new File(path));
    }

    public static long getAllSize4File(File file) {
        long res = 0;
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    if (file1.isDirectory()) {
                        res += getAllSize4File(file1.getAbsolutePath());
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
     * 写文件
     *
     * @param zOut
     * @param file
     * @throws IOException
     */
    public static void writeFile(ZipOutputStream zOut, File file) throws IOException {
        byte[] buffer = new byte[16 * 1024];
        FileInputStream in = new FileInputStream(file);
        int len;
        while ((len = in.read(buffer)) > 0) {
            zOut.write(buffer, 0, len);
            zOut.flush();
        }
        in.close();
    }

    public static void main(String[] args) {
        /*String fileName = "D:/awk/parameter/2018-10-30-dd774fd831ef4f07afe0f07e40e73773.txt";
        Map<String, String> sampleMap = new HashMap<>();
        String path = "D:/awk/pdf/nuo300/2018-10-30-00cf7320423b48179bda7129e44b8209" + File.separator + "5dcb4af3475b485aa9b3833f521d3838" + File.separator + "SS-NK300" + File.separator + "7050-2401-2257-SS-NK300.pdf";
        System.out.println(path);
        path = path.replace("\\", "/");
        System.out.println(path);
        sampleMap.put("7050-2401-2257", path);

        writeMapFile(sampleMap, fileName);*/

        String[] array = {"E:/aaaa.txt"};
        String[] list = CommUtil.getArrFromTxt(array);
        Map<String, String> map = new HashMap<>();
        for (String string : list) {
            String[] arr = string.split(" ");
            String value = arr[3] + arr[5];
            if (map.containsKey(arr[1])) {
                if (!map.get(arr[1]).contains(arr[3])) {
                    value += "," + map.get(arr[1]);
                }
            }
            map.put(arr[1], value);
        }

        System.out.println(map.size());
        map.forEach((x,y) -> System.out.println(x + ": " + y));

    }

    /**
     * map写进文件里
     * // a = {'a': 'hangge', 'b': 'man', 'school': 'wust'}
     * @param sampleMap
     * @param paraFileName
     * @return
     */
    public static boolean writeMapFile(Map<String, String> sampleMap, String paraFileName) {
        boolean res = false;
        BufferedWriter bw = null;
        try {
            File file = new File(paraFileName);
            if (!file.exists()) {
                FileUtil.createFile(paraFileName);
            }
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(paraFileName), "utf-8"));
            if (sampleMap.size() > 0) {
                bw.write('{');
                int index = 0;
                for (String key : sampleMap.keySet()) {
                    bw.write('\'');
                    bw.write(key);
                    bw.write('\'');
                    bw.write(':');
                    bw.write('\'');
                    bw.write(sampleMap.get(key));
                    bw.write('\'');
                    if (index < sampleMap.size() - 1) {
                        bw.write(',');
                    }
                    index++;
                }
                bw.write('}');
                res = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }try {
            if (bw != null) {
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}
