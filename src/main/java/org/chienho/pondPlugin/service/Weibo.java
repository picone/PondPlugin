package org.chienho.pondPlugin.service;

import com.intellij.util.io.URLUtil;
import org.chienho.pondPlugin.model.response.ContainerBaseCard;
import org.chienho.pondPlugin.model.response.ContainerIndex;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.util.Url;
import com.intellij.util.Urls;
import com.intellij.util.io.HttpRequests;
import com.sun.istack.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Weibo {

    final static String HOST_WEIBO = "m.weibo.cn";
    final static String PATH_CONTAINER_INDEX = "/api/container/getIndex";
    final static String CONTAINER_ID_HOT = "106003type=25&t=3&disable_hot=1&filter_type=realtimehot";
    final static String USER_AGENT = "Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko)";

    private final static ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Nullable
    public static List<ContainerBaseCard> getContainerIndex(String containerId) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("containerid", containerId);
            Url url = Urls.newUrl(URLUtil.HTTPS_PROTOCOL, HOST_WEIBO, PATH_CONTAINER_INDEX, parameters);
            return HttpRequests
                    .request(url.toString())
                    .tuner(connection -> {
                        connection.setRequestProperty("referer", Urls.newUrl(URLUtil.HTTPS_PROTOCOL, HOST_WEIBO, "/", "").toString());
                        connection.setRequestProperty("user-agent", USER_AGENT);
                    })
                    .connect(request -> {
                        ContainerIndex containerIndex = MAPPER.readValue(request.readString(), ContainerIndex.class);
                        if (containerIndex == null || containerIndex.ok != 1 || containerIndex.data == null) {
                            return null;
                        }
                        if (containerIndex.data.cards.isEmpty()) {
                            return null;
                        }
                        return containerIndex.data.cards;
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static List<ContainerBaseCard> getHotList() {
        return getContainerIndex(CONTAINER_ID_HOT);
    }
}
