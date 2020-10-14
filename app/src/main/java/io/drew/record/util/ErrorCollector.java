package io.drew.record.util;

import com.umeng.umcrash.UMCrash;

/**
 * @Author: KKK
 * @CreateDate: 2020/8/25 2:42 PM
 */
public class ErrorCollector {
    public static final String ERROR_TYPE_COLLECT = "error_type_collect";

    private static ErrorCollector errorCollector;

    public static ErrorCollector instance() {
        if (errorCollector == null) {
            errorCollector = new ErrorCollector();
        }
        return errorCollector;
    }

    public void reportError(String content, String type) {
        UMCrash.generateCustomLog(content, type);
    }
}
