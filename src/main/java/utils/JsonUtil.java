package utils;

import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: youxingyang
 * @date: 2018/4/25 10:25
 */
public final class JsonUtil {
    private JsonUtil() {
    }

    /**
     * 读取 文件  转为 字符串
     *
     * @param jsonFile
     * @return
     */
    public static String readFile(String jsonFile) {
        String str = null;
        File file = new File(jsonFile);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            str = readStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    /**
     * 读取流  成为  字符串
     *
     * @param inputStream
     * @return
     */
    public static String readStream(InputStream inputStream) {
        String str = null;
        try {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            str = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    public static JSONObject loader(String jsonFile) {
        return JSONObject.fromObject(readFile(jsonFile));
    }
}