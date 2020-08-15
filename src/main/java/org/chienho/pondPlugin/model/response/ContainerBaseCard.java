package org.chienho.pondPlugin.model.response;

import com.intellij.util.Url;
import com.intellij.util.Urls;
import org.chienho.pondPlugin.util.URLParser;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ContainerBaseCard {
    public @Nullable String title;

    public @Nullable String scheme;

    public @Nullable String desc;

    public @Nullable MircoBlog mblog;

    public @Nullable List<ContainerBaseCard> card_group;

    /**
     * 从scheme中解析出container id
     * @return container id
     */
    public @Nullable String getSchemeContainerId() {
        if (scheme == null) {
            return null;
        }
        Url url = Urls.parse(scheme, false);
        if (url == null || url.getParameters() == null) {
            return null;
        }
        return URLParser.getParameter(url.getParameters(), "containerid");
    }
}
