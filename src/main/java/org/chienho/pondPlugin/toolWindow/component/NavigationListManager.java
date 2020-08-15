package org.chienho.pondPlugin.toolWindow.component;

import com.sun.istack.Nullable;

import javax.swing.*;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NavigationListManager<E> {

    private final Stack<ListModel<E>> stack = new Stack<>();

    private final Lock stackLock = new ReentrantLock();

    private final JList<E> list;

    private final DefaultListModel<E> model = new DefaultListModel<>();

    public NavigationListManager(JList<E> list) {
        this.list = list;
        //this.list.setModel(model);
    }

    public void goBack() {
        stackLock.lock();
        try {
            if (stack.size() >= 2) {
                stack.pop();
                list.setModel(stack.peek());
//                ListModel<E> model = stack.peek();
//                this.model.clear();
//                for (int i = 0; i < model.getSize(); ++i) {
//                    this.model.addElement(model.getElementAt(i));
//                }
            }
        } finally {
            stackLock.unlock();
        }
    }

    public void goTo(ListModel<E> model) {
        goTo(model, false);
    }

    public void goTo(ListModel<E> model, boolean replace) {
        stackLock.lock();
        try {
            if (replace) {
                stack.clear();
            }
            stack.push(model);
            list.setModel(model);
//            this.model.clear();
//            for (int i = 0; i < model.getSize(); ++i) {
//                this.model.addElement(model.getElementAt(i));
//            }
        } finally {
            stackLock.unlock();
        }
    }

    @Nullable
    public ListModel<E> getTop() {
        ListModel<E> model = null;
        stackLock.lock();
        try {
            if (!stack.isEmpty()) {
                model = stack.peek();
            }
        } finally {
            stackLock.unlock();
        }
        return model;
    }
}
