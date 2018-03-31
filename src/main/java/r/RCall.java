package main.java.r;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @Author: youxingyang
 * @date: 2018/3/30 17:55
 */
public class RCall {
    /**
     * only in windows
     */
    public static void main(String[] args) {
        // 返回与当前 Java 应用程序相关的运行时对象
        Runtime run = Runtime.getRuntime();
        try {
            // 注意：对字符串中路径\进行转义
            String projectPath = RCall.class.getResource("/").getPath();
            System.out.println(projectPath);
            projectPath = projectPath.replace("\\\\", "/");
            System.out.println(projectPath);
            String cmds = "Rscript " + projectPath + "my_script.R tea coff eoo fff";
            //String cmds = "Rscript D:\\IDEAWORKPLACE\\practice\\src\\test.r D:\\IDEAWORKPLACE/practice/src/QAT16ANAA00018F-test5.txt cx";
            // 启动另一个进程来执行命令
            Process p = run.exec(cmds);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
