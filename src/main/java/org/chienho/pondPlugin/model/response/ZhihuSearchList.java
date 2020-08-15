package org.chienho.pondPlugin.model.response;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ZhihuSearchList {

    public List<SearchItem> item;

    public Paging paging;

    public static class SearchItem {
        public MainObject object;

        public static class MainObject {
            public @Nullable String excerpt;
            public @Nullable Author author;
            public @Nullable Question question;

            public static class Author {
                public @Nullable String name;
            }

            public static class Question {
                public @Nullable String name;
            }
        }
    }

    public static class Paging {
        public boolean is_end;
        public @Nullable String next;
    }
}
