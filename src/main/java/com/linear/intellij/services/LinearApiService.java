package com.linear.intellij.services;

import com.intellij.openapi.components.Service;
import com.linear.intellij.api.LinearApiClient;
import com.linear.intellij.models.LinearTask;
import com.linear.intellij.settings.LinearSettings;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public final class LinearApiService {
    private final LinearApiClient apiClient;
    
    public LinearApiService() {
        this.apiClient = new LinearApiClient();
        updateApiKey();
    }
    
    public void updateApiKey() {
        LinearSettings settings = LinearSettings.getInstance();
        apiClient.setApiKey(settings.getApiKey());
    }
    
    public CompletableFuture<List<LinearTask>> fetchAssignedTasks() {
        return apiClient.getAssignedTasks();
    }
    
    public boolean isConfigured() {
        LinearSettings settings = LinearSettings.getInstance();
        String apiKey = settings.getApiKey();
        return apiKey != null && !apiKey.trim().isEmpty();
    }
}