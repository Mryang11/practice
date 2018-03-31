package main.java.utils;

import java.io.*;
import java.util.regex.Pattern;

/**
 * @Author: youxingyang
 * @date: 2018/3/30 14:50
 */
public class GenCodeUtil {
    private GenCodeUtil() {}
    private static Pattern blank = Pattern.compile("^[//s&&[^//n]]*$");
    public static void main(String[] args) {
        transferFile("C:\\Users\\domainclient\\Desktop\\src.txt", "C:\\Users\\domainclient\\Desktop\\des.txt");
    }

    /**
     *      <result column="serial_number" property="serialNumber"/>
     ->     a.serial_number
     * @param src       xml里定义的集合resultMap
     * @param start     截取字符头   保证唯一
     * @param end       截取字符尾   保证唯一
     * @param prefix    结果前缀
     * @return          a.serial_number
     */
    public static String formatSelectContent(String src, String start, String end, String prefix) {
        if (isEmpty(src) || isEmpty(start) || isEmpty(end) || prefix == null || !src.contains(start) || !src.contains(end)) {
            return "";
        }
        return prefix + src.substring(src.indexOf(start) + start.length(), src.lastIndexOf(end));
    }

    /**
     * 转换show -> info
     * @param src
     * @param des
     */
    public static void transferFile(String src, String des) {
        File file = new File(src);
        if (!file.exists()) {
            return;
        }
        StringBuilder sb = new StringBuilder("");
        //read and transfer
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line;
            while (reader.ready()) {
                line = reader.readLine();
                if (!blank.matcher(line).find()) {
                    String newLine = formatSelectContent(line, "column=\"", "\" ", "a.");
                    if (!"".equals(newLine)) {
                        newLine += ",";
                        sb.append(newLine).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        File desFile = new File(des);
        if (desFile.exists()) {
            if (desFile.delete()) {
                System.out.println(desFile.getAbsolutePath() + " is deleted");
            }
        }
        if (FileUtil.createFile(des)) {
            //write
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(desFile), "utf-8"));
                bw.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 是否是空
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        return "".equals(string) || string == null;
    }
}
