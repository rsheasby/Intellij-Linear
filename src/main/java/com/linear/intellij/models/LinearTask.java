package com.linear.intellij.models;

import java.util.List;

public class LinearTask {
    private String id;
    private String identifier;
    private String title;
    private String description;
    private LinearState state;
    private int priority;
    private String priorityLabel;
    private String createdAt;
    private String updatedAt;
    private String url;
    private LinearLabels labels;
    
    public static class LinearState {
        private String id;
        private String name;
        private String color;
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getColor() { return color; }
    }
    
    public static class LinearLabel {
        private String id;
        private String name;
        private String color;
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getColor() { return color; }
    }
    
    public static class LinearLabels {
        private List<LinearLabel> nodes;
        
        public List<LinearLabel> getNodes() { return nodes; }
    }
    
    public String getId() { return id; }
    public String getIdentifier() { return identifier; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LinearState getState() { return state; }
    public int getPriority() { return priority; }
    public String getPriorityLabel() { return priorityLabel; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public String getUrl() { return url; }
    public LinearLabels getLabels() { return labels; }
    
    public List<LinearLabel> getLabelsList() {
        return labels != null && labels.getNodes() != null ? labels.getNodes() : List.of();
    }
}