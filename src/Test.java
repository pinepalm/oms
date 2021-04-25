/*
 * @Author: Zhe Chen
 * @Date: 2021-03-19 20:13:15
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 12:07:02
 * @Description: 测试类
 */
import java.util.Scanner;

import com.buaa.oms.OmsApp;

public class Test {
    public static void main(String[] args) {
        OmsApp app = OmsApp.getInstance();
        app.closed.addEventHandler((sender, e) -> System.exit(0));

        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            app.runnerHost.getRunnerForCurrentView().run(in.nextLine());
        }
        in.close();
    }
}
