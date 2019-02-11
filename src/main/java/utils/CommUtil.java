package utils;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: youxingyang
 * @date: 2018/4/2 9:27
 */
public final class CommUtil {
    private CommUtil() {
    }

    /**
     * 是否是空
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        return "".equals(string) || string == null;
    }

    /**
     * 读取多个文本文件-准备插入数据库
     * @param arr
     * @return
     */
    public static String[] getArrFromTxt(String[] arr) {
        Set<String> list = new HashSet<String>(30000);
        for (int i = 0; i < arr.length; i++) {
            if (!StringUtils.isBlank(arr[i])) {
                File file = new File(arr[i]);
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                    String temp;
                    while (((temp = reader.readLine()) != null)) {
                        if (temp.startsWith("\t")) {
                            temp = temp.substring("\t".length());
                        }
                        list.add(temp);
                    }
                } catch (IOException e) {
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
            }
        }
        String[] array = new String[list.size()];
        int index = 0;
        for (String ss : list) {
            array[index] = ss;
            index++;
        }
        return array;
    }
}
