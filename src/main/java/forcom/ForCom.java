package main.java.forcom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author youxingyang
 * @date 2018/4/118:26
 */
public class ForCom {

    /**
     * @param list
     */
    public static void print(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    /**
     * @param list
     */
    public static void print1(List<String> list) {
        for (String s : list) {
            System.out.println(s);
        }
    }

    /**
     * @param list
     */
    public static void print2(List<String> list) {
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    /**
     * @param num
     * @param isLinkedList
     * @param list
     * @return
     */
    public static List<String> create(long num, boolean isLinkedList, List<String> list) {
        if (isLinkedList) {
            list = new LinkedList<>();
        } else {
            list = new ArrayList<>();
        }
        for (int i = 0; i < num; i++) {
            list.add(i + "");
        }
        return list;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        int num = 500;
        //遍历LinkedList比较
        boolean isLinkedList = true;
        //遍历ArrayList比较
        //boolean isLinkedList = false;
        String cate;
        List<String> list = null;
        if (isLinkedList) {
            list = create(num, isLinkedList, list);
            cate = "LinkedList";
        } else {
            list = create(num, isLinkedList, list);
            cate = "ArrayList";
        }
        ExecutorService executor = Executors.newFixedThreadPool(3);
        ForTask forTask = new ForTask(list, cate, "for");
        ForTask forTask1 = new ForTask(list, cate, "foreach");
        ForTask forTask2 = new ForTask(list, cate, "iterator");

        executor.submit(forTask);
        executor.submit(forTask1);
        executor.submit(forTask2);

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static class ForTask implements Callable<String> {
        private List<String> list;
        private String type;
        private String cate;

        ForTask(List<String> list, String cate, String type) {
            this.list = list;
            this.cate = cate;
            this.type = type;
        }

        @Override
        public String call() {
            long aaStart = System.currentTimeMillis();
            if ("for".equals(type)) {
                print(list);
            } else if ("foreach".equals(type)) {
                print1(list);
            } else if ("iterator".equals(type)) {
                print2(list);
            } else {
                System.out.println("not matched");
            }
            long bbStart = System.currentTimeMillis();
            String res = cate + " " + type + " -> 耗时：" + (bbStart - aaStart) + "ms";
            System.out.println(res);
            return res;
        }
    }
}
