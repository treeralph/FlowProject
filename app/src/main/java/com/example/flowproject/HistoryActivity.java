package com.example.flowproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.example.flowproject.DatabaseManager.SQLiteManager;
import com.example.flowproject.adapter.RecyclerViewAdapterForHistory;
import com.example.flowproject.tool.Callback;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    RecyclerViewAdapterForHistory adapter;

    SQLiteManager sqLiteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.HistoryActivityRecyclerView);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        sqLiteManager = new SQLiteManager(this);
        sqLiteManager.read(new Callback() {
            @Override
            public void OnCallback(Object object) {
                ArrayList<String> dataList = (ArrayList<String>) object;
                adapter = new RecyclerViewAdapterForHistory(dataList, HistoryActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}