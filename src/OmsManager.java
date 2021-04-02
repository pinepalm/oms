/*
 * @Author: Zhe Chen
 * @Date: 2021-03-26 19:31:06
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-02 23:32:55
 * @Description: Oms管理器
 */
import java.util.Arrays;

/**
 * @description: Oms管理器
 */
public final class OmsManager implements ICommandContainer {
    // <editor-fold> 字符串常量
    private static final String GOOD_BYE = "----- Good Bye! -----";
    // </editor-fold>

    // <editor-fold> 退出QUIT
    private final Lazy<ExactMatchCommand> quitCommand = new Lazy<>(() -> new ExactMatchCommand("QUIT", () -> {
        System.out.println(GOOD_BYE);
        System.exit(0);

        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(quitCommand.getValue()));
    // </editor-fold>

    public OmsManager() {

    }

    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }
}