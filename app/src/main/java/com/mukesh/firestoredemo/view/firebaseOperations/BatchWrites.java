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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

public class BatchWrites extends AppCompatActivity {
    private EditText editTitle, editDesc, editPriority;
    private CollectionReference noteCollRef = FirebaseFirestore.getInstance()
            .collection(SimpleQueries.QUERY_TABLE_NAME);
    //IF any operation fail none of the operation will be performed

    /*
     *Each batch of writes can write to a maximum of 500 documents.
     * if you do not need to read any documents in your operation set,
     * you can execute multiple write operations as a single batch that contains any
     * combination of set(), update(), or delete() operations. A batch of writes completes
     * automatically and can write to multiple documents.
     * A batched write can contain up to 500 operations. Each operation in the batch counts
     * separately towards your Cloud Firestore usage.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_writes);
        editTitle = findViewById(R.id.edit_title);
        editDesc = findViewById(R.id.edit_description);
        TextView noteTextView = findViewById(R.id.note_text_view);
        editPriority = findViewById(R.id.edit_priority);
        setTitle("Batch writes");


        exicuteWriteBatchOperation();
    }

    private void exicuteWriteBatchOperation() {
        // Get a new write batch
        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        DocumentReference addRef = noteCollRef.document("New Note");
        batch.set(addRef, new NoteSimpleQueryModel("New Note", "New note des", 1));

        DocumentReference updateRef = noteCollRef.document("tqAJPdZMWgDjBwUQCFMw");
        batch.update(updateRef, "priority", 100);


        DocumentReference deleteRef = noteCollRef.document("SCXyqbbm89vjEkviFbPh");
        batch.delete(deleteRef);

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(BatchWrites.this, "Success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();

                Toast.makeText(BatchWrites.this, "Failed to perform", Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void addNote(View view) {

      /*  if (editTitle.getText().toString().trim().length() == 0 || editDesc.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show();

            return;
        }*/
        if (editPriority.getText().toString().length() == 0)
            editPriority.setText("1");
        int priority = Integer.parseInt(editPriority.getText().toString());
        NoteSimpleQueryModel model = new NoteSimpleQueryModel(editTitle.getText().toString(), editDesc.getText().toString(), priority);

        noteCollRef.add(model)
                .addOnSuccessListener(
                        new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(BatchWrites.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(BatchWrites.this, "Failed to add", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
