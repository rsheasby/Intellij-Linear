package com.linear.intellij.toolwindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.linear.intellij.models.LinearTask;
import com.linear.intellij.services.LinearApiService;
import com.linear.intellij.ui.TaskDetailsPanel;
import com.linear.intellij.ui.TaskListCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LinearToolWindowPanel extends SimpleToolWindowPanel {
    private final Project project;
    private final JBList<LinearTask> taskList;
    private final DefaultListModel<LinearTask> listModel;
    private final TaskDetailsPanel detailsPanel;
    private final JPanel mainPanel;
    
    public LinearToolWindowPanel(Project project) {
        super(true, true);
        this.project = project;
        this.listModel = new DefaultListModel<>();
        this.taskList = new JBList<>(listModel);
        this.detailsPanel = new TaskDetailsPanel();
        this.mainPanel = new JPanel(new BorderLayout());
        
        initComponents();
        loadTasks();
    }
    
    private void initComponents() {
        taskList.setCellRenderer(new TaskListCellRenderer());
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                LinearTask selected = taskList.getSelectedValue();
                detailsPanel.setTask(selected);
            }
        });
        
        JBScrollPane scrollPane = new JBScrollPane(taskList);
        scrollPane.setBorder(JBUI.Borders.empty());
        
        JBSplitter splitter = new JBSplitter(false, 0.6f);
        splitter.setFirstComponent(scrollPane);
        splitter.setSecondComponent(detailsPanel);
        
        mainPanel.add(splitter, BorderLayout.CENTER);
        
        setContent(mainPanel);
        setupToolbar();
    }
    
    private void setupToolbar() {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(ActionManager.getInstance().getAction("com.linear.intellij.actions.RefreshLinearTasks"));
        
        ActionToolbar toolbar = ActionManager.getInstance()
                .createActionToolbar("LinearToolbar", actionGroup, true);
        toolbar.setTargetComponent(mainPanel);
        setToolbar(toolbar.getComponent());
    }
    
    public void loadTasks() {
        LinearApiService apiService = ApplicationManager.getApplication()
                .getService(LinearApiService.class);
        
        if (!apiService.isConfigured()) {
            showConfigurationMessage();
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            mainPanel.removeAll();
            mainPanel.add(new JBLabel("Loading tasks...", SwingConstants.CENTER), BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        
        apiService.fetchAssignedTasks()
                .thenAccept(this::updateTaskList)
                .exceptionally(throwable -> {
                    SwingUtilities.invokeLater(() -> showErrorMessage(throwable.getMessage()));
                    return null;
                });
    }
    
    private void updateTaskList(List<LinearTask> tasks) {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            tasks.forEach(listModel::addElement);
            
            mainPanel.removeAll();
            if (tasks.isEmpty()) {
                mainPanel.add(new JBLabel("No tasks found", SwingConstants.CENTER), BorderLayout.CENTER);
            } else {
                initComponents();
            }
            mainPanel.revalidate();
            mainPanel.repaint();
        });
    }
    
    private void showConfigurationMessage() {
        mainPanel.removeAll();
        JPanel messagePanel = new JPanel(new BorderLayout());
        JBLabel message = new JBLabel("<html><center>Linear API key not configured.<br>Please configure it in Settings > Tools > Linear</center></html>", 
                SwingConstants.CENTER);
        messagePanel.add(message, BorderLayout.CENTER);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void showErrorMessage(String error) {
        mainPanel.removeAll();
        mainPanel.add(new JBLabel("Error: " + error, SwingConstants.CENTER), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}