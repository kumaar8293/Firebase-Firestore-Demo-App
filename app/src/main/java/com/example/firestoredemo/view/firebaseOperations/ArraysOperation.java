package com.example.firestoredemo.view.firebaseOperations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firestoredemo.R;
import com.example.firestoredemo.model.NoteArrayModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class ArraysOperation extends AppCompatActivity {
    private EditText editTitle, editDesc, editPriority, editTag;
    private TextView noteTextView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference noteCollRef = db.collection("Notebook");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrays_operation);
        editTitle = findViewById(R.id.edit_title);
        editDesc = findViewById(R.id.edit_description);
        noteTextView = findViewById(R.id.note_text_view);
        editPriority = findViewById(R.id.edit_priority);
        editTag = findViewById(R.id.edit_tags);
        setTitle("Array operation");


        removeParticularDataFromArray();
    }

    public void addNote(View view) {
        String title = editTitle.getText().toString();
        String description = editDesc.getText().toString();

        if (editPriority.getText().toString().trim().length() == 0) {
            editPriority.setText("1");
        }
        int priority = Integer.parseInt(editPriority.getText().toString());
        String tag = editTag.getText().toString();
        String[] tagArray = tag.split("\\s*,\\s*");
        List<String> tagList = Arrays.asList(tagArray);
        NoteArrayModel model = new NoteArrayModel(title, description, priority, tagList);

        noteCollRef.add(model);
    }

    public void loadNotes(View view) {
        noteCollRef.whereArrayContains("tags", "tag2")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                StringBuilder data = new StringBuilder();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    NoteArrayModel model = documentSnapshot.toObject(NoteArrayModel.class);
                    model.setDocumentId(documentSnapshot.getId());

                    data.append("ID : ").append(model.getDocumentId());

                    for (String tag : model.getTags()) {
                        data.append("\n-").append(tag);
                    }
                    data.append("\n\n");
                }
                noteTextView.setText(data.toString());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ArraysOperation.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateParticularArrayData() {
        noteCollRef.document("BbJhSNTFRoDw8Loc9De1")
                .update("tags", FieldValue.arrayUnion("new tag1","new tag2"));
    }  private void removeParticularDataFromArray() {
        noteCollRef.document("BbJhSNTFRoDw8Loc9De1")
                .update("tags", FieldValue.arrayRemove("new tag1"));
    }
}
