package org.chienho.pondPlugin.model.response;

import java.util.List;

public class ZhihuHotList {
    public List<HotItem> data;

    public static class HotItem {
        public String query_display;
        public String real_query;
        public String query_description;
    }
}
