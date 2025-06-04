package com.linear.intellij.models

data class LinearTask(
    val id: String,
    val identifier: String,
    val title: String,
    val description: String?,
    val state: LinearState?,
    val priority: Int,
    val priorityLabel: String,
    val createdAt: String,
    val updatedAt: String,
    val url: String,
    val labels: LinearLabels?
) {
    data class LinearState(
        val id: String,
        val name: String,
        val color: String
    )
    
    data class LinearLabel(
        val id: String,
        val name: String,
        val color: String
    )
    
    data class LinearLabels(
        val nodes: List<LinearLabel>?
    )
    
    fun getLabelsList(): List<LinearLabel> {
        return labels?.nodes ?: emptyList()
    }
}