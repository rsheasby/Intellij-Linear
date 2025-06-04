package com.linear.intellij.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.linear.intellij.models.LinearTask
import com.linear.intellij.models.LinearUser
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.CompletableFuture

class LinearApiClient {
    companion object {
        private const val API_URL = "https://api.linear.app/graphql"
        private val JSON = "application/json; charset=utf-8".toMediaType()
    }
    
    private val client = OkHttpClient()
    private val gson = Gson()
    private var apiKey: String? = null
    
    fun setApiKey(apiKey: String) {
        this.apiKey = apiKey
    }
    
    fun getCurrentUser(): CompletableFuture<LinearUser> {
        val query = "{ viewer { id name email } }"
        
        return executeQuery(query).thenApply { response ->
            val data = response.getAsJsonObject("data")
            val viewer = data.getAsJsonObject("viewer")
            gson.fromJson(viewer, LinearUser::class.java)
        }
    }
    
    fun getAssignedTasks(): CompletableFuture<List<LinearTask>> {
        val query = """
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
        """.trimIndent()
        
        return executeQuery(query).thenApply { response ->
            val tasks = mutableListOf<LinearTask>()
            val data = response.getAsJsonObject("data")
            val viewer = data.getAsJsonObject("viewer")
            val assignedIssues = viewer.getAsJsonObject("assignedIssues")
            
            assignedIssues.getAsJsonArray("nodes").forEach { element ->
                val task = gson.fromJson(element, LinearTask::class.java)
                tasks.add(task)
            }
            
            tasks
        }
    }
    
    private fun executeQuery(query: String): CompletableFuture<JsonObject> {
        val future = CompletableFuture<JsonObject>()
        
        val requestBody = JsonObject().apply {
            addProperty("query", query)
        }
        
        val request = Request.Builder()
            .url(API_URL)
            .header("Authorization", apiKey ?: "")
            .header("Content-Type", "application/json")
            .post(requestBody.toString().toRequestBody(JSON))
            .build()
        
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                future.completeExceptionally(e)
            }
            
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    future.completeExceptionally(IOException("Unexpected response: $response"))
                    return
                }
                
                val responseBody = response.body?.string()
                val jsonResponse = gson.fromJson(responseBody, JsonObject::class.java)
                future.complete(jsonResponse)
            }
        })
        
        return future
    }
}