/*
 * @Author: Zhe Chen
 * @Date: 2021-03-19 20:13:15
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-06 00:26:39
 * @Description: 测试类
 */
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        OmsCoreView.openNewView(new MainRunnerDefinition()).asCurrentView().setTag(OmsCoreView.MAIN_TAG);
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            OmsCoreRunner.getForCurrentView().run(in.nextLine());
        }
        in.close();
    }
}

final class MainRunnerDefinition implements IRunnerDefinition {
    private final Lazy<ICommandContainer[]> containers;

    public MainRunnerDefinition() {
        containers = new Lazy<>(() -> {
            ICommandContainer[] containers = { new OmsManager(), new PersonList(), new Menu() };
            return containers;
        });
    }

    @Override
    public ICommandContainer[] getCommandContainers() {
        return containers.getValue();
    }

    @Override
    public RunResult getEmptyCommandResult() {
        return RunResult.inputIllegal;
    }

    @Override
    public RunResult getUnknownCommandResult() {
        return RunResult.commandNotExist;
    }
}