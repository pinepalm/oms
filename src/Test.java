/*
 * @Author: Zhe Chen
 * @Date: 2021-03-19 20:13:15
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-02 17:40:00
 * @Description: 测试类
 */
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        OmsCoreView.openNewView(new OmsManager(), new PersonList(), new Menu()).asCurrentView().setTag("Main");
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            OmsCoreRunner.getForCurrentView().run(in.nextLine());
        }
        in.close();
    }
}