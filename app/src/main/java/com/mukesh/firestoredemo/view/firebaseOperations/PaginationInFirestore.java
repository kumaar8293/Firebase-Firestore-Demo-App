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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * STEP-6
 * Pagination in Firestore database
 */
public class PaginationInFirestore extends AppCompatActivity {
    private EditText editTitle, editDesc, editPriority;
    private TextView noteTextView;
    private CollectionReference noteCollRef = FirebaseFirestore.getInstance()
            .collection(SimpleQueries.QUERY_TABLE_NAME);

    private DocumentSnapshot lastResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pegination_in_firestore);

        editTitle = findViewById(R.id.edit_title);
        editDesc = findViewById(R.id.edit_description);
        noteTextView = findViewById(R.id.note_text_view);
        editPriority = findViewById(R.id.edit_priority);
        getSupportActionBar();
        setTitle("Pagination");
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
                .addOnSuccessListener(
                        new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(PaginationInFirestore.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(
                        new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(PaginationInFirestore.this, "Failed to add", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void loadNote(View view) {
 /*     This will return all documents from priority 3 but if there is
       Multiple doc with priority 3 , it will return only who have title (Title2) then will continue with priority 4.5.6....
   noteCollRef.orderBy("priority")
                .orderBy("title")
                .startAt(3,"Title2")
                .get()
*/
        /*
        * This will return on the Doc from priority 3 and above
         *  noteCollRef.orderBy("priority")
                .startAt(3)
                .get()
         *
         */

        final Query query;
        if (lastResult == null) {
            query = noteCollRef.
                    orderBy("priority")
                    .limit(3);
        } else {
            query = noteCollRef.orderBy("priority")
                    .startAfter(lastResult)
                    .limit(3);
        }
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        StringBuilder data = new StringBuilder();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            NoteSimpleQueryModel noteModel = documentSnapshot.toObject(NoteSimpleQueryModel.class);
                            noteModel.setDocumentId(documentSnapshot.getId());
                            data.append("Title ").append(noteModel.getTitle()).append("\nDescription ").append(noteModel.getDescription()).append("\nPriority ").append(noteModel.getPriority()).append("\n\n");
                        }
                        if (queryDocumentSnapshots.size() > 0) {
                            data.append("--------------------\n\n");
                            noteTextView.append(data.toString());
                            lastResult = queryDocumentSnapshots.getDocuments()
                                    .get(queryDocumentSnapshots.size() - 1);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(PaginationInFirestore.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
