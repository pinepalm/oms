/*
 * @Author: Zhe Chen
 * @Date: 2021-03-19 20:13:15
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-06 10:53:51
 * @Description: 测试类
 */
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            OmsCoreRunner.getForCurrentView().run(in.nextLine());
        }
        in.close();
    }
}
