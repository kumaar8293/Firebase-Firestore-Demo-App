package com.example.firestoredemo.view.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestoredemo.R;
import com.example.firestoredemo.view.firebaseOperations.ArraysOperation;
import com.example.firestoredemo.view.firebaseOperations.BatchWrites;
import com.example.firestoredemo.view.firebaseOperations.CompoundQueries;
import com.example.firestoredemo.view.firebaseOperations.CreateOrQueries;
import com.example.firestoredemo.view.firebaseOperations.DocumentChanges;
import com.example.firestoredemo.view.firebaseOperations.JsonArrayData;
import com.example.firestoredemo.view.firebaseOperations.multiDoc.MultiDocOperation;
import com.example.firestoredemo.view.firebaseOperations.PaginationInFirestore;
import com.example.firestoredemo.view.firebaseOperations.SimpleQueries;
import com.example.firestoredemo.view.firebaseOperations.SingleDocRefOperation;
import com.example.firestoredemo.view.firebaseOperations.Transactions;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a Simple class having list of buttons related to a particular databse operation
 * Each class having own description
 */
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        prepareData();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        HomeAdapter adapter = new HomeAdapter(nameList) {
            @Override
            public void customOnClick(int position) {
                openRelatedActivity(position);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void openRelatedActivity(int position) {

        switch (position) {
            case 0:
                openActivity(SingleDocRefOperation.class);
                break;
            case 1:
                openActivity(MultiDocOperation.class);
                break;
            case 2:
                openActivity(DocumentChanges.class);
                break;
            case 3:
                openActivity(SimpleQueries.class);
                break;
            case 4:
                openActivity(CompoundQueries.class);
                break;
            case 5:
                openActivity(CreateOrQueries.class);
                break;
            case 6:
                openActivity(PaginationInFirestore.class);
                break;
            case 7:
                openActivity(BatchWrites.class);
                break;
            case 8:
                openActivity(Transactions.class);
                break;
            case 9:
                openActivity(ArraysOperation.class);
                break;
            case 10:
                openActivity(JsonArrayData.class);
                break;

        }
    }

    private void openActivity(Class<?> className) {
        startActivity(new Intent(HomeActivity.this, className));
    }

    private void prepareData() {
        nameList = new ArrayList<>();
        nameList.add("Single Doc Ref Operation");
        nameList.add("Multi Doc Operation");
        nameList.add("Document Changes");
        nameList.add("Simple Query");
        nameList.add("Compound Query");
        nameList.add("Create Or Queries");
        nameList.add("Pagination");
        nameList.add("Batch writes");
        nameList.add("Transactions");
        nameList.add("ArrayOperation");
        nameList.add("Nested Data operation (JSON)");
    }
}
