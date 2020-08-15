package org.chienho.pondPlugin.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.chienho.pondPlugin.toolWindow.panel.WeiboPanel;
import org.chienho.pondPlugin.toolWindow.panel.ZhihuPanel;
import org.jetbrains.annotations.NotNull;

public class UIFactory implements ToolWindowFactory {

    /**
     * 创建新窗口内容
     * @param project
     * @param toolWindow
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        //微博
        Content weiboContent = contentFactory.createContent(new WeiboPanel().getContent(), "Weibo", true);
        toolWindow.getContentManager().addContent(weiboContent);
        //知乎
        Content zhihuContent = contentFactory.createContent(new ZhihuPanel().getContent(), "Zhihu", true);
        toolWindow.getContentManager().addContent(zhihuContent);
    }
}
