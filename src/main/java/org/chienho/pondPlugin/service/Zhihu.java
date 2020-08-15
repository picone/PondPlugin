package org.chienho.pondPlugin.service;

import com.intellij.util.Url;
import com.intellij.util.Urls;
import com.intellij.util.io.HttpRequests;
import com.intellij.util.io.URLUtil;
import org.chienho.pondPlugin.model.response.ZhihuHotList;
import org.chienho.pondPlugin.model.response.ZhihuSearchList;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Zhihu {
    private final static String URL_HOT = "https://www.zhihu.com/api/v4/search/top_search/tabs/hot/items";
    private final static String HOST_ZHIHU = "www.zhihu.com";
    private final static String PATH_SEARCH = "/api/v4/search_v3";

    public static @Nullable List<ZhihuHotList.HotItem> getHotList() {
        try {
            return HttpRequests
                    .request(URL_HOT)
                    .connect(request -> {
                        ZhihuHotList hotList = JsonDecode.getMapper().readValue(request.readString(), ZhihuHotList.class);
                        if (hotList.data == null || hotList.data.isEmpty()) {
                            return null;
                        }
                        return hotList.data;
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static @Nullable ZhihuSearchList search(String url) {
        try {
            return HttpRequests
                    .request(url)
                    .connect(request -> {
                        return JsonDecode.getMapper().readValue(request.readString(), ZhihuSearchList.class);
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static @Nullable ZhihuSearchList search(String query, int offset, int limit) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("t", "general");
        parameters.put("q", query);
        parameters.put("correction", "1");
        parameters.put("offset", String.valueOf(offset));
        parameters.put("limit", String.valueOf(limit));
        parameters.put("lc_index", "0");
        parameters.put("show_all_topics", "0");
        Url url = Urls.newUrl(URLUtil.HTTPS_PROTOCOL, HOST_ZHIHU, PATH_SEARCH, parameters);
        return search(url.toString());
    }
}
