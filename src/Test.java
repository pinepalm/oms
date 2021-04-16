/*
 * @Author: Zhe Chen
 * @Date: 2021-03-19 20:13:15
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-16 10:58:01
 * @Description: 测试类
 */
import java.util.Scanner;

import com.buaa.appmodel.cli.CliAppRunner;
import com.buaa.oms.OmsApp;

public class Test {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            CliAppRunner.getForCurrentView(OmsApp.instance).run(in.nextLine());
        }
        in.close();
    }
}
