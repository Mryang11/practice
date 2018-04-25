package utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * @Author: youxingyang
 * @date: 2018/4/25 9:58
 */
public final class PasswordUtil {
    private static final String SALT = BCrypt.gensalt();

    private PasswordUtil() {
    }

    /**
     * 加密 密码
     *
     * @param rawPassword 密码
     * @return
     */
    public static String encoder(String rawPassword) {
        return BCrypt.hashpw(rawPassword, SALT);
    }


    /**
     * 校验密码 是否正确
     *
     * @param rawPassword
     * @param passWord
     * @return
     */
    public static boolean check(String rawPassword, String passWord) {
        return BCrypt.checkpw(rawPassword, passWord);
    }
}
