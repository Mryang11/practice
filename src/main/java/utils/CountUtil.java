package main.java.utils;

import java.io.*;

/**
 * @Author: youxingyang
 * @date: 2018/4/2 16:56
 */
public final class CountUtil {
    private CountUtil() { }

    // 代码行
    private static long normalLines = 0;
    // 注释行
    private static long commentLines = 0;
    // 空行
    private static long whiteLines = 0;

    /**
     * 查找出一个目录下所有的.java文件
     * @param file  要查找的目录
     */
    public static void treeFile(File file) throws IOException {

        File[] childs = file.listFiles();
        //int count = 0;
        //int sum = 0;
        if (childs != null) {
            for (File child : childs) {
                // System.out.println(preStr + childs[i].getName());
                if (!child.isDirectory()) {
                    if (child.getName().matches(".*.java$")) {
                        System.out.println(child.getName());
                        //count ++;
                        sumCode(child);
                    }
                } else {
                    treeFile(child);
                    //sum += count;
                }
            }
        }
    }

    /**
     * 计算一个.java文件中的代码行，空行，注释行
     * @param file 要计算的.java文件
     */
    public static void sumCode(File file) throws IOException {
        BufferedReader br = null;
        boolean comment = false;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.matches("^[//s&&[^//n]]*$")) {
                        whiteLines++;
                    } else if (line.startsWith("/*") && !line.endsWith("*/")) {
                        commentLines++;
                        comment = true;
                    } else if (comment) {
                        commentLines++;
                        if (line.endsWith("*/")) {
                            comment = false;
                        }
                    } else if (line.startsWith("//")) {
                        commentLines++;
                    } else {
                        normalLines++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 统计一个字符串在另一个字符串中出现次数(不覆盖查找)
     * @param str1  另一个字符串
     * @param str2  一个字符串
     * @return
     */
    public static int countInString(String str1, String str2) {
        int total = 0;
        for (String tmp = str1; tmp != null && tmp.length() >= str2.length();) {
            if (tmp.indexOf(str2) == 0) {
                total++;
                tmp = tmp.substring(str2.length());
            } else {
                tmp = tmp.substring(1);
            }
        }
        return total;
    }

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        /**
         * 统计.java文件里的实际代码行数
         */
        //D:\MyEclipse10Workspaces\report\src\com\annoroad\tumorindiv
        //D:\MyEclipse10Workspaces\report\src\com\business\controller\tumormedicate
        //D:\MyEclipse10Workspaces\report\src\com\business\entity\tumormedicate
        //D:\MyEclipse10Workspaces\report\src\com\business\mybatis\mapper\tumormedicate
        //D:\MyEclipse10Workspaces\report\src\com\business\mybatis\service\tumormedicate
        //D:\MyEclipse10Workspaces\report\src\com\business\mybatis\service\impl\tumormedicate
        //D:\\MyEclipse10Workspaces\\report\\src\\com\\business\\util\\Scanner.java
        //D:\MyEclipse10Workspaces\report\src\com\business\view
        File file = new File("E:\\aa\\bbbbb");
        System.out.println(file.getName());
        treeFile(file);

        System.out.println("空行：" + whiteLines);
        System.out.println("注释行：" + commentLines);
        System.out.println("代码行：" + normalLines);
    }
}
