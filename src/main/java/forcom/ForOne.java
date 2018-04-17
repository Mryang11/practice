package forcom;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author youxingyang
 * @date 2018/4/118:26
 */
public class ForOne {
    @Test
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }


}
