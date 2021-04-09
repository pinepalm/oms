/*
 * @Author: Zhe Chen
 * @Date: 2021-03-24 20:22:43
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-09 18:46:20
 * @Description: 菜单类
 */
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @description: 菜单类
 */
public final class Menu implements ICommandContainer {
    // <editor-fold> 字符串常量
    private static final String INPUT_ILLEGAL = "%s input illegal";
    private static final String DID_INPUT_ILLEGAL = String.format(INPUT_ILLEGAL, "Did");
    private static final String NEW_NAME_INPUT_ILLEGAL = String.format(INPUT_ILLEGAL, "New name");
    private static final String NEW_DISHS_ATTRIBUTES_INPUT_ILLEGAL = String.format(INPUT_ILLEGAL,
            "New dish's attributes");

    private static final String UPDATE_DISHS_SUCCESS = "Update dish's %s success";
    private static final String UPDATE_DISHS_NAME_SUCCESS = String.format(UPDATE_DISHS_SUCCESS, "name");
    private static final String UPDATE_DISHS_PRICE_SUCCESS = String.format(UPDATE_DISHS_SUCCESS, "price");
    private static final String UPDATE_DISHS_TOTAL_SUCCESS = String.format(UPDATE_DISHS_SUCCESS, "total");

    private static final String CHANGE_DISHS_ILLEGAL = "Change dish's %s illegal";
    private static final String CHANGE_DISHS_PRICE_ILLEGAL = String.format(CHANGE_DISHS_ILLEGAL, "price");
    private static final String CHANGE_DISHS_TOTAL_ILLEGAL = String.format(CHANGE_DISHS_ILLEGAL, "total");

    private static final String NAME_REPEATED = "Name repeated";
    private static final String NEW_NAME_REPEATED = "New name repeated";
    private static final String ADD_DISH_SUCCESS = "Add dish success";
    private static final String DISH_EXISTS = "Dish exists";
    private static final String DISH_DOES_NOT_EXIST = "Dish does not exist";
    private static final String EMPTY_MENU = "Empty Menu";
    // </editor-fold>

    // <editor-fold> 打印菜单pm
    private final Lazy<StandardCommand> printMenuCommand = new Lazy<>(() -> new StandardCommand("pm", (runtimeArgs) -> {
        switch (runtimeArgs.length) {
            case 1:
                print();

                return RunResult.empty;
            case 3:
                return OmsManager.handleRunRequest(() -> {
                    Integer pageIndex = IntegerUtil.tryParse(runtimeArgs[1], null);
                    Integer pageSize = IntegerUtil.tryParse(runtimeArgs[2], null);

                    MenuViewer viewer = new MenuViewer(this, pageIndex, pageSize);
                    viewer.build().printCurrent();
                    viewer.getBindingView(true).asCurrentView();
                }, () -> RunResult.empty);
            default:
                return RunResult.paramsCountIllegal;
        }
    }));
    // </editor-fold>
    // <editor-fold> 添加菜品nd
    private final Lazy<StandardCommand> newDishCommand = new Lazy<>(() -> new StandardCommand("nd", (runtimeArgs) -> {
        if (runtimeArgs.length != 5) {
            return RunResult.paramsCountIllegal;
        }

        return OmsManager.handleRunRequest(() -> {
            String did = runtimeArgs[1];
            String name = runtimeArgs[2];
            double price = DoubleUtil.tryParse(runtimeArgs[3], -1d);
            int total = IntegerUtil.tryParse(runtimeArgs[4], -1);

            addDish(did, name, price, total);
        }, () -> new RunResult(ADD_DISH_SUCCESS));
    }));
    // </editor-fold>
    // <editor-fold> 获取菜品gd
    private final Lazy<StandardCommand> getDishCommand = new Lazy<>(() -> new StandardCommand("gd", (runtimeArgs) -> {
        if (runtimeArgs.length < 2 || !Arrays.asList("-id", "-key").contains(runtimeArgs[1])) {
            return RunResult.commandNotExist;
        }

        if (runtimeArgs.length != 3 && runtimeArgs.length != 5) {
            return RunResult.paramsCountIllegal;
        }

        switch (runtimeArgs[1]) {
            case "-id":
                return OmsManager.handleRunRequest(() -> {
                    Dish dishById = getDishById(runtimeArgs[2]);
                    if (dishById == null) {
                        throw new IllegalArgumentException(DISH_DOES_NOT_EXIST);
                    }

                    return dishById;
                }, (dishById) -> new RunResult(dishById.toString()));
            case "-key":
                switch (runtimeArgs.length) {
                    case 3:
                        Vector<Dish> list = getDishByKeyWord(runtimeArgs[2]);
                        if (list.isEmpty()) {
                            return new RunResult(DISH_DOES_NOT_EXIST);
                        }

                        int i = 1;
                        for (Dish dish : list) {
                            System.out.println(String.format("%d. %s", i++, dish));
                        }
                        break;
                    case 5:
                        return OmsManager.handleRunRequest(() -> {
                            Integer pageIndex = IntegerUtil.tryParse(runtimeArgs[3], null);
                            Integer pageSize = IntegerUtil.tryParse(runtimeArgs[4], null);

                            MenuViewer viewer = new MenuViewer(this, runtimeArgs[2], pageIndex, pageSize);
                            viewer.build().printCurrent();
                            viewer.getBindingView(true).asCurrentView();
                        }, () -> RunResult.empty);
                    default:
                        break;
                }

                break;
            default:
                break;
        }

        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 修改菜品udd
    private final Lazy<StandardCommand> updateDishCommand = new Lazy<>(
            () -> new StandardCommand("udd", (runtimeArgs) -> {
                if (runtimeArgs.length < 2 || !Arrays.asList("-n", "-t", "-p").contains(runtimeArgs[1])) {
                    return RunResult.commandNotExist;
                }

                if (runtimeArgs.length != 4) {
                    return RunResult.paramsCountIllegal;
                }

                String did = runtimeArgs[2];

                switch (runtimeArgs[1]) {
                    case "-n":
                        return OmsManager.handleRunRequest(() -> {
                            String name = runtimeArgs[3];
                            updateDish(did, name);
                        }, () -> new RunResult(UPDATE_DISHS_NAME_SUCCESS));
                    case "-t":
                        return OmsManager.handleRunRequest(() -> {
                            int total = IntegerUtil.tryParse(runtimeArgs[3], -1);
                            updateDish(did, total);
                        }, () -> new RunResult(UPDATE_DISHS_TOTAL_SUCCESS));
                    case "-p":
                        return OmsManager.handleRunRequest(() -> {
                            double price = DoubleUtil.tryParse(runtimeArgs[3], -1d);
                            updateDish(did, price);
                        }, () -> new RunResult(UPDATE_DISHS_PRICE_SUCCESS));
                    default:
                        break;
                }

                return RunResult.empty;
            }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(printMenuCommand.getValue(),
            newDishCommand.getValue(), getDishCommand.getValue(), updateDishCommand.getValue()));
    // </editor-fold>

    public static final Menu instance = new Menu();

    private final Map<String, SortedMap<String, Dish>> dishes;
    private final Map<String, SortedMap<String, Dish>> readonlyDishes;

    /**
     * @description: 默认构造
     * @param {*}
     * @return {*}
     */
    private Menu() {
        dishes = new LinkedHashMap<>();
        dishes.put("H", new TreeMap<String, Dish>());// 热菜
        dishes.put("C", new TreeMap<String, Dish>());// 凉菜
        dishes.put("O", new TreeMap<String, Dish>());// 其它

        readonlyDishes = Collections.unmodifiableMap(dishes);
    }

    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }

    /**
     * @description: 获取菜品列表的只读映射
     * @param {*}
     * @return {*}
     */
    public Map<String, SortedMap<String, Dish>> getDishes() {
        return readonlyDishes;
    }

    /**
     * @description: 通过编号取出菜品
     * @param {String} did
     * @return {*}
     */
    public Dish getDishById(String did) throws IllegalArgumentException {
        if (!Dish.checkDID(did)) {
            throw new IllegalArgumentException(DID_INPUT_ILLEGAL);
        }

        String key = did.substring(0, 1);
        SortedMap<String, Dish> map = dishes.get(key);
        if (map == null) {
            return null;
        }

        return map.get(did);
    }

    /**
     * @description: 通过名称取出菜品
     * @param {String} name
     * @return {*}
     */
    public Dish getDishByName(String name) {
        for (SortedMap<String, Dish> map : dishes.values()) {
            for (Dish dish : map.values()) {
                if (name.equals(dish.getName())) {
                    return dish;
                }
            }
        }

        return null;
    }

    /**
     * @description: 通过关键词取出菜品集合
     * @param {String} keyword
     * @return {*}
     */
    public Vector<Dish> getDishByKeyWord(String keyword) {
        Vector<Dish> res = new Vector<>();

        String lowercaseKeyword = keyword.toLowerCase();
        for (SortedMap<String, Dish> map : dishes.values()) {
            for (Dish dish : map.values()) {
                if (dish.getName().toLowerCase().contains(lowercaseKeyword)) {
                    res.add(dish);
                }
            }
        }

        return res;
    }

    /**
     * @description: 添加菜品
     * @param {String} did
     * @param {String} name
     * @param {double} price
     * @param {int}    total
     * @return {*}
     */
    public void addDish(String did, String name, double price, int total) throws IllegalArgumentException {
        Dish dishById = getDishById(did);
        if (dishById != null) {
            throw new IllegalArgumentException(DISH_EXISTS);
        }

        if (!Dish.checkName(name) || !Dish.checkPrice(price) || !Dish.checkTotal(total)) {
            throw new IllegalArgumentException(NEW_DISHS_ATTRIBUTES_INPUT_ILLEGAL);
        }

        Dish dishByName = getDishByName(name);
        if (dishByName != null) {
            throw new IllegalArgumentException(NAME_REPEATED);
        }

        String key = did.substring(0, 1);
        SortedMap<String, Dish> map = dishes.get(key);
        if (map != null) {
            map.put(did, new Dish(did, name, price, total));
        }
    }

    /**
     * @description: 修改菜品(名称)
     * @param {String} did
     * @param {String} name
     * @return {*}
     */
    public void updateDish(String did, String name) throws IllegalArgumentException {
        Dish dishById = getDishById(did);
        if (dishById == null) {
            throw new IllegalArgumentException(DISH_DOES_NOT_EXIST);
        }

        if (!Dish.checkName(name)) {
            throw new IllegalArgumentException(NEW_NAME_INPUT_ILLEGAL);
        }

        Dish dishByName = getDishByName(name);
        if (dishByName != null) {
            throw new IllegalArgumentException(NEW_NAME_REPEATED);
        }

        dishById.setName(name);
    }

    /**
     * @description: 修改菜品(价格)
     * @param {String} did
     * @param {double} price
     * @return {*}
     */
    public void updateDish(String did, double price) throws IllegalArgumentException {
        Dish dishById = getDishById(did);
        if (dishById == null) {
            throw new IllegalArgumentException(DISH_DOES_NOT_EXIST);
        }

        if (!Dish.checkPrice(price)) {
            throw new IllegalArgumentException(CHANGE_DISHS_PRICE_ILLEGAL);
        }

        dishById.setPrice(price);
    }

    /**
     * @description: 修改菜品(总量)
     * @param {String} did
     * @param {int}    total
     * @return {*}
     */
    public void updateDish(String did, int total) throws IllegalArgumentException {
        Dish dishById = getDishById(did);
        if (dishById == null) {
            throw new IllegalArgumentException(DISH_DOES_NOT_EXIST);
        }

        if (!Dish.checkTotal(total)) {
            throw new IllegalArgumentException(CHANGE_DISHS_TOTAL_ILLEGAL);
        }

        dishById.setTotal(total);
    }

    /**
     * @description: 菜单是否为空
     * @param {*}
     * @return {*}
     */
    public boolean isEmpty() {
        for (SortedMap<String, Dish> map : dishes.values()) {
            if (!map.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * @description: 打印菜单
     * @param {*}
     * @return {*}
     */
    public void print() {
        boolean hasItems = false;
        int i = 1;

        for (SortedMap<String, Dish> map : dishes.values()) {
            if (!map.isEmpty()) {
                hasItems = true;
                for (Dish dish : map.values()) {
                    System.out.println(String.format("%d. %s", i++, dish));
                }
            }
        }

        if (!hasItems) {
            System.out.println(EMPTY_MENU);
        }
    }
}
