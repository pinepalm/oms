/*
 * @Author: Zhe Chen
 * @Date: 2021-03-25 00:04:09
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-06 00:30:57
 * @Description: 运行结果
 */

/**
 * @description: 运行结果
 */
public class RunResult {
    private static final String ILLEGAL = "%s illegal";
    private static final String PARAMS_COUNT_ILLEGAL = String.format(ILLEGAL, "Params' count");
    private static final String INPUT_ILLEGAL = String.format(ILLEGAL, "Input");
    private static final String CALL_INNER_METHOD_ILLEGAL = String.format(ILLEGAL, "Call inner method");

    private static final String COMMAND_NOT_EXIST = "Command not exist"; 

    public static final RunResult empty = new RunResult(null);
    public static final RunResult inputIllegal = new RunResult(INPUT_ILLEGAL);
    public static final RunResult paramsCountIllegal = new RunResult(PARAMS_COUNT_ILLEGAL);
    public static final RunResult callInnerMethodIllegal = new RunResult(CALL_INNER_METHOD_ILLEGAL);
    public static final RunResult commandNotExist = new RunResult(COMMAND_NOT_EXIST);


    public final String message;

    public RunResult(String message) {
        this.message = message;
    }

    public void print() {
        if (message == null)
            return;

        System.out.println(message);
    }
}
