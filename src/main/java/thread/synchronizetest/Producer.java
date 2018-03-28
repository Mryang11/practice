package thread.synchronizetest;

/**
 * Created by youxingyang on 2016/6/24.
 */
public class Producer extends Thread {
    private Stack theStack;

    public Producer(Stack s, String name) {
        super(name);
        theStack = s;
        start();    //启动自身生产者线程
    }
    @Override
    public void run() {
        String goods;
        for (int i = 0; i < 200; i++) {
            synchronized (this) {
                goods = "goods" + (theStack.getPoint() + 1);
                theStack.push(goods);
                System.out.println(getName() + ": push " + goods + " to " + theStack.getName());
                yield();
            }
        }
    }
}
