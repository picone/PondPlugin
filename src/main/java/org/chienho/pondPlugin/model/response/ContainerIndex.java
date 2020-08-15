package org.chienho.pondPlugin.model.response;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ContainerIndex {
    public int ok;
    public @Nullable Data data;
    public static class Data {
        public List<ContainerBaseCard> cards;
    }
}
