package utils;

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
}
