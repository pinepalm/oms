/*
 * @Author: Zhe Chen
 * @Date: 2021-03-26 19:31:06
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-15 00:40:17
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
     * @param {Runnable}            action
     * @param {Supplier<RunResult>} success
     * @return {*} 若有异常, 则返回包含异常信息的运行结果
     */
    public static RunResult handleRunRequest(Runnable action, Supplier<RunResult> success) {
        return handleRunRequest(action, success, (ex) -> new RunResult(ex.getMessage()));
    }

    /**
     * @description: 处理运行请求(单参)
     * @param {Supplier<T>function,Function<T,RunResult>} success
     * @return {*} 若有异常, 则返回包含异常信息的运行结果
     */
    public static <T> RunResult handleRunRequest(Supplier<T> function, Function<T, RunResult> success) {
        return handleRunRequest(function, success, (ex) -> new RunResult(ex.getMessage()));
    }

    /**
     * @description: 处理运行请求(无参)
     * @param {Runnable}            action
     * @param {Supplier<RunResult>} success
     * @return {*}
     */
    public static RunResult handleRunRequest(Runnable action, Supplier<RunResult> success,
            Function<Exception, RunResult> fail) {
        try {
            action.run();
        } catch (Exception ex) {
            return fail.apply(ex);
        }

        return success.get();
    }

    /**
     * @description: 处理运行请求(单参)
     * @param {Supplier<T>function,Function<T,RunResult>success,Function<Exception,RunResult>} fail
     * @return {*}
     */
    public static <T> RunResult handleRunRequest(Supplier<T> function, Function<T, RunResult> success,
            Function<Exception, RunResult> fail) {
        T res;

        try {
            res = function.get();
        } catch (Exception ex) {
            return fail.apply(ex);
        }

        return success.apply(res);
    }

    // <editor-fold> 字符串常量
    private static final String GOOD_BYE = "----- Good Bye! -----";
    // </editor-fold>

    // <editor-fold> 退出QUIT
    private final Lazy<ExactMatchCommand> quitCommand = new Lazy<>(() -> new ExactMatchCommand("QUIT", () -> {
        return handleRunRequest(() -> {
            System.out.println(GOOD_BYE);
            OmsCoreView.mainView.close();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 进入超级用户模式SUDO
    private final Lazy<ExactMatchCommand> sudoCommand = new Lazy<>(() -> new ExactMatchCommand("SUDO", () -> {

        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 登录login
    private final Lazy<StandardCommand> loginCommand = new Lazy<>(() -> new StandardCommand("login", (runtimeArgs) -> {
        if (runtimeArgs.length < 2 || !Arrays.asList("-i", "-n").contains(runtimeArgs[1])) {
            return RunResult.commandNotExist;
        }

        if (runtimeArgs.length != 4) {
            return RunResult.paramsCountIllegal;
        }

        switch (runtimeArgs[1]) {
            case "-i":
                
                break;
            case "-n":
                
                break;
            default:
                break;
        }

        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(
            () -> Arrays.asList(quitCommand.getValue(), sudoCommand.getValue(), loginCommand.getValue()));
    // </editor-fold>

    public static final OmsManager instance = new OmsManager();

    private OmsManager() {

    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }
}