/*
 * @Author: Zhe Chen
 * @Date: 2021-04-16 01:47:07
 * @LastEditors: Zhe Chen
 * @LastEditTime: 2021-04-24 12:02:16
 * @Description: 运行请求工具类
 */
package com.buaa.appmodel.cli.util;

import java.util.function.Function;
import java.util.function.Supplier;

import com.buaa.appmodel.cli.RunResult;

/**
 * 运行请求工具类
 */
public final class RunRequestUtil {
    /**
     * 处理运行请求(无参)
     * 
     * @param action  运行请求
     * @param success 运行成功返回结果
     * @return 若有异常, 则返回包含异常信息的运行结果
     */
    public static RunResult handleRunRequest(Runnable action, Supplier<RunResult> success) {
        return handleRunRequest(action, success, (ex) -> new RunResult(ex.getMessage()));
    }

    /**
     * 处理运行请求(单参)
     * 
     * @param <T>      运行请求返回结果的类型
     * @param function 运行请求
     * @param success  运行成功返回结果
     * @return 若有异常, 则返回包含异常信息的运行结果
     */
    public static <T> RunResult handleRunRequest(Supplier<T> function, Function<T, RunResult> success) {
        return handleRunRequest(function, success, (ex) -> new RunResult(ex.getMessage()));
    }

    /**
     * 处理运行请求(无参)
     * 
     * @param action  运行请求
     * @param success 运行成功返回结果
     * @param fail    发生异常时返回结果
     * @return 运行结果
     */
    public static RunResult handleRunRequest(Runnable action, Supplier<RunResult> success,
            Function<Exception, RunResult> fail) {
        try {
            action.run();
        } catch (Exception ex) {
            return fail.apply(ex);
        }

        return success.get();
    }

    /**
     * 处理运行请求(单参)
     * 
     * @param function 运行请求
     * @param success  运行成功返回结果
     * @param fail     发生异常时返回结果
     * @return 运行结果
     */
    public static <T> RunResult handleRunRequest(Supplier<T> function, Function<T, RunResult> success,
            Function<Exception, RunResult> fail) {
        T res;

        try {
            res = function.get();
        } catch (Exception ex) {
            return fail.apply(ex);
        }

        return success.apply(res);
    }

    private RunRequestUtil() {

    }
}
