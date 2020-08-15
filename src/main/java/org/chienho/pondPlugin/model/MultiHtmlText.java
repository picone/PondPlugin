package org.chienho.pondPlugin.model;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Url;
import com.intellij.util.Urls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MultiHtmlText {
    public static final String SCHEME_POND = "pond";
    public static final String AUTHORITY_WEIBO = "weibo";
    public static final String PATH_CONTAINER_INDEX = "/container_index";
    public static final String PARAMETER_CONTAINER_ID = "container_id";

    private final @NotNull String text;
    private @Nullable Url url;
    private @Nullable String headText;

    public MultiHtmlText(String text) {
        this.text = StringUtil.removeHtmlTags(text);
    }

    /**
     * 设置跳转的url
     * @param scheme
     * @param authority
     * @param path
     * @param parameters
     */
    public void setUrl(String scheme, String authority, String path, Map<String, String> parameters) {
        url = Urls.newUrl(scheme, authority, path, parameters);
    }

    /**
     * 为html字符串设置头部内容，一般是作者等
     * @param headText 头部内容
     */
    public void setHeadText(@Nullable String headText) {
        this.headText = headText;
    }

    /**
     * 把上述内容拼装成html字符串
     * @return html字符串结果
     */
    public String toString() {
        String htmlText = text;
        if (headText != null && !headText.isEmpty()) {
            htmlText = "<b>" + headText + "：</b>" + htmlText;
        }
        if (url != null) {
            return "<a href=\"" + url.toString() + "\">" + htmlText + "</a>";
        } else {
            return htmlText;
        }
    }
}
