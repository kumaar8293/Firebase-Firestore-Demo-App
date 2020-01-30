package com.mukesh.firestoredemo.view.firebaseOperations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.firestoredemo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class Transactions extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference noteCollRef = db.collection(SimpleQueries.QUERY_TABLE_NAME);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        /* When using transactions, note that:

         * Read operations must come before write operations.
         * A function calling a transaction (transaction function) might run more
         * than once if a concurrent edit affects a document that the transaction reads.
         * Transaction functions should not directly modify application state.
         * Transactions will fail when the client is offline.
         */
        EditText editTitle = findViewById(R.id.edit_title);
        EditText editDesc = findViewById(R.id.edit_description);
        TextView noteTextView = findViewById(R.id.note_text_view);
        EditText editPriority = findViewById(R.id.edit_priority);
        setTitle("Transactions ");


    }

    public void addNote(View view) {

        executeTransactionOperation();
    }

    private void executeTransactionOperation() {


        db.runTransaction(new Transaction.Function<Long>() {
            @Nullable
            @Override
            public Long apply(@NonNull Transaction transaction) throws
                    FirebaseFirestoreException {

                DocumentReference documentReference =
                        noteCollRef.document(SimpleQueries.QUERY_TABLE_NAME);

                DocumentSnapshot documentSnapshot = transaction.get(documentReference);
                //It will increase priority with 1
                long newPriority = documentSnapshot.getLong("priority") + 1;

                transaction.update(documentReference, "priority", newPriority);

                return newPriority;
            }
        }).addOnSuccessListener(new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long result) {
                Toast.makeText(Transactions.this, "New priority " + result, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Transactions.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void loadNotes(View view) {
    }
}
