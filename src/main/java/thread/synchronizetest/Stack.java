package thread.synchronizetest;

/**
 * @Author: youxingyang
 * @date: 2016/6/24 13:17
 */
public class Stack {
    private String name;
    private String[] buffer = new String[100];
    int point = -1;

    public Stack(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public synchronized int getPoint() {
        return point;
    }

    public synchronized String pop() {
        synchronized (this) {
            this.notifyAll();
            while (point == -1) {
                System.out.println(Thread.currentThread().getName() + ": wait");
                try {
                    //wait()得放到while循环里
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            String goods = buffer[point];
            buffer[point] = null;
            Thread.yield();
            point--;
            return goods;
        }

    }

    public synchronized void push(String goods) {
        point++;
        Thread.yield();
        buffer[point] = goods;
    }
}
