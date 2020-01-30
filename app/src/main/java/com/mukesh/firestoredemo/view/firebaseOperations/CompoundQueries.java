package com.mukesh.firestoredemo.view.firebaseOperations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.firestoredemo.R;
import com.mukesh.firestoredemo.model.NoteSimpleQueryModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

/** STEP-5
 * In this activity, we are simply observing the DOC changes
 * We are using NoteSimpleQueryModel
 *  In this class we are using
 * .whereGreaterThanOrEqualTo("priority", 1)
 * .orderBy("priority")
 * .orderBy("title")
 */


public class CompoundQueries extends AppCompatActivity {
    private EditText editTitle, editDesc, editPriority;
    private TextView noteTextView;

    private CollectionReference noteCollRef = FirebaseFirestore
            .getInstance().collection(SimpleQueries.QUERY_TABLE_NAME);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compound_quries);
        editTitle = findViewById(R.id.edit_title);
        editDesc = findViewById(R.id.edit_description);
        noteTextView = findViewById(R.id.note_text_view);
        editPriority = findViewById(R.id.edit_priority);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteCollRef.whereEqualTo("priority", 1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(CompoundQueries.this, "Failed to add listener", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuilder data = new StringBuilder();
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        NoteSimpleQueryModel noteModel = documentSnapshot.toObject(NoteSimpleQueryModel.class);
                        noteModel.setDocumentId(documentSnapshot.getId());
                        data.append("Title ").append(noteModel.getTitle()).append("\nDescription ").append(noteModel.getDescription()).append("\nPriority ").append(noteModel.getPriority()).append("\n\n");
                    }
                }
                noteTextView.setText(data.toString());
            }
        });
    }

    public void addNote(View view) {

        if (editTitle.getText().toString().trim().length() == 0 || editDesc.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show();

            return;
        }
        if (editPriority.getText().toString().length() == 0)
            editPriority.setText("0");
        int priority = Integer.parseInt(editPriority.getText().toString());
        NoteSimpleQueryModel model = new NoteSimpleQueryModel(editTitle.getText().toString(), editDesc.getText().toString(), priority);

        noteCollRef.add(model)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CompoundQueries.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(CompoundQueries.this, "Failed to add", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void loadNote(View view) {
        noteCollRef
                .whereGreaterThanOrEqualTo("priority", 1)
                .orderBy("priority")
                .orderBy("title")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                StringBuilder data = new StringBuilder();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    NoteSimpleQueryModel noteModel = documentSnapshot.toObject(NoteSimpleQueryModel.class);
                    noteModel.setDocumentId(documentSnapshot.getId());
                    data.append("Title ").append(noteModel.getTitle()).append("\nDescription ").append(noteModel.getDescription()).append("\nPriority ").append(noteModel.getPriority()).append("\n\n");
                }
                noteTextView.setText(data.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(CompoundQueries.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
