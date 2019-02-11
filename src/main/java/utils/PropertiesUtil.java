package utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


/**
 * 配置文件处理工具类
 */
public class PropertiesUtil {

    /**
     * 读取properties配置文件
     *
     * @param fileName properties文件名
     * @param key      配置key
     * @return 返回的对应key的value
     */
    public static String readProperties(String fileName, String key) {
        return readProperties(fileName, key, "UTF-8");
    }

    /**
     * 读取properties配置文件
     *
     * @param fileName properties文件名
     * @param key      配置key
     * @param code     编码格式
     * @return 返回的对应key的value
     */
    public static String readProperties(String fileName, String key, String code) {
        String value = "";
        try {
            Properties props = new Properties();
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), code));
            value = props.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
}
