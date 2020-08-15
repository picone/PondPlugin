package org.chienho.pondPlugin.toolWindow.component;

import com.intellij.util.Url;
import com.intellij.util.Urls;
import com.intellij.util.ui.HtmlPanel;
import com.intellij.util.ui.JBDimension;
import org.chienho.pondPlugin.model.MultiHtmlText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.HyperlinkEvent;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultilineTextList<E> extends HtmlPanel {

    private final Navigation navigation = new Navigation();
    private @Nullable MultilineTextClickListener clickListener;

    public MultilineTextList() {
        super();
        setEditable(false);
        setPreferredSize(new JBDimension(150, 100));
    }

    /**
     * 跳转到指定页面
     * @param model 页面的model list
     * @param replace 是否清空当前展现的栈再跳转
     */
    public void goTo(List<E> model, boolean replace) {
        navigation.goTo(model, replace);
        update();
    }

    /**
     * 返回上一个页面
     */
    public void goBack() {
        if (navigation.goBack() != null) {
            update();
        }
    }

    /**
     * 当超级链接被滑过或者点击时候触发
     * @param e 事件
     */
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (clickListener == null) {
            super.hyperlinkUpdate(e);
            return;
        }
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            Url url = Urls.parse(e.getDescription(), false);
            if (url != null && url.getScheme() != null && url.getScheme().equals(MultiHtmlText.SCHEME_POND)) {
                clickListener.onClick(this, url);
                return;
            }
        }
        super.hyperlinkUpdate(e);
    }

    /**
     * @return HTML正文body部分
     */
    @Override
    protected @NotNull String getBody() {
        if (navigation == null) {
            return "";
        }
        List<E> model = navigation.current();
        if (model == null || model.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("<table style=\"border-collapse:collapse;\">");
        for (E item : model) {
            sb.append("<tr style=\"border:1px solid black;\"><td>");
            sb.append(item.toString());
            sb.append("</td></tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    private class Navigation {
        private final Stack<List<E>> stack = new Stack<>();
        private final Lock stackLock = new ReentrantLock();

        /**
         * 导航返回
         * @return 返回后应当展示的model list
         */
        public @Nullable List<E> goBack() {
            List<E> ret = null;
            stackLock.lock();
            try {
                if (stack.size() >= 2) {
                    stack.pop();
                    ret = stack.peek();
                }
            } finally {
                stackLock.unlock();
            }
            return ret;
        }

        /**
         * 跳转到某一个页面
         * @param model 需要展现的model list
         * @param replace 是否清空当前历史栈再跳转
         */
        public void goTo(List<E> model, boolean replace) {
            stackLock.lock();
            try {
                if (replace) {
                    stack.clear();
                }
                stack.push(model);
            } finally {
                stackLock.unlock();
            }
        }

        /**
         * @return 当前栈顶的model list
         */
        public @Nullable List<E> current() {
            try {
                return stack.peek();
            } catch (EmptyStackException e) {
                return null;
            }
        }
    }

    public void setOnClickListener(MultilineTextClickListener listener) {
        clickListener = listener;
    }

    public interface MultilineTextClickListener {
        void onClick(@NotNull MultilineTextList<?> list, @NotNull Url url);
    }
}
