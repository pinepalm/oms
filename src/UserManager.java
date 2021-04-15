/*
 * @Author: Zhe Chen
 * @Date: 2021-04-14 23:38:55
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-15 00:46:29
 * @Description: 用户管理器
 */
import java.util.Arrays;

/**
 * @description: 用户管理器
 */
public final class UserManager implements ICommandContainer {
    /**
     * @description: 登录
     * @param {String} user
     * @param {String} password
     * @param {LoginMode} mode
     * @return {*}
     */
    public static UserManager login(String user, String password, LoginMode mode) {
        switch (mode) {
            case ID:
                
                break;
            case NAME:

                break;
            default:
                break;
        }

        return new UserManager();
    }

    // <editor-fold> 字符串常量
    private static final String SUCCESS = "%s success";
    private static final String LOGOUT_SUCCESS = String.format(SUCCESS, "Logout");
    private static final String CHANGE_PASSWORD_SUCCESS = String.format(SUCCESS, "Change password");

    private static final String NOT_MATCH = "Not match";
    private static final String NEW_PASSWORD_ILLEGAL = "New password illegal";
    // </editor-fold>

    // <editor-fold> 修改密码chgpw
    private final Lazy<StandardCommand> chgpwCommand = new Lazy<>(() -> new StandardCommand("chgpw", (runtimeArgs) -> {
        if (runtimeArgs.length != 3) {
            return RunResult.paramsCountIllegal;
        }

        return OmsManager.handleRunRequest(() -> {
            changePassword(runtimeArgs[1], runtimeArgs[2]);
        }, () -> new RunResult(CHANGE_PASSWORD_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 我的信息myinfo
    private final Lazy<ExactMatchCommand> myinfoCommand = new Lazy<>(() -> new ExactMatchCommand("myinfo", () -> {
        return OmsManager.handleRunRequest(() -> {
            printInfo();
        }, () -> RunResult.empty);
    }));
    // </editor-fold>
    // <editor-fold> 退出登录back
    private final Lazy<ExactMatchCommand> backCommand = new Lazy<>(() -> new ExactMatchCommand("back", () -> {
        return OmsManager.handleRunRequest(() -> {
            logout();
        }, () -> new RunResult(LOGOUT_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(
            () -> Arrays.asList(chgpwCommand.getValue(), myinfoCommand.getValue(), backCommand.getValue()));
    // </editor-fold>
    
    private UserManager() {

    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }

    public void changePassword(String password, String confirm) {

    }
    
    public void printInfo() {

    }
    
    public void logout() {

    }
}
