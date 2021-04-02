/*
 * @Author: Zhe Chen
 * @Date: 2021-03-28 11:31:50
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-02 23:32:47
 * @Description: 菜单查看器
 */
import java.util.Arrays;

/**
 * @description: 菜单查看器
 */
public final class MenuViewer implements ICommandContainer {
    // <editor-fold> 字符串常量
    private static final String THIS_IS_THE_PAGE = "This is the %s page";
    private static final String THIS_IS_THE_FIRST_PAGE = String.format(THIS_IS_THE_PAGE, "first");
    private static final String THIS_IS_THE_LAST_PAGE = String.format(THIS_IS_THE_PAGE, "last");
    
    private static final String EXIT_PAGE_CHECK_MODE = "Exit page check mode";
    private static final String CALL_INNER_METHOD_ILLEGAL = "Call inner method illegal";
    // </editor-fold>

    // <editor-fold> 下一页n
    private final Lazy<ExactMatchCommand> nextCommand = new Lazy<>(() -> new ExactMatchCommand("n", () -> {
        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 上一页l
    private final Lazy<ExactMatchCommand> lastCommand = new Lazy<>(() -> new ExactMatchCommand("l", () -> {
        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 第一页f
    private final Lazy<ExactMatchCommand> firstCommand = new Lazy<>(() -> new ExactMatchCommand("f", () -> {
        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 退出q
    private final Lazy<ExactMatchCommand> quitCommand = new Lazy<>(() -> new ExactMatchCommand("q", () -> {
        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(nextCommand.getValue(),
            lastCommand.getValue(), firstCommand.getValue(), quitCommand.getValue()));
    // </editor-fold>

    private Menu menu;

    public MenuViewer() {

    }

    public MenuViewer(Menu menu) {
        setMenu(menu);
    }

    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }

    public Menu getMenu() {
        return menu;
    }
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}