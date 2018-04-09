package main.java.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: youxingyang
 * @date: 2016/6/24 13:21
 */
public class TestCallable {

    private static class Worker implements Callable<String> {
        private int i;

        public Worker(int i) {
            this.i = i;
        }

        @Override
        public String call() throws Exception {
            return "*******" + i + "*******";
        }
    }

    public static void main(String[] args) {
        try {
            List<Future<String>> list = new ArrayList<Future<String>>();
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 50; i++) {
                list.add(executorService.submit(new Worker(i)));
            }
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.DAYS);

            //获取返回的值
            List<String> values = new ArrayList<String>();
            for (Future<String> aList : list) {
                //判断任务是否完成
                if (aList.isDone()) {
                    values.add(aList.get());
                }
            }
            for (String s : values) {
                System.out.println(s);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
