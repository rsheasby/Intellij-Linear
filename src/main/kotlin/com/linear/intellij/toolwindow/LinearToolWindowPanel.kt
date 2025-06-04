package com.linear.intellij.toolwindow

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import com.linear.intellij.models.LinearTask
import com.linear.intellij.services.LinearApiService
import com.linear.intellij.ui.TaskDetailsPanel
import com.linear.intellij.ui.TaskListCellRenderer
import java.awt.BorderLayout
import javax.swing.*

class LinearToolWindowPanel(private val project: Project) : SimpleToolWindowPanel(true, true) {
    private val listModel = DefaultListModel<LinearTask>()
    private val taskList = JBList(listModel)
    private val detailsPanel = TaskDetailsPanel()
    private val mainPanel = JPanel(BorderLayout())
    
    init {
        initComponents()
        loadTasks()
    }
    
    private fun initComponents() {
        taskList.cellRenderer = TaskListCellRenderer()
        taskList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        taskList.addListSelectionListener { e ->
            if (!e.valueIsAdjusting) {
                val selected = taskList.selectedValue
                detailsPanel.setTask(selected)
            }
        }
        
        val scrollPane = JBScrollPane(taskList).apply {
            border = JBUI.Borders.empty()
        }
        
        val splitter = JBSplitter(false, 0.6f).apply {
            firstComponent = scrollPane
            secondComponent = detailsPanel
        }
        
        mainPanel.add(splitter, BorderLayout.CENTER)
        
        setContent(mainPanel)
        setupToolbar()
    }
    
    private fun setupToolbar() {
        val actionGroup = DefaultActionGroup()
        actionGroup.add(ActionManager.getInstance().getAction("com.linear.intellij.actions.RefreshLinearTasks"))
        
        val toolbar = ActionManager.getInstance()
            .createActionToolbar("LinearToolbar", actionGroup, true)
        toolbar.setTargetComponent(mainPanel)
        setToolbar(toolbar.component)
    }
    
    fun loadTasks() {
        val apiService = ApplicationManager.getApplication()
            .getService(LinearApiService::class.java)
        
        if (!apiService.isConfigured()) {
            showConfigurationMessage()
            return
        }
        
        SwingUtilities.invokeLater {
            listModel.clear()
            mainPanel.removeAll()
            mainPanel.add(JBLabel("Loading tasks...", SwingConstants.CENTER), BorderLayout.CENTER)
            mainPanel.revalidate()
            mainPanel.repaint()
        }
        
        apiService.fetchAssignedTasks()
            .thenAccept(::updateTaskList)
            .exceptionally { throwable ->
                SwingUtilities.invokeLater { showErrorMessage(throwable.message ?: "Unknown error") }
                null
            }
    }
    
    private fun updateTaskList(tasks: List<LinearTask>) {
        SwingUtilities.invokeLater {
            listModel.clear()
            tasks.forEach(listModel::addElement)
            
            mainPanel.removeAll()
            if (tasks.isEmpty()) {
                mainPanel.add(JBLabel("No tasks found", SwingConstants.CENTER), BorderLayout.CENTER)
            } else {
                initComponents()
            }
            mainPanel.revalidate()
            mainPanel.repaint()
        }
    }
    
    private fun showConfigurationMessage() {
        mainPanel.removeAll()
        val messagePanel = JPanel(BorderLayout())
        val message = JBLabel(
            "<html><center>Linear API key not configured.<br>Please configure it in Settings > Tools > Linear</center></html>",
            SwingConstants.CENTER
        )
        messagePanel.add(message, BorderLayout.CENTER)
        mainPanel.add(messagePanel, BorderLayout.CENTER)
        mainPanel.revalidate()
        mainPanel.repaint()
    }
    
    private fun showErrorMessage(error: String) {
        mainPanel.removeAll()
        mainPanel.add(JBLabel("Error: $error", SwingConstants.CENTER), BorderLayout.CENTER)
        mainPanel.revalidate()
        mainPanel.repaint()
    }
}