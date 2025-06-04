package com.linear.intellij.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.linear.intellij.services.LinearApiService;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class LinearSettingsConfigurable implements Configurable {
    private JBPasswordField apiKeyField;
    private final LinearSettings settings;
    
    public LinearSettingsConfigurable() {
        this.settings = LinearSettings.getInstance();
    }
    
    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Linear";
    }
    
    @Nullable
    @Override
    public JComponent createComponent() {
        apiKeyField = new JBPasswordField();
        apiKeyField.setColumns(30);
        
        JPanel panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("API Key:"), apiKeyField, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        
        panel.setBorder(JBUI.Borders.empty(10));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JBLabel infoLabel = new JBLabel("<html>To get your Linear API key:<br>" +
                "1. Go to Linear Settings > API > Personal API keys<br>" +
                "2. Create a new key with 'read' scope<br>" +
                "3. Copy and paste it here</html>");
        infoLabel.setForeground(infoLabel.getForeground().darker());
        infoPanel.add(infoLabel);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(infoPanel);
        
        return mainPanel;
    }
    
    @Override
    public boolean isModified() {
        return !StringUtil.equals(settings.getApiKey(), new String(apiKeyField.getPassword()));
    }
    
    @Override
    public void apply() {
        settings.setApiKey(new String(apiKeyField.getPassword()));
        
        LinearApiService apiService = ApplicationManager.getApplication()
                .getService(LinearApiService.class);
        apiService.updateApiKey();
    }
    
    @Override
    public void reset() {
        apiKeyField.setText(settings.getApiKey());
    }
}