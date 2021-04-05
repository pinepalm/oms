/*
 * @Author: Zhe Chen
 * @Date: 2021-03-21 17:02:44
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-06 00:11:06
 * @Description: Oms核心运行器
 */
import java.util.HashMap;
import java.util.Map;

/**
 * @description: Oms核心运行器
 */
public final class OmsCoreRunner {
    private static final Map<OmsCoreView, OmsCoreRunner> runners = new HashMap<>();

    /**
     * @description: 获取与当前视图关联的 OmsCoreRunner 对象
     * @param {*}
     * @return {*}
     */
    public static OmsCoreRunner getForCurrentView() {
        OmsCoreView view = OmsCoreView.getCurrentView();
        if (view == null)
            return null;

        OmsCoreRunner runner = runners.get(view);
        if (runner == null) {
            runner = new OmsCoreRunner(view);
            runners.put(view, runner);
        }

        return runner;
    }

    private final Map<String, ExactMatchCommand> exactMatchCmds;
    private final Map<String, StandardCommand> standardCmds;
    private final IRunnerDefinition runnerDefinition;

    private EventHandler<EventArgs> onViewClosed;

    private OmsCoreRunner(OmsCoreView view) {
        exactMatchCmds = new HashMap<>();
        standardCmds = new HashMap<>();

        onViewClosed = (sender, args) -> {
            OmsCoreView closedView = (OmsCoreView) sender;
            closedView.closed.removeEventHandler(onViewClosed);
            runners.remove(closedView);
        };

        runnerDefinition = view.runnerDefinition;
        
        for (ICommandContainer container : runnerDefinition.getCommandContainers()) {
            for (ICommand command : container.getCommands()) {
                if (command instanceof ExactMatchCommand) {
                    exactMatchCmds.put(command.getName(), (ExactMatchCommand) command);
                } else if (command instanceof StandardCommand) {
                    standardCmds.put(command.getName(), (StandardCommand) command);
                }
            }
        }

        view.closed.addEventHandler(onViewClosed);
    }

    /**
     * @description: 运行命令
     * @param {String} cmd
     * @return {*}
     */
    public void run(String cmd) {
        ExactMatchCommand exactMatchCmd = exactMatchCmds.get(cmd);
        RunResult result;

        if (exactMatchCmd != null) {
            result = exactMatchCmd.run();
        } else {
            String[] runtimeArgs = cmd.split("\\s+");
            if (runtimeArgs.length > 0) {
                StandardCommand standardCmd = standardCmds.get(runtimeArgs[0]);
                result = standardCmd != null ? standardCmd.run(runtimeArgs) : runnerDefinition.getUnknownCommandResult();
            } else {
                result = runnerDefinition.getEmptyCommandResult();
            }
        }

        if (result != null)
            result.print();
    }
}