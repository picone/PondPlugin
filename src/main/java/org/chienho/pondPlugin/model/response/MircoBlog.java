package org.chienho.pondPlugin.model.response;

import org.jetbrains.annotations.Nullable;

public class MircoBlog {
    public String created_at;

    public String text;

    public User user;

    public @Nullable LongText longText;

    public static class LongText {
        public String longTextContent;
    }
}
