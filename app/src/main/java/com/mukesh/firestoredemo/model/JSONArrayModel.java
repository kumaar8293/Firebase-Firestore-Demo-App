package com.mukesh.firestoredemo.model;

import com.google.firebase.firestore.Exclude;

import java.util.Map;

public class JSONArrayModel {

    private String title;
    private String description;
    private int priority;
    private String documentId;
    private Map<String, Object> tags;

    public JSONArrayModel() {
    }

    public JSONArrayModel(String title, String description, int priority, Map<String, Object> tags) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public Map<String, Object> getTags() {
        return tags;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
