package com.example.firestoredemo.view.firebaseOperations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firestoredemo.R;
import com.example.firestoredemo.model.JSONArrayModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class JsonArrayData extends AppCompatActivity {
    private EditText editTitle, editDesc, editPriority, editTag;
    private TextView noteTextView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference noteCollRef = db.collection("NestedDataTable");
    /*
     * We can access fields of objects or maps within a document with "dot notation",
     * where we chain the keys of hierarchical fields together to reach the nested value.
     * We can modify and query these values the same as top level fields with update, FieldValue.delete and so on.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_array_data);

        editTitle = findViewById(R.id.edit_title);
        editDesc = findViewById(R.id.edit_description);
        noteTextView = findViewById(R.id.note_text_view);
        editPriority = findViewById(R.id.edit_priority);
        editTag = findViewById(R.id.edit_tags);
        setTitle("Nested Object");


        deleteNestedValue();
       // updateNestedValue();
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

        Map<String, Object> tags = new HashMap<>();

        for (String tagData : tagArray) {
            tags.put(tagData, true);
        }

        JSONArrayModel model = new JSONArrayModel(title, description, priority, tags);

        noteCollRef.add(model);
    }


    public void loadNotes(View view) {
        // noteCollRef.whereArrayContains("tags", "tag2")

        noteCollRef.whereEqualTo("tags.tag1", true).get()
                .addOnSuccessListener(
                        new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                StringBuilder data = new StringBuilder();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    JSONArrayModel model = documentSnapshot.toObject(JSONArrayModel.class);
                    model.setDocumentId(documentSnapshot.getId());

                    data.append("ID : ").append(model.getDocumentId());

                    for (String tag : model.getTags().keySet()) {
                        data.append("\n-").append(tag);
                    }
                    data.append("\n\n");
                }
                noteTextView.setText(data.toString());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(JsonArrayData.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNestedValue() {

        noteCollRef.document("uKmrFZXX0O94uKdTlS2o")
                .update("tags.tag1", false);

    }private void deleteNestedValue() {

        noteCollRef.document("uKmrFZXX0O94uKdTlS2o")
                .update("tags.tag1", FieldValue.delete());

    }
}
