package utils;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author: youxingyang
 * @date: 2018/9/17 18:03
 */
public class CollectionUtil {
    private CollectionUtil() {
    }
    /**
     * 列出全排列组合
     * @param str       数组
     * @param st        开始index
     * @param len       数组长度
     * @param list      保存结果集合
     */
    public static void arrange(String[] str, int st, int len, List<String> list) {
        if (st == len - 1) {
            String res = "";
            for (int i = 0; i < len; i++) {
                res += str[i] + ",";
            }
            if (!"".equals(res) && res.endsWith(",")) {
                res = res.substring(0, res.length() - 1);
                list.add(res);
            }
        } else {
            for (int i = st; i < len; i++) {
                swap(str, st, i);
                arrange(str, st + 1, len, list);
                swap(str, st, i);
            }
        }
    }

    /**
     * 交换2个串
     * @param str
     * @param i
     * @param j
     */
    public static void swap(String[] str, int i, int j) {
        String temp = str[i];
        str[i] = str[j];
        str[j] = temp;
    }

    /**
     * list转成string串儿
     * {a,b} => type1=common-log,type2=advance-log
     * @param list
     * @param append
     * @param split
     * @return
     */
    public static String list2String(List<String> list, String append, String split) {
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < list.size(); i++) {
            if (StringUtils.isNotBlank(list.get(i))) {
                sb.append("\\").append(append).append(list.get(i)).append("\\").append(append);
                if (i < list.size() - 1) {
                    sb.append("\\").append(split);
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * map => string
     * XS16ZE00801=hangge1,pdf,XS16ZE00802=hangge2.pdf
     * @param sampleMap
     * @param split
     * @return
     */
    public static String map2String(Map<String, String> sampleMap, String split) {
        StringBuffer sb = new StringBuffer("");
        int index = 0;
        for (String key : sampleMap.keySet()) {
            sb.append(key).append("=").append(sampleMap.get(key));
            if (index < sampleMap.size() - 1) {
                sb.append(split);
            }
            index++;
        }
        return sb.toString();
    }

}
