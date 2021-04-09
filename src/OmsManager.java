/*
 * @Author: Zhe Chen
 * @Date: 2021-03-26 19:31:06
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-09 11:43:58
 * @Description: Oms管理器
 */
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @description: Oms管理器
 */
public final class OmsManager implements ICommandContainer {
    /**
     * @description: 处理运行请求(无参)
     * @param {Runnable} action
     * @param {Supplier<RunResult>} success
     * @return {*}
     */
    public static RunResult handleRunRequest(Runnable action, Supplier<RunResult> success) {
        try {
            action.run();
        } catch (Exception ex) {
            return new RunResult(ex.getMessage());
        }

        return success.get();
    }

    /**
     * @description: 处理运行请求(单参)
     * @param {Supplier<T>function,Function<T,RunResult>} success
     * @return {*}
     */
    public static <T> RunResult handleRunRequest(Supplier<T> function, Function<T, RunResult> success) {
        T res;

        try {
            res = function.get();
        } catch (Exception ex) {
            return new RunResult(ex.getMessage());
        }

        return success.apply(res);
    }

    // <editor-fold> 字符串常量
    private static final String GOOD_BYE = "----- Good Bye! -----";
    // </editor-fold>

    // <editor-fold> 退出QUIT
    private final Lazy<ExactMatchCommand> quitCommand = new Lazy<>(() -> new ExactMatchCommand("QUIT", () -> {
        System.out.println(GOOD_BYE);
        OmsCoreView.mainView.close();

        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(quitCommand.getValue()));
    // </editor-fold>

    public static final OmsManager instance = new OmsManager();

    private OmsManager() {

    }

    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }
}