package org.chienho.pondPlugin.toolWindow.panel;

import com.intellij.util.Url;
import org.chienho.pondPlugin.model.MultiHtmlText;
import org.chienho.pondPlugin.model.response.ContainerBaseCard;
import org.chienho.pondPlugin.service.Weibo;
import org.chienho.pondPlugin.toolWindow.MultilinePanel;
import org.chienho.pondPlugin.toolWindow.component.MultilineTextList;
import org.chienho.pondPlugin.util.URLParser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeiboPanel extends MultilinePanel {

    @Override
    protected void onUpdate() {
        new Thread(() -> {
            List<ContainerBaseCard> hotList = Weibo.getHotList();
            if (hotList == null) {
                return;
            }
            List<MultiHtmlText> modelList = new ArrayList<>();
            for (ContainerBaseCard cardGroup : hotList) {
                if (cardGroup.title == null || cardGroup.card_group == null || cardGroup.card_group.isEmpty()) {
                    continue;
                }
                modelList.add(new MultiHtmlText(cardGroup.title));
                for (ContainerBaseCard card : cardGroup.card_group) {
                    if (card.desc == null) {
                        continue;
                    }
                    MultiHtmlText text = new MultiHtmlText(card.desc);
                    String containerId = card.getSchemeContainerId();
                    if (containerId != null) {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put(MultiHtmlText.PARAMETER_CONTAINER_ID, containerId);
                        text.setUrl(MultiHtmlText.SCHEME_POND,
                                MultiHtmlText.AUTHORITY_WEIBO,
                                MultiHtmlText.PATH_CONTAINER_INDEX,
                                parameters);
                    }
                    modelList.add(text);
                }
                textList.goTo(modelList, true);
            }
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
        if (url.getAuthority().equals(MultiHtmlText.AUTHORITY_WEIBO) && url.getPath().equals(MultiHtmlText.PATH_CONTAINER_INDEX)) {
            if (url.getParameters() == null) {
                return;
            }
            String containerId = URLParser.getParameter(url.getParameters(), MultiHtmlText.PARAMETER_CONTAINER_ID);
            if (containerId != null) {
                loadWeiboList(list, containerId);
            }
        }
    }

    private void loadWeiboList(@NotNull MultilineTextList<?> list, @NotNull String containerId) {
        new Thread(() -> {
            List<ContainerBaseCard> weiboList = Weibo.getContainerIndex(containerId);
            if (weiboList == null) {
                return;
            }
            list.setEnabled(false);
            List<MultiHtmlText> modelList = new ArrayList<>();
            for (ContainerBaseCard cardGroup : weiboList) {
                if (cardGroup.mblog != null) {
                    String text = cardGroup.mblog.longText != null ? cardGroup.mblog.longText.longTextContent : cardGroup.mblog.text;
                    MultiHtmlText model = new MultiHtmlText(text);
                    model.setHeadText(cardGroup.mblog.user.screen_name);
                    modelList.add(model);
                    continue;
                }
                if (cardGroup.card_group != null) {
                    for (ContainerBaseCard card : cardGroup.card_group) {
                        if (card.mblog != null) {
                            String text = card.mblog.longText != null ? card.mblog.longText.longTextContent : card.mblog.text;
                            MultiHtmlText model = new MultiHtmlText(text);
                            model.setHeadText(card.mblog.user.screen_name);
                            modelList.add(model);
                        }
                    }
                }
            }

            list.setEnabled(true);
            textList.goTo(modelList, false);
        }).start();
    }
}
