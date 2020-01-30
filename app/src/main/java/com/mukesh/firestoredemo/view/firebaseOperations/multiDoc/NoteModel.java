package com.mukesh.firestoredemo.view.firebaseOperations.multiDoc;

public class NoteModel {
    private String title;
    private String description;
    private String documentId;
    private int priority;

     String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public NoteModel() {
    }

     NoteModel(String title, String description) {
        this.title = title;
        this.description = description;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
