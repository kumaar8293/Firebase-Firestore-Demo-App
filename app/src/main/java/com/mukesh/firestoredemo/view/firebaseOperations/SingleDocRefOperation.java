package com.mukesh.firestoredemo.view.firebaseOperations;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mukesh.firestoredemo.R;
import com.mukesh.firestoredemo.utils.CommonMethods;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * STEP -1
 * In this Activity we are performing Add/Update/Delete/Load
 * operation on a Single Document
 * In this class Document id is fixed = DOC_REF (My note)
 * We are not using any POJO class in this Activity
 */
public class SingleDocRefOperation extends AppCompatActivity {
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String COLLECTION_REF = "MyNotes";
    public static final String DOC_REF = "Single Doc Notes";

    //Creating Firestore Doc reference
    private DocumentReference noteDocRef = FirebaseFirestore.getInstance()
            .collection(COLLECTION_REF).document(DOC_REF);
    private EditText editTitle, editDesc;
    private TextView noteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTitle = findViewById(R.id.edit_title);
        editDesc = findViewById(R.id.edit_description);
        noteTextView = findViewById(R.id.note_text_view);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Snapshot listener works as a socket event.
        //It's get called automatically if any changes happens in the Document reference
        noteDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                String title = "";
                String dec = "";

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    title = documentSnapshot.getString(TITLE);
                    dec = documentSnapshot.getString(DESCRIPTION);
                }
                noteTextView.setText(("Title : " + title + "\nDescription : " + dec));
            }
        });

    }

    private boolean isNotValidInput() {

        if (editTitle.getText().toString().trim().length() == 0 ||
                editDesc.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show();

            return true;
        }
        return false;
    }

    public void addNote(View view) {

        CommonMethods.hideKeyboard(this);
        if (isNotValidInput()) {
            return;
        }
        Map<String, Object> data = new HashMap<>();

        data.put(TITLE, editTitle.getText().toString().trim());
        data.put(DESCRIPTION, editDesc.getText().toString().trim());
        noteDocRef.set(data).addOnSuccessListener(this, new
                OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(SingleDocRefOperation.this, "Added Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SingleDocRefOperation.this, "Failed to add", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    public void loadNote(View view) {

        noteDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String title = "";
                String dec = "";
                if (documentSnapshot != null && documentSnapshot.exists()) {

                    title = documentSnapshot.getString(TITLE);
                    dec = documentSnapshot.getString(DESCRIPTION);
                }
                noteTextView.setText(("Title : " + title + "\nDescription : " + dec));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(SingleDocRefOperation.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteNote(View view) {

        noteDocRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SingleDocRefOperation.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(SingleDocRefOperation.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    public void updateDescription(View view) {


        CommonMethods.hideKeyboard(this);
        if (isNotValidInput()) {
            return;
        }

        // we can  also use

       /* Map<String, Object> data = new HashMap<>();
        data.put(DESCRIPTION, editDesc.getText().toString().trim());
        noteDocRef.set(data, SetOptions.merge());*/


        noteDocRef.update(DESCRIPTION, editDesc.getText().toString().trim())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SingleDocRefOperation.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(SingleDocRefOperation.this, "Failed to update", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
