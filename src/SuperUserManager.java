/*
 * @Author: Zhe Chen
 * @Date: 2021-04-15 00:58:25
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-15 21:56:30
 * @Description: 超级用户管理器
 */
import java.util.Arrays;

/**
 * @description: 超级用户管理器
 */
public final class SuperUserManager implements ICommandContainer {
    // <editor-fold> 字符串常量
    private static final String SUCCESS = "%s success";
    private static final String ILLEGAL = "%s illegal";

    private static final String QUIT_SUDO_MODE = "Quit sudo mode";
    // </editor-fold>

    // <editor-fold> 退出超级用户状态SQ
    private final Lazy<ExactMatchCommand> sqCommand = new Lazy<>(() -> new ExactMatchCommand("SQ", () -> {
        return OmsManager.handleRunRequest(() -> {
            close();
        }, () -> new RunResult(QUIT_SUDO_MODE));
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(sqCommand.getValue()));
    // </editor-fold>

    public SuperUserManager() {

    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }

    public void close() {
        
    }
}
