package utils;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;

/**
 * @Author: youxingyang
 * @date: 2018/6/15 12:54
 */
public final class ConvertUtil {
    private ConvertUtil() {
    }

    private static final String COMMA = ",";
    private static final String SINGLEQUOTE = "'";

    /**
     *      XS16JA00564
     *      XS16JA00572
     *      ->
     *      'XS16JA00564','XS16JA00572'
     * @param filePath
     * @return  string
     */
    public static String txt2SingleQuoteSQL(String filePath) {
        String res;
        String[] sampleArr = getArrFromTxt(filePath);
        System.out.println(sampleArr.length);
        StringBuilder sb = new StringBuilder("");
        for (String aSampleArr : sampleArr) {
            sb.append(SINGLEQUOTE).append(aSampleArr.trim()).append(SINGLEQUOTE).append(COMMA);
        }
        String strings = sb.toString();
        if (strings.endsWith(COMMA)) {
            strings = strings.substring(0, strings.length() - 1);
        }
        res = strings;
        return res;
    }

    /**
     *
     * @param filePath
     * @return
     */
    public static String[] getArrFromTxt(String filePath) {
        List<String> list = new ArrayList<>();
        if (!StringUtils.isBlank(filePath)) {
            File file = new File(filePath);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String temp;
                while (!StringUtils.isBlank(temp = reader.readLine())) {
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

        String[] arr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static void main(String[] args) {
        String src = txt2SingleQuoteSQL("C:\\Users\\domainclient\\Desktop\\测试的样本号(1).txt");
        System.out.println(src);
        System.out.println(StringUtil.deDup(src));

        String[] sampleArr = getArrFromTxt("C:\\Users\\domainclient\\Desktop\\测试的样本号(1).txt");
        String[] sampleArr1 = getArrFromTxt("C:\\Users\\domainclient\\Desktop\\测试的样本号(2).txt");
        Set<String> aaaa = new HashSet<>(10000);
        Set<String> bbbb = new HashSet<>(10000);
        aaaa.addAll(Arrays.asList(sampleArr));
        bbbb.addAll(Arrays.asList(sampleArr1));

        aaaa.removeAll(bbbb);
        aaaa.forEach(System.out::print);

    }

}
