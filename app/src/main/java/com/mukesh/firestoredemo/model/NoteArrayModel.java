package com.mukesh.firestoredemo.model;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class NoteArrayModel {
    private String title;
    private String description;
    private int priority;
    private String documentId;
    private List<String> tags;

    public NoteArrayModel() {
        //Default constructor needed
    }

    public NoteArrayModel(String title, String description, int priority, List<String> tags) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.tags = tags;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

    public List<String> getTags() {
        return tags;
    }
}
