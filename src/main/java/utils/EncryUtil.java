package utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @author: youxingyang
 * @date: 2018/8/16 18:25
 */
public class EncryUtil {

    /**
     * 生成32位码
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }

    // 加密
    public static String getBase64Encode(byte[] b) {
        String s = null;
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    // 解密
    public static String getBase64Decode(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
    }
}
