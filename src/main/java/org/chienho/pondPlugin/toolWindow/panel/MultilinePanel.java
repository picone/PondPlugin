package org.chienho.pondPlugin.toolWindow.panel;

import com.intellij.util.Url;
import org.chienho.pondPlugin.model.MultiHtmlText;
import org.chienho.pondPlugin.toolWindow.component.MultilineTextList;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class MultilinePanel {
    protected JTextField searchText;
    protected JButton searchButton;
    protected JPanel mainContent;
    protected MultilineTextList<MultiHtmlText> textList;
    protected JButton backButton;

    public MultilinePanel() {
        searchButton.addActionListener(e -> onSearchButtonClick());
        backButton.addActionListener(e -> textList.goBack());
        textList.setOnClickListener(this::onTextListClick);
    }

    public JPanel getContent() {
        return mainContent;
    }

    public void createUIComponents() {
        textList = new MultilineTextList<>();
    }

    private void onSearchButtonClick() {
        if (searchText.getText().isEmpty()) {
            onUpdate();
        } else {
            onSearch(searchText.getText());
        }
    }

    protected abstract void onUpdate();

    protected abstract void onSearch(String text);

    protected abstract void onTextListClick(@NotNull MultilineTextList<?> list, @NotNull Url url);
}
