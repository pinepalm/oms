/*
 * @Author: Zhe Chen
 * @Date: 2021-03-25 19:32:12
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-15 22:54:37
 * @Description: 人员列表类
 */
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * @description: 人员列表类
 */
public final class PersonList extends GroupingBase<String, Person> implements ICommandContainer {
    // <editor-fold> 字符串常量
    private static final String ILLEGAL = "%s illegal";
    private static final String PHONE_NUMBER_ILLEGAL = String.format(ILLEGAL, "Phone number");
    private static final String SEX_ILLEGAL = String.format(ILLEGAL, "Sex");
    // private static final String ARGUMENTS_ILLEGAL = String.format(ILLEGAL,
    // "Arguments");

    private static final String CUSTOMER = "Cu";
    private static final String WAITER = "Wa";
    private static final String COOK = "Co";
    private static final String PID = "PID";
    private static final String NAME = "Name";
    private static final String PHONE_NUM = "PhoneNum";
    // </editor-fold>

    // <editor-fold> 添加人np
    private final Lazy<StandardCommand> newPersonCommand = new Lazy<>(() -> new StandardCommand("np", (runtimeArgs) -> {
        if (runtimeArgs.length != 4) {
            // return new RunResult(ARGUMENTS_ILLEGAL);
            return RunResult.paramsCountIllegal;
        }

        return OmsManager.handleRunRequest(() -> {
            return addPerson(runtimeArgs[1], runtimeArgs[2], runtimeArgs[3]);
        }, (person) -> new RunResult(person.toString()));
    }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(newPersonCommand.getValue()));
    // </editor-fold>

    public static final PersonList instance = new PersonList();

    private PersonList() {
        Comparator<Person> personComparator = (p1, p2) -> p1.getPID().compareTo(p2.getPID());

        items.put(CUSTOMER, new TreeSet<Person>(personComparator));// 顾客
        items.put(WAITER, new TreeSet<Person>(personComparator));// 服务员
        items.put(COOK, new TreeSet<Person>(personComparator));// 厨师

        indexers.put(PID, new HashMap<Object, Person>());
        indexers.put(NAME, new HashMap<Object, Person>());
        indexers.put(PHONE_NUM, new HashMap<Object, Person>());
    }

    @Override
    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }

    /**
     * @description: 添加人
     * @param {String} name
     * @param {String} sex
     * @param {String} phoneNum
     * @return {*}
     */
    public Person addPerson(String name, String sex, String phoneNum) throws IllegalArgumentException {
        if (!Person.checkSex(sex)) {
            throw new IllegalArgumentException(SEX_ILLEGAL);
        }

        String last = phoneNum.length() > 0 ? phoneNum.substring(phoneNum.length() - 1) : "";
        if (!(Person.checkNum(phoneNum) && ((sex.equals("M") && last.equals("0")) || (sex.equals("F") && last.equals("1"))))) {
            throw new IllegalArgumentException(PHONE_NUMBER_ILLEGAL);
        }

        return new Person(name, sex, phoneNum);
    }

    public void addPerson(String name, String sex, String phoneNum, String pid) {

    }

    public void deletePerson(String pid) {

    }

    public void print() {

    }
}
