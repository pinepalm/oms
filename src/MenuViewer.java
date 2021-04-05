/*
 * @Author: Zhe Chen
 * @Date: 2021-03-28 11:31:50
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-06 00:54:58
 * @Description: 菜单查看器
 */
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * @description: 菜单查看器
 */
public final class MenuViewer implements ICommandContainer {
    // <editor-fold> 字符串常量
    private static final String THIS_IS_THE_PAGE = "This is the %s page";
    private static final String THIS_IS_THE_FIRST_PAGE = String.format(THIS_IS_THE_PAGE, "first");
    private static final String THIS_IS_THE_LAST_PAGE = String.format(THIS_IS_THE_PAGE, "last");

    private static final String PAGE = "Page: %d";
    private static final String EXIT_PAGE_CHECK_MODE = "Exit page check mode";
    private static final String MENU_IS_EMPTY_EXIT_PAGE_CHECK_MODE = "Menu is empty, exit page check mode";
    private static final String NEXT_LAST_FIRST_QUIT = "n-next page,l-last page,f-first page,q-quit";
    // </editor-fold>

    // <editor-fold> 下一页n
    private final Lazy<ExactMatchCommand> nextCommand = new Lazy<>(() -> new ExactMatchCommand("n", () -> {
        next();

        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 上一页l
    private final Lazy<ExactMatchCommand> lastCommand = new Lazy<>(() -> new ExactMatchCommand("l", () -> {
        last();

        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 第一页f
    private final Lazy<ExactMatchCommand> firstCommand = new Lazy<>(() -> new ExactMatchCommand("f", () -> {
        first();

        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 退出q
    private final Lazy<ExactMatchCommand> quitCommand = new Lazy<>(() -> new ExactMatchCommand("q", () -> {
        close();

        return new RunResult(EXIT_PAGE_CHECK_MODE);
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(nextCommand.getValue(),
            lastCommand.getValue(), firstCommand.getValue(), quitCommand.getValue()));
    // </editor-fold>

    private final Menu menu;
    private final String keyword;
    private final int pageIndex;
    private final int pageSize;

    private OmsCoreView bindingView;
    private Vector<Dish> dishList;
    private int pagesCount;
    private int current;

    public MenuViewer(Menu menu, int pageIndex, int pageSize) {
        this(menu, null, pageIndex, pageSize);
    }

    public MenuViewer(Menu menu, String keyword, int pageIndex, int pageSize) {
        this.menu = menu;
        this.keyword = keyword;
        this.pageIndex = Math.max(pageIndex, 1);
        this.pageSize = Math.max(pageSize, 1);
    }

    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }

    /**
     * @description: 获取绑定的视图
     * @param {boolean} createIfNotExist
     * @return {*}
     */
    public OmsCoreView getBindingView(boolean createIfNotExist) {
        if (bindingView != null)
            return bindingView;

        if (createIfNotExist) {
            bindingView = OmsCoreView.openNewView(new MenuViewerRunnerDefinition(this));
        }

        return bindingView;
    }

    /**
     * @description: 构建
     * @param {boolean} quitIfEmpty
     * @return {*}
     */
    public boolean build(boolean quitIfEmpty) {
        dishList = menu.getDishByKeyWord(keyword != null ? keyword : "");
        pagesCount = (int) Math.ceil((double) dishList.size() / pageSize);
        current = Math.min(pageIndex, pagesCount);

        if (dishList.isEmpty()) {
            if (quitIfEmpty) {
                close();
                System.out.println(MENU_IS_EMPTY_EXIT_PAGE_CHECK_MODE);
            }

            return false;
        }

        return true;
    }

    public void printCurrent() {
        if (dishList == null)
            return;

        int i = 1;
        int index = current;
        List<Dish> list = dishList.stream().skip((index - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        System.out.println(String.format(PAGE, index));
        for (Dish dish : list) {
            System.out.println(String.format("%d. %s", i++, dish));
        }
        System.out.println(NEXT_LAST_FIRST_QUIT);
    }

    public void next() {
        int index = current + 1;
        if (index <= pagesCount) {
            current = index;
            printCurrent();
        } else {
            System.out.println(THIS_IS_THE_LAST_PAGE);
        }
    }

    public void last() {
        int index = current - 1;
        if (index >= 1) {
            current = index;
            printCurrent();
        } else {
            System.out.println(THIS_IS_THE_FIRST_PAGE);
        }
    }

    public void first() {
        current = 1;
        printCurrent();
    }

    public void close() {
        dishList = null;
        OmsCoreView bindingView = getBindingView(false);
        if (bindingView != null) {
            bindingView.close();
            OmsCoreView.getView((view) -> OmsCoreView.MAIN_TAG.equals(view.getTag())).asCurrentView();
        }
    }
}

final class MenuViewerRunnerDefinition implements IRunnerDefinition {
    private final Lazy<ICommandContainer[]> containers;

    public MenuViewerRunnerDefinition(MenuViewer viewer) {
        containers = new Lazy<>(() -> {
            ICommandContainer[] containers = { viewer };
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
        return RunResult.callInnerMethodIllegal;
    }
}
