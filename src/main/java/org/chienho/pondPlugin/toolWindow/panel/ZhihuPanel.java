package org.chienho.pondPlugin.toolWindow.panel;

import com.intellij.util.Url;
import org.chienho.pondPlugin.model.MultiHtmlText;
import org.chienho.pondPlugin.model.response.ContainerBaseCard;
import org.chienho.pondPlugin.model.response.ZhihuHotList;
import org.chienho.pondPlugin.model.response.ZhihuSearchList;
import org.chienho.pondPlugin.service.Weibo;
import org.chienho.pondPlugin.service.Zhihu;
import org.chienho.pondPlugin.toolWindow.component.MultilineTextList;
import org.chienho.pondPlugin.util.URLParser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZhihuPanel extends MultilinePanel {

    @Override
    protected void onUpdate() {
        new Thread(() -> {
            List<ZhihuHotList.HotItem> hotList = Zhihu.getHotList();
            if (hotList == null) {
                return;
            }
            List<MultiHtmlText> modelList = new ArrayList<>();
            for (ZhihuHotList.HotItem item : hotList) {
                if (item.query_display.isEmpty()) {
                    continue;
                }
                MultiHtmlText model = new MultiHtmlText(item.query_display + "，" + item.query_description);
                if (!item.real_query.isEmpty()) {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put(MultiHtmlText.PARAMETER_QUERY, item.real_query);
                    model.setUrl(MultiHtmlText.SCHEME_POND,MultiHtmlText.AUTHORITY_ZHIHU, MultiHtmlText.PATH_SEARCH, parameters);
                }
                modelList.add(model);
            }
            textList.goTo(modelList, true);
        }).start();
    }

    @Override
    protected void onSearch(String text) {

    }

    @Override
    protected void onTextListClick(@NotNull MultilineTextList<?> list, @NotNull Url url) {
        if (url.getAuthority() == null) {
            return;
        }
        if (url.getAuthority().equals(MultiHtmlText.AUTHORITY_ZHIHU) && url.getPath().equals(MultiHtmlText.PATH_SEARCH)) {
            if (url.getParameters() == null) {
                return;
            }
            String query = URLParser.getParameter(url.getParameters(), MultiHtmlText.PARAMETER_QUERY);
            if (query != null) {
                searchZhihu(list, query);
            }
        }
    }

    private void searchZhihu(@NotNull MultilineTextList<?> list, @NotNull String query) {
        new Thread(() -> {
            ZhihuSearchList searchList = Zhihu.search(query, 0, 10);
            if (searchList == null || searchList.item == null || searchList.item.isEmpty()) {
                return;
            }
            list.setEnabled(false);
            List<MultiHtmlText> modelList = new ArrayList<>();
            for (ZhihuSearchList.SearchItem item : searchList.item) {
                if (item.object == null || item.object.excerpt == null) {
                    continue;
                }
                MultiHtmlText model = new MultiHtmlText(item.object.excerpt);
                if (item.object.author != null) {
                    model.setHeadText(item.object.author.name);
                }
                if (item.object.question != null) {
                    model.setTopText(item.object.question.name);
                }
                modelList.add(model);
            }
            list.setEnabled(true);
            textList.goTo(modelList, false);
        }).start();
    }
}
