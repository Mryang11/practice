package utils;

/**
 * @author: youxingyang
 * @date: 2018/8/30 9:57
 */
public class OsUtil {
    private OsUtil() {}

    /**
     * 判断是否是windows
     * @return
     */
    public static boolean isWindows() {
        String os = System.getProperties().getProperty("os.name");
        return (os.startsWith("win") || os.startsWith("Win"));
    }
}
