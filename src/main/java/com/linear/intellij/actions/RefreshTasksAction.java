package com.linear.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.linear.intellij.toolwindow.LinearToolWindowPanel;
import org.jetbrains.annotations.NotNull;

public class RefreshTasksAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        
        ToolWindow toolWindow = ToolWindowManager.getInstance(project)
                .getToolWindow("Linear Tasks");
        
        if (toolWindow != null) {
            Content content = toolWindow.getContentManager().getContent(0);
            if (content != null && content.getComponent() instanceof LinearToolWindowPanel) {
                LinearToolWindowPanel panel = (LinearToolWindowPanel) content.getComponent();
                panel.loadTasks();
            }
        }
    }
    
    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }
}