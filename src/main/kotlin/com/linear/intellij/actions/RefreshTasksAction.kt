package com.linear.intellij.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.ToolWindowManager
import com.linear.intellij.toolwindow.LinearToolWindowPanel

class RefreshTasksAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        
        val toolWindow = ToolWindowManager.getInstance(project)
            .getToolWindow("Linear Tasks")
        
        toolWindow?.let { window ->
            val content = window.contentManager.getContent(0)
            val component = content?.component
            if (component is LinearToolWindowPanel) {
                component.loadTasks()
            }
        }
    }
    
    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
}