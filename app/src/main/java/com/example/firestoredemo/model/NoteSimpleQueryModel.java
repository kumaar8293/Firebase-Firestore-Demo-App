package com.example.firestoredemo.model;

public class NoteSimpleQueryModel {

    private String title;
    private String description;
    private String documentId;
    private int priority;

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public NoteSimpleQueryModel() {
        //Default Constructor is mandatory  for firebase
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDocumentId() {
        return documentId;
    }

    public int getPriority() {
        return priority;
    }

    public NoteSimpleQueryModel(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }
}
