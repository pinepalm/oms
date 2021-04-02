/*
 * @Author: Zhe Chen
 * @Date: 2021-03-24 23:38:48
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-02 23:31:08
 * @Description: 完全匹配命令
 */
import java.util.function.Supplier;

/**
 * @description: 完全匹配命令
 * @param {*}
 * @return {*}
 */
public class ExactMatchCommand extends CommandBase {
    private final Supplier<RunResult> runner;

    /**
     * @description: 指定参数构造
     * @param {String}              name
     * @param {Supplier<RunResult>} runner
     * @return {*}
     */
    public ExactMatchCommand(String name, Supplier<RunResult> runner) {
        super(name);

        this.runner = runner;
    }

    /**
     * @description: 运行
     * @param {*}
     * @return {*}
     */
    public RunResult run() {
        if (runner == null)
            return RunResult.empty;

        return runner.get();
    }
}