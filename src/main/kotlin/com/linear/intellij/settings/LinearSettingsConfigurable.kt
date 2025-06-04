package com.linear.intellij.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.util.text.StringUtil
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPasswordField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import com.linear.intellij.services.LinearApiService
import javax.swing.*

class LinearSettingsConfigurable : Configurable {
    private var apiKeyField: JBPasswordField? = null
    private val settings = LinearSettings.getInstance()
    
    override fun getDisplayName(): String {
        return "Linear"
    }
    
    override fun createComponent(): JComponent? {
        apiKeyField = JBPasswordField().apply {
            columns = 30
        }
        
        val panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("API Key:"), apiKeyField!!, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
        
        panel.border = JBUI.Borders.empty(10)
        
        val infoPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            val infoLabel = JBLabel("""
                <html>To get your Linear API key:<br>
                1. Go to Linear Settings > API > Personal API keys<br>
                2. Create a new key with 'read' scope<br>
                3. Copy and paste it here</html>
            """.trimIndent())
            infoLabel.foreground = infoLabel.foreground.darker()
            add(infoLabel)
        }
        
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(panel)
            add(Box.createVerticalStrut(20))
            add(infoPanel)
        }
    }
    
    override fun isModified(): Boolean {
        return !StringUtil.equals(settings.apiKey, String(apiKeyField?.password ?: charArrayOf()))
    }
    
    override fun apply() {
        settings.apiKey = String(apiKeyField?.password ?: charArrayOf())
        
        val apiService = ApplicationManager.getApplication()
            .getService(LinearApiService::class.java)
        apiService.updateApiKey()
    }
    
    override fun reset() {
        apiKeyField?.text = settings.apiKey
    }
}