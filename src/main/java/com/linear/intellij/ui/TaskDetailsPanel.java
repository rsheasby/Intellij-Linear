package com.linear.intellij.ui;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.linear.intellij.models.LinearTask;

import javax.swing.*;
import java.awt.*;

public class TaskDetailsPanel extends JPanel {
    private final JBLabel titleLabel;
    private final JBLabel identifierLabel;
    private final JBLabel stateLabel;
    private final JBLabel priorityLabel;
    private final JBTextArea descriptionArea;
    private final HyperlinkLabel viewInLinearLink;
    private final JPanel labelsPanel;
    private String currentTaskUrl = null;
    
    public TaskDetailsPanel() {
        setLayout(new BorderLayout());
        setBorder(JBUI.Borders.empty(10));
        
        JPanel contentPanel = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 5, true, false));
        
        titleLabel = new JBLabel();
        titleLabel.setFont(titleLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        
        identifierLabel = new JBLabel();
        identifierLabel.setForeground(JBColor.GRAY);
        
        stateLabel = new JBLabel();
        priorityLabel = new JBLabel();
        
        descriptionArea = new JBTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(getBackground());
        
        viewInLinearLink = new HyperlinkLabel("View in Linear");
        viewInLinearLink.setIcon(null);
        viewInLinearLink.addHyperlinkListener(e -> {
            if (currentTaskUrl != null) {
                BrowserUtil.browse(currentTaskUrl);
            }
        });
        
        labelsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        labelsPanel.setOpaque(false);
        
        contentPanel.add(titleLabel);
        contentPanel.add(identifierLabel);
        contentPanel.add(createFieldPanel("State:", stateLabel));
        contentPanel.add(createFieldPanel("Priority:", priorityLabel));
        contentPanel.add(new JSeparator());
        contentPanel.add(new JBLabel("Description:"));
        contentPanel.add(descriptionArea);
        contentPanel.add(new JSeparator());
        contentPanel.add(new JBLabel("Labels:"));
        contentPanel.add(labelsPanel);
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(viewInLinearLink);
        
        JBScrollPane scrollPane = new JBScrollPane(contentPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        
        setTask(null);
    }
    
    private JPanel createFieldPanel(String label, JComponent value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);
        JBLabel labelComponent = new JBLabel(label);
        labelComponent.setFont(labelComponent.getFont().deriveFont(Font.BOLD));
        panel.add(labelComponent);
        panel.add(value);
        return panel;
    }
    
    public void setTask(LinearTask task) {
        if (task == null) {
            titleLabel.setText("Select a task to view details");
            identifierLabel.setText("");
            stateLabel.setText("");
            priorityLabel.setText("");
            descriptionArea.setText("");
            viewInLinearLink.setVisible(false);
            labelsPanel.removeAll();
            currentTaskUrl = null;
            return;
        }
        
        titleLabel.setText(task.getTitle());
        identifierLabel.setText(task.getIdentifier());
        
        if (task.getState() != null) {
            stateLabel.setText(task.getState().getName());
            stateLabel.setForeground(JBColor.decode(task.getState().getColor()));
        }
        
        priorityLabel.setText(task.getPriorityLabel());
        descriptionArea.setText(task.getDescription() != null ? task.getDescription() : "No description");
        
        currentTaskUrl = task.getUrl();
        viewInLinearLink.setVisible(true);
        viewInLinearLink.setHyperlinkTarget(task.getUrl());
        
        labelsPanel.removeAll();
        task.getLabelsList().forEach(label -> {
            JLabel labelChip = new JLabel(label.getName());
            labelChip.setFont(labelChip.getFont().deriveFont(11f));
            labelChip.setBorder(JBUI.Borders.empty(2, 8));
            labelChip.setOpaque(true);
            labelChip.setBackground(JBColor.decode(label.getColor()));
            labelChip.setForeground(getContrastColor(JBColor.decode(label.getColor())));
            labelsPanel.add(labelChip);
        });
        
        revalidate();
        repaint();
    }
    
    private Color getContrastColor(Color color) {
        double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
        return luminance > 0.5 ? JBColor.BLACK : JBColor.WHITE;
    }
}