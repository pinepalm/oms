/*
 * @Author: Zhe Chen
 * @Date: 2021-03-24 20:22:43
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-02 23:23:08
 * @Description: 菜单类
 */
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

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
        if (runtimeArgs.length != 1) {
            return RunResult.paramsCountIllegal;
        }
        
        print();
        
        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 添加菜品nd
    private final Lazy<StandardCommand> newDishCommand = new Lazy<>(() -> new StandardCommand("nd", (runtimeArgs) -> {
        if (runtimeArgs.length != 5) {
            return RunResult.paramsCountIllegal;
        }

        String did = runtimeArgs[1];
        String name = runtimeArgs[2];
        double price = -1d;
        int total = -1;

        if (!Dish.checkDID(did)) {
            return new RunResult(DID_INPUT_ILLEGAL);
        }

        Dish dishById = getDishById(did);
        if (dishById != null) {
            return new RunResult(DISH_EXISTS);
        }

        try {
            price = Double.parseDouble(runtimeArgs[3]);
            total = Integer.parseInt(runtimeArgs[4]);
        } catch (NumberFormatException ex) {
            // 忽略...
        }
        if (!Dish.checkName(name) || !Dish.checkPrice(price) || !Dish.checkTotal(total)) {
            return new RunResult(NEW_DISHS_ATTRIBUTES_INPUT_ILLEGAL);
        }

        Dish dishByName = getDishByName(name);
        if (dishByName != null) {
            return new RunResult(NAME_REPEATED);
        }

        addDish(did, name, price, total);

        return RunResult.empty;
    }));
    // </editor-fold>
    // <editor-fold> 获取菜品gd
    private final Lazy<StandardCommand> getDishCommand = new Lazy<>(() -> new StandardCommand("gd", (runtimeArgs) -> {
        if (runtimeArgs.length < 2 || !Arrays.asList("-id", "-key").contains(runtimeArgs[1])) {
            return RunResult.commandNotExists;
        }

        if (runtimeArgs.length != 3) {
            return RunResult.paramsCountIllegal;
        }

        switch (runtimeArgs[1]) {
            case "-id":
                if (!Dish.checkDID(runtimeArgs[2])) {
                    return new RunResult(DID_INPUT_ILLEGAL);
                }

                Dish dishById = getDishById(runtimeArgs[2]);
                if (dishById == null) {
                    return new RunResult(DISH_DOES_NOT_EXIST);
                }

                System.out.println(dishById);
                break;
            case "-key":
                Vector<Dish> list = getDishByKeyWord(runtimeArgs[2]);
                if (list.size() == 0) {
                    return new RunResult(DISH_DOES_NOT_EXIST);
                }

                int i = 1;
                for (Dish dish : list) {
                    System.out.println(String.format("%d. %s", i++, dish));
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
                    return RunResult.commandNotExists;
                }

                if (runtimeArgs.length != 4) {
                    return RunResult.paramsCountIllegal;
                }

                String did = runtimeArgs[2];

                if (!Dish.checkDID(did)) {
                    return new RunResult(DID_INPUT_ILLEGAL);
                }

                Dish dish = getDishById(did);
                if (dish == null) {
                    return new RunResult(DISH_DOES_NOT_EXIST);
                }

                switch (runtimeArgs[1]) {
                    case "-n":
                        String name = runtimeArgs[3];

                        if (!Dish.checkName(name)) {
                            return new RunResult(NEW_NAME_INPUT_ILLEGAL);
                        }

                        Dish dishByName = getDishByName(name);
                        if (dishByName != null) {
                            return new RunResult(NEW_NAME_REPEATED);
                        }

                        dish.setName(name);
                        return new RunResult(UPDATE_DISHS_NAME_SUCCESS);
                    case "-t":
                        int total = -1;

                        try {
                            total = Integer.parseInt(runtimeArgs[3]);
                        } catch (NumberFormatException ex) {
                            // 忽略...
                        }
                        if (!Dish.checkTotal(total)) {
                            return new RunResult(CHANGE_DISHS_TOTAL_ILLEGAL);
                        }

                        dish.setTotal(total);
                        return new RunResult(UPDATE_DISHS_TOTAL_SUCCESS);
                    case "-p":
                        double price = -1d;

                        try {
                            price = Double.parseDouble(runtimeArgs[3]);
                        } catch (NumberFormatException ex) {
                            // 忽略...
                        }
                        if (!Dish.checkPrice(price)) {
                            return new RunResult(CHANGE_DISHS_PRICE_ILLEGAL);
                        }

                        dish.setPrice(price);
                        return new RunResult(UPDATE_DISHS_PRICE_SUCCESS);
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

    private final Map<String, Vector<Dish>> dishes;
    private final Map<String, Vector<Dish>> readonlyDishes;

    /**
     * @description: 默认构造
     * @param {*}
     * @return {*}
     */
    public Menu() {
        dishes = new LinkedHashMap<>();
        dishes.put("H", new Vector<Dish>());// 热菜
        dishes.put("C", new Vector<Dish>());// 凉菜
        dishes.put("O", new Vector<Dish>());// 其它

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
    public Map<String, Vector<Dish>> getDishes() {
        return readonlyDishes;
    }

    /**
     * @description: 通过编号取出菜品
     * @param {String} did
     * @return {*}
     */
    public Dish getDishById(String did) {
        String key = did.substring(0, 1);
        Vector<Dish> list = dishes.get(key);
        if (list != null) {
            for (Dish dish : list) {
                if (did.equals(dish.getDID())) {
                    return dish;
                }
            }
        }

        return null;
    }

    /**
     * @description: 通过名称取出菜品
     * @param {String} name
     * @return {*}
     */
    public Dish getDishByName(String name) {
        for (Entry<String, Vector<Dish>> entry : dishes.entrySet()) {
            for (Dish dish : entry.getValue()) {
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
        for (Entry<String, Vector<Dish>> entry : dishes.entrySet()) {
            Vector<Dish> list = entry.getValue();
            if (list.size() > 0) {
                list.sort((d1, d2) -> d1.getDID().compareTo(d2.getDID()));
                for (Dish dish : list) {
                    if (dish.getName().toLowerCase().contains(lowercaseKeyword)) {
                        res.add(dish);
                    }
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
    public void addDish(String did, String name, double price, int total) {
        String key = did.substring(0, 1);
        Vector<Dish> list = dishes.get(key);
        if (list != null) {
            list.add(new Dish(did, name, price, total));
        }
        System.out.println(ADD_DISH_SUCCESS);
    }

    /**
     * @description: 打印菜单
     * @param {*}
     * @return {*}
     */
    public void print() {
        boolean hasItems = false;
        int i = 1;

        for (Entry<String, Vector<Dish>> entry : dishes.entrySet()) {
            Vector<Dish> list = entry.getValue();
            if (list.size() > 0) {
                hasItems = true;
                list.sort((d1, d2) -> d1.getDID().compareTo(d2.getDID()));
                for (Dish dish : list) {
                    System.out.println(String.format("%d. %s", i++, dish));
                }
            }
        }

        if (!hasItems) {
            System.out.println(EMPTY_MENU);
        }
    }
}
