/*
 * @Author: Zhe Chen
 * @Date: 2021-03-25 19:32:12
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-02 21:16:15
 * @Description: 人员列表类
 */
import java.util.Arrays;

/**
 * @description: 人员列表类
 */
public final class PersonList implements ICommandContainer {
    // <editor-fold> 字符串常量
    private static final String ILLEGAL = "%s illegal";
    private static final String PHONE_NUMBER_ILLEGAL = String.format(ILLEGAL, "Phone number");
    private static final String SEX_ILLEGAL = String.format(ILLEGAL, "Sex");
    //private static final String ARGUMENTS_ILLEGAL = String.format(ILLEGAL, "Arguments");
    // </editor-fold>

    // <editor-fold> 添加人np
    private final Lazy<StandardCommand> newPersonCommand = new Lazy<>(
            () -> new StandardCommand("np", (runtimeArgs) -> {
                if (runtimeArgs.length != 4) {
                    //return new RunResult(ARGUMENTS_ILLEGAL);
                    return RunResult.paramsCountIllegal;
                }

                if (runtimeArgs[2].length() != 1) {
                    return new RunResult(SEX_ILLEGAL);
                }

                Person person = addPerson(runtimeArgs[1], runtimeArgs[2].charAt(0), runtimeArgs[3]);
                if (person != null) {
                    System.out.println(person);
                }
                
                return RunResult.empty;
            }));
    // </editor-fold>
    // <editor-fold> 命令枚举
    private final Lazy<Iterable<ICommand>> commands = new Lazy<>(() -> Arrays.asList(newPersonCommand.getValue()));
    // </editor-fold>

    public PersonList() {

    }

    public Iterable<ICommand> getCommands() {
        return commands.getValue();
    }

    /**
     * @description: 添加人
     * @param {String} name
     * @param {char}   sex
     * @param {String} phoneNum
     * @return {*}
     */
    public Person addPerson(String name, char sex, String phoneNum) {
        if (!Person.checkSex(sex)) {
            System.out.println(SEX_ILLEGAL);
            return null;
        }

        char last = phoneNum.length() > 0 ? phoneNum.charAt(phoneNum.length() - 1) : '\0';
        if (!(Person.checkNum(phoneNum) && ((sex == 'M' && last == '0') || (sex == 'F' && last == '1')))) {
            System.out.println(PHONE_NUMBER_ILLEGAL);
            return null;
        }

        return new Person(name, sex, phoneNum);
    }
}
