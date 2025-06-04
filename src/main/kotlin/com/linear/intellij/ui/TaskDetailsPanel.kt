package com.linear.intellij.ui

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.HyperlinkLabel
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.JBUI
import com.linear.intellij.models.LinearTask
import java.awt.*
import javax.swing.*

class TaskDetailsPanel : JPanel(BorderLayout()) {
    private val titleLabel = JBLabel().apply {
        font = font.deriveFont(16f).deriveFont(Font.BOLD)
    }
    private val identifierLabel = JBLabel().apply {
        foreground = JBColor.GRAY
    }
    private val stateLabel = JBLabel()
    private val priorityLabel = JBLabel()
    private val descriptionArea = JBTextArea().apply {
        isEditable = false
        lineWrap = true
        wrapStyleWord = true
        background = this@TaskDetailsPanel.background
    }
    private val viewInLinearLink = HyperlinkLabel("View in Linear").apply {
        icon = null
        addHyperlinkListener {
            currentTaskUrl?.let { BrowserUtil.browse(it) }
        }
    }
    private val labelsPanel = JPanel(FlowLayout(FlowLayout.LEFT, 4, 0)).apply {
        isOpaque = false
    }
    private var currentTaskUrl: String? = null
    
    init {
        border = JBUI.Borders.empty(10)
        
        val contentPanel = JPanel(VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 5, true, false))
        
        contentPanel.apply {
            add(titleLabel)
            add(identifierLabel)
            add(createFieldPanel("State:", stateLabel))
            add(createFieldPanel("Priority:", priorityLabel))
            add(JSeparator())
            add(JBLabel("Description:"))
            add(descriptionArea)
            add(JSeparator())
            add(JBLabel("Labels:"))
            add(labelsPanel)
            add(Box.createVerticalGlue())
            add(viewInLinearLink)
        }
        
        val scrollPane = JBScrollPane(contentPanel).apply {
            border = null
        }
        add(scrollPane, BorderLayout.CENTER)
        
        setTask(null)
    }
    
    private fun createFieldPanel(label: String, value: JComponent): JPanel {
        return JPanel(FlowLayout(FlowLayout.LEFT, 5, 0)).apply {
            isOpaque = false
            val labelComponent = JBLabel(label).apply {
                font = font.deriveFont(Font.BOLD)
            }
            add(labelComponent)
            add(value)
        }
    }
    
    fun setTask(task: LinearTask?) {
        if (task == null) {
            titleLabel.text = "Select a task to view details"
            identifierLabel.text = ""
            stateLabel.text = ""
            priorityLabel.text = ""
            descriptionArea.text = ""
            viewInLinearLink.isVisible = false
            labelsPanel.removeAll()
            currentTaskUrl = null
            return
        }
        
        titleLabel.text = task.title
        identifierLabel.text = task.identifier
        
        task.state?.let { state ->
            stateLabel.text = state.name
            stateLabel.foreground = JBColor.decode(state.color)
        }
        
        priorityLabel.text = task.priorityLabel
        descriptionArea.text = task.description ?: "No description"
        
        currentTaskUrl = task.url
        viewInLinearLink.isVisible = true
        
        labelsPanel.removeAll()
        task.getLabelsList().forEach { label ->
            val labelChip = JLabel(label.name).apply {
                font = font.deriveFont(11f)
                border = JBUI.Borders.empty(2, 8)
                isOpaque = true
                background = JBColor.decode(label.color)
                foreground = getContrastColor(JBColor.decode(label.color))
            }
            labelsPanel.add(labelChip)
        }
        
        revalidate()
        repaint()
    }
    
    private fun getContrastColor(color: Color): Color {
        val luminance = (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue) / 255
        return if (luminance > 0.5) JBColor.BLACK else JBColor.WHITE
    }
}