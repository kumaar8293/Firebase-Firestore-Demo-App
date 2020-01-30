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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

/** STEP-3
 * In this activity, we are simply observing the DOC changes
 * We are using NoteSimpleQueryModel but Priority is useless in this class
 */
public class DocumentChanges extends AppCompatActivity {
    private EditText editTitle, editDesc, editPriority;
    private TextView noteTextView;
    private CollectionReference noteCollRef = FirebaseFirestore.getInstance()
            .collection("Document Change Table");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_changes);

        editTitle = findViewById(R.id.edit_title);
        editDesc = findViewById(R.id.edit_description);
        noteTextView = findViewById(R.id.note_text_view);
        editPriority = findViewById(R.id.edit_priority);

        setTitle("Document Changes");

        findViewById(R.id.load_notes).setVisibility(View.GONE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        noteCollRef.addSnapshotListener(this,
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }

                        if (queryDocumentSnapshots != null) {
                            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                                DocumentSnapshot documentSnapshot = dc.getDocument();
                                NoteSimpleQueryModel note = documentSnapshot.toObject
                                        (NoteSimpleQueryModel.class);
                                note.setDocumentId(documentSnapshot.getId());
                                int oldIndex = dc.getOldIndex();
                                int newIndex = dc.getNewIndex();

                                switch (dc.getType()) {
                                    case ADDED:
                                        noteTextView.append("Title : " + note.getTitle()
                                                + "\nDescription : " + note.getDescription()
                                                + "\nAdded : " + note.getDocumentId()
                                                + "\nOld index " + oldIndex
                                                + "\nNew index " + newIndex + "\n\n");
                                        break;
                                    case MODIFIED:
                                        noteTextView.append("Title : " + note.getTitle()
                                                + "\nDescription : " + note.getDescription()
                                                + "\nModified : " + note.getDocumentId()
                                                + "\nOld index " + oldIndex
                                                + "\nNew index " + newIndex + "\n\n");
                                        break;
                                    case REMOVED:
                                        noteTextView.append("Title : " + note.getTitle()
                                                + "\nDescription : " + note.getDescription() +
                                                "\nRemoved : " + note.getDocumentId()
                                                + "\nOld index " + oldIndex
                                                + "\nNew index " + newIndex + "\n\n");
                                        break;

                                }

                            }
                        }

                    }
                });
    }

    public void addNote(View view) {

        String title = editTitle.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();


        if (title.length() == 0 || desc.length() == 0) {
            Toast.makeText(this, "Please enter title and description",
                    Toast.LENGTH_SHORT).show();

            return;
        }
        if (editPriority.getText().toString().length() == 0)
            editPriority.setText("1");
        int priority = Integer.parseInt(editPriority.getText().toString());
        NoteSimpleQueryModel model = new NoteSimpleQueryModel
                (editTitle.getText().toString(), editDesc.getText().toString(), priority);

        noteCollRef.add(model)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(DocumentChanges.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(DocumentChanges.this, "Failed to add", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void loadNote(View view) {


    }
}
