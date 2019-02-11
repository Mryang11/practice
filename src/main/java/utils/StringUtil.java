package utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.*;

/**
 * @author: youxingyang
 * @date: 2018/8/27 16:11
 */
public final class StringUtil {
    private StringUtil() {
    }

    /**
     * rs11556218,rs9510787 => 'rs11556218','rs9510787'
     * @param src       rs11556218,rs9510787
     * @param split     ,
     * @param sign      '
     * @return
     */
    public static String addSign(String src, String split, String sign) {
        StringBuffer res = new StringBuffer("");
        if (StringUtils.isNotBlank(src) && StringUtils.isNotBlank(split) && StringUtils.isNotBlank(sign)) {
            String[] arr = src.split(split);
            for (int i = 0; i < arr.length; i++) {
                if (StringUtils.isNotBlank(arr[i])) {
                    res.append(sign).append(arr[i]).append(sign);
                    if (i < arr.length - 1) {
                        res.append(split);
                    }
                }
            }
        }
        return res.toString();
    }

    /**
     * rs11556218,rs9510787
     * @param src
     * @param split
     * @return
     */
    public static Set<String> str2Set(String src, String split) {
        Set<String> res = new HashSet<>();
        if (StringUtils.isNotBlank(src) && StringUtils.isNotBlank(split)) {
            String[] arr = src.split(split);
            for (String anArr : arr) {
                if (StringUtils.isNotBlank(anArr)) {
                    res.add(anArr);
                }
            }
        }
        return res;
    }

    /**
     * 2个集合之差
     * @param list1
     * @param list2
     * @return
     */
    public static Set<String> subList(Set<String> list1, Set<String> list2) {
        Set<String> res = new HashSet<>();
        if (list1.size() > 0 && list2.size() > 0) {
            for (String string : list2) {
                if (!list1.contains(string)) {
                    res.add(string);
                }
            }
        }
        return res;
    }

    /**
     * string去重
     * @param string
     * @return
     */
    public static String deDup(String string) {
        return deDup(string, ",");
    }

    /**
     * string去重
     * @param string
     * @param split
     * @return
     */
    public static String deDup(String string, String split) {
        Set<String> set = new LinkedHashSet<>();
        if (StringUtils.isNotBlank(split) && StringUtils.isNotBlank(string)) {
            String[] arr = string.split(split);
            set.addAll(Arrays.asList(arr));
            System.out.println(set.size());
        }
        return set2String(set, split);
    }

    public static String set2String(Set<String> set, String split) {
        StringBuffer sb = new StringBuffer("");
        for (String ss : set) {
            sb.append(ss).append(split);
        }
        String res = sb.toString();
        if (res.endsWith(split)) {
            res = res.substring(0, res.length() - split.length());
        }
        return res;
    }

    public static String set2String(Set<String> set) {
        return set2String(set, ",");
    }

    /**
     * 从字符串找到第一个大写字母的下标
     * @param string
     * @return
     */
    public static int getPos4String(String string) {
        int pos = -1;
        char[] chars = string.toCharArray();
        String ss;
        for (int i = 0; i < chars.length; i++) {
            ss = chars[i] + "";
            if (ss.matches("[A-Z]")) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    public static void main(String[] args) {
        //System.out.println(addSign("", ",", "\""));
        /*String string = "";
        System.out.println(string.split(",").length);
        System.out.println(string);
        System.out.println(deDup(string).split(",").length);
        System.out.println(deDup(string));

        int i = 0;
        for (; i < 100; ++i) {
            System.out.println(i);
        }*/

        System.out.println("rs789798UA".matches("^(rs)([0-9]+)([A-Z]+)$"));
        System.out.println("rs78979G".matches("^(rs)([0-9]+)([A-Z]+)$"));

        File file = new File("E:\\XS16ZE01107.pdf_tet");
        System.out.println(file.getName());

    }
}
