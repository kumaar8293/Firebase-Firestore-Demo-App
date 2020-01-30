package com.example.firestoredemo.view.firebaseOperations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firestoredemo.R;
import com.example.firestoredemo.model.NoteSimpleQueryModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/* STEP-6
 * Firebase does not have OR or -= query
 * So we have to run multiple queries (MERGE TASK)
 */
public class CreateOrQueries extends AppCompatActivity {
    private EditText editTitle, editDesc, editPriority;
    private TextView noteTextView;

    private CollectionReference noteCollRef = FirebaseFirestore.getInstance()
            .collection(SimpleQueries.QUERY_TABLE_NAME);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_quries);

        editTitle = findViewById(R.id.edit_title);
        editDesc = findViewById(R.id.edit_description);
        noteTextView = findViewById(R.id.note_text_view);
        editPriority = findViewById(R.id.edit_priority);
        getSupportActionBar();
        setTitle("Merge task");
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
                        Toast.makeText(CreateOrQueries.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(CreateOrQueries.this, "Failed to add", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void loadNote(View view) {
        /*
         * We will create 2 queries  and merge them
         * But the Problem is we don't know which Query will run first
         * so we will use diff - diff methods depends on response
         * If both task completes
         */
        Task task1 = noteCollRef.whereGreaterThan("priority", 2)
                .orderBy("priority").get();
        Task task2 = noteCollRef.whereLessThan("priority", 2)
                .orderBy("priority").get();




        //Added return type
        Task<List<QuerySnapshot>> mergeTask = Tasks.whenAllSuccess(task1, task2);

        mergeTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                StringBuilder data = new StringBuilder();
                for (QuerySnapshot querySnapshot : querySnapshots) {

                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        NoteSimpleQueryModel noteModel = documentSnapshot.toObject(NoteSimpleQueryModel.class);
                        noteModel.setDocumentId(documentSnapshot.getId());
                        data.append("Title ").append(noteModel.getTitle()).append("\nDescription ").append(noteModel.getDescription()).append("\nPriority ").append(noteModel.getPriority()).append("\n\n");
                    }
                }

                noteTextView.setText(data.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                e.printStackTrace();
                Toast.makeText(CreateOrQueries.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
