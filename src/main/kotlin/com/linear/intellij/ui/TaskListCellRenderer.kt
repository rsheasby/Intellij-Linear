package com.linear.intellij.ui

import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.linear.intellij.models.LinearTask
import java.awt.*
import javax.swing.*

class TaskListCellRenderer : JPanel(BorderLayout(5, 2)), ListCellRenderer<LinearTask> {
    private val identifierLabel = JBLabel().apply {
        font = font.deriveFont(Font.BOLD)
    }
    private val titleLabel = JBLabel()
    private val stateLabel = JBLabel().apply {
        font = font.deriveFont(11f)
    }
    private val labelsPanel = JPanel(FlowLayout(FlowLayout.LEFT, 4, 0)).apply {
        isOpaque = false
    }
    
    init {
        border = JBUI.Borders.empty(8, 10)
        
        val topPanel = JPanel(BorderLayout()).apply {
            isOpaque = false
            add(identifierLabel, BorderLayout.WEST)
            add(stateLabel, BorderLayout.EAST)
        }
        
        add(topPanel, BorderLayout.NORTH)
        add(titleLabel, BorderLayout.CENTER)
        add(labelsPanel, BorderLayout.SOUTH)
    }
    
    override fun getListCellRendererComponent(
        list: JList<out LinearTask>?,
        task: LinearTask?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        if (isSelected) {
            background = UIUtil.getListSelectionBackground(cellHasFocus)
            foreground = UIUtil.getListSelectionForeground(cellHasFocus)
        } else {
            background = list?.background
            foreground = list?.foreground
        }
        
        task?.let {
            identifierLabel.text = it.identifier
            titleLabel.text = it.title
            
            it.state?.let { state ->
                stateLabel.text = state.name
                stateLabel.foreground = JBColor.decode(state.color)
            }
            
            labelsPanel.removeAll()
            it.getLabelsList().forEach { label ->
                val labelChip = JLabel(label.name).apply {
                    font = font.deriveFont(10f)
                    border = JBUI.Borders.empty(2, 6)
                    isOpaque = true
                    background = JBColor.decode(label.color)
                    foreground = getContrastColor(JBColor.decode(label.color))
                }
                labelsPanel.add(labelChip)
            }
        }
        
        return this
    }
    
    private fun getContrastColor(color: Color): Color {
        val luminance = (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue) / 255
        return if (luminance > 0.5) JBColor.BLACK else JBColor.WHITE
    }
}