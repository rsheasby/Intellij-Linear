package com.linear.intellij.ui;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.linear.intellij.models.LinearTask;

import javax.swing.*;
import java.awt.*;

public class TaskListCellRenderer extends JPanel implements ListCellRenderer<LinearTask> {
    private final JBLabel identifierLabel;
    private final JBLabel titleLabel;
    private final JBLabel stateLabel;
    private final JPanel labelsPanel;
    
    public TaskListCellRenderer() {
        setLayout(new BorderLayout(5, 2));
        setBorder(JBUI.Borders.empty(8, 10));
        
        identifierLabel = new JBLabel();
        identifierLabel.setFont(identifierLabel.getFont().deriveFont(Font.BOLD));
        
        titleLabel = new JBLabel();
        
        stateLabel = new JBLabel();
        stateLabel.setFont(stateLabel.getFont().deriveFont(11f));
        
        labelsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        labelsPanel.setOpaque(false);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(identifierLabel, BorderLayout.WEST);
        topPanel.add(stateLabel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        add(titleLabel, BorderLayout.CENTER);
        add(labelsPanel, BorderLayout.SOUTH);
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends LinearTask> list, 
                                                  LinearTask task, 
                                                  int index, 
                                                  boolean isSelected, 
                                                  boolean cellHasFocus) {
        if (isSelected) {
            setBackground(UIUtil.getListSelectionBackground(cellHasFocus));
            setForeground(UIUtil.getListSelectionForeground(cellHasFocus));
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        identifierLabel.setText(task.getIdentifier());
        titleLabel.setText(task.getTitle());
        
        if (task.getState() != null) {
            stateLabel.setText(task.getState().getName());
            stateLabel.setForeground(JBColor.decode(task.getState().getColor()));
        }
        
        labelsPanel.removeAll();
        task.getLabelsList().forEach(label -> {
            JLabel labelChip = new JLabel(label.getName());
            labelChip.setFont(labelChip.getFont().deriveFont(10f));
            labelChip.setBorder(JBUI.Borders.empty(2, 6));
            labelChip.setOpaque(true);
            labelChip.setBackground(JBColor.decode(label.getColor()));
            labelChip.setForeground(getContrastColor(JBColor.decode(label.getColor())));
            labelsPanel.add(labelChip);
        });
        
        return this;
    }
    
    private Color getContrastColor(Color color) {
        double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
        return luminance > 0.5 ? JBColor.BLACK : JBColor.WHITE;
    }
}