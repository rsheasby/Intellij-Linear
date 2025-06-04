package com.linear.intellij.services

import com.intellij.openapi.components.Service
import com.linear.intellij.api.LinearApiClient
import com.linear.intellij.models.LinearTask
import com.linear.intellij.settings.LinearSettings
import java.util.concurrent.CompletableFuture

@Service
class LinearApiService {
    private val apiClient = LinearApiClient()
    
    init {
        updateApiKey()
    }
    
    fun updateApiKey() {
        val settings = LinearSettings.getInstance()
        apiClient.setApiKey(settings.apiKey)
    }
    
    fun fetchAssignedTasks(): CompletableFuture<List<LinearTask>> {
        return apiClient.getAssignedTasks()
    }
    
    fun isConfigured(): Boolean {
        val settings = LinearSettings.getInstance()
        val apiKey = settings.apiKey
        return !apiKey.isNullOrBlank()
    }
}