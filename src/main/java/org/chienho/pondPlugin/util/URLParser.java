package org.chienho.pondPlugin.util;

import com.google.common.base.Splitter;
import com.intellij.util.io.URLUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class URLParser {

    /**
     * 解析query列表
     * 注意，还不支持query的值是数组
     * @param rawParameter 原始query string
     * @return 解析后的kv map
     */
    public static @Nullable Map<String, String> getParameters(@Nullable String rawParameter) {
        if (rawParameter == null) {
            return null;
        }
        int pos = rawParameter.indexOf('?');
        if (pos >= 0) {
            rawParameter = rawParameter.substring(pos + 1);
        }
        return Splitter.on('&').withKeyValueSeparator('=').split(rawParameter);
    }

    /**
     * 从query string中解析出指定的query
     * @param rawParameter 原始query string
     * @param name 指定的query名字
     * @return 指定query的值
     */
    public static @Nullable String getParameter(@Nullable String rawParameter, @NotNull String name) {
        Map<String, String> parameters = getParameters(rawParameter);
        if (parameters == null) {
            return null;
        }
        String parameter = parameters.get(name);
        if (parameter != null) {
            parameter = URLUtil.unescapePercentSequences(parameter);
        }
        return parameter;
    }
}
