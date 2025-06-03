package com.linear.intellij.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.linear.intellij.models.LinearTask;
import com.linear.intellij.models.LinearUser;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LinearApiClient {
    private static final String API_URL = "https://api.linear.app/graphql";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final OkHttpClient client;
    private final Gson gson;
    private String apiKey;
    
    public LinearApiClient() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public CompletableFuture<LinearUser> getCurrentUser() {
        String query = "{ viewer { id name email } }";
        
        return executeQuery(query).thenApply(response -> {
            JsonObject data = response.getAsJsonObject("data");
            JsonObject viewer = data.getAsJsonObject("viewer");
            return gson.fromJson(viewer, LinearUser.class);
        });
    }
    
    public CompletableFuture<List<LinearTask>> getAssignedTasks() {
        String query = """
            {
                viewer {
                    assignedIssues {
                        nodes {
                            id
                            identifier
                            title
                            description
                            state {
                                id
                                name
                                color
                            }
                            priority
                            priorityLabel
                            createdAt
                            updatedAt
                            url
                            labels {
                                nodes {
                                    id
                                    name
                                    color
                                }
                            }
                        }
                    }
                }
            }
            """;
        
        return executeQuery(query).thenApply(response -> {
            List<LinearTask> tasks = new ArrayList<>();
            JsonObject data = response.getAsJsonObject("data");
            JsonObject viewer = data.getAsJsonObject("viewer");
            JsonObject assignedIssues = viewer.getAsJsonObject("assignedIssues");
            
            assignedIssues.getAsJsonArray("nodes").forEach(element -> {
                LinearTask task = gson.fromJson(element, LinearTask.class);
                tasks.add(task);
            });
            
            return tasks;
        });
    }
    
    private CompletableFuture<JsonObject> executeQuery(String query) {
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("query", query);
        
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", apiKey)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toString(), JSON))
                .build();
        
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    future.completeExceptionally(new IOException("Unexpected response: " + response));
                    return;
                }
                
                String responseBody = response.body().string();
                JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                future.complete(jsonResponse);
            }
        });
        
        return future;
    }
}