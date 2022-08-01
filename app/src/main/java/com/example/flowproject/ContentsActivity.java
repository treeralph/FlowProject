package com.example.flowproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.flowproject.DatabaseManager.SQLiteManager;
import com.example.flowproject.adapter.RecyclerViewAdapterForContents;
import com.example.flowproject.tool.Callback;
import com.example.flowproject.tool.NaverMovieAPI;

import org.json.JSONArray;
import org.json.JSONObject;

public class ContentsActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private int REQUEST_CODE_TO_HISTORYACTIVITY = 0;

    EditText searchEditText;
    ImageView searchButton;
    ImageView historyButton;
    RecyclerView contentsRecyclerView;

    RecyclerViewAdapterForContents adapter;
    Handler handler;
    NaverMovieAPI movieAPI;

    SQLiteManager sqLiteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        searchEditText = findViewById(R.id.MainActivitySearchEditText);
        searchButton = findViewById(R.id.MainActivitySearchButton);
        historyButton = findViewById(R.id.MainActivityHistoryButton);
        contentsRecyclerView = findViewById(R.id.MainActivityContentsRecyclerView);

        movieAPI = new NaverMovieAPI();

        sqLiteManager = new SQLiteManager(this);

        contentsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        contentsRecyclerView.setLayoutManager(layoutManager);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchQuery = searchEditText.getText().toString();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        movieAPI.request(searchQuery, new Callback() {
                            @Override
                            public void OnCallback(Object object) {
                                JSONObject jsonObject = (JSONObject) object;
                                Message message = handler.obtainMessage();
                                message.obj = jsonObject;
                                handler.sendMessage(message);
                            }
                        });
                    }
                });
                thread.start();

                sqLiteManager.write(searchQuery, new Callback() {
                    @Override
                    public void OnCallback(Object object) {

                    }
                });
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentsActivity.this, HistoryActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TO_HISTORYACTIVITY);

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                JSONObject jsonObject = (JSONObject) message.obj;
                try {
                    JSONArray child = jsonObject.getJSONArray("items");
                    adapter = new RecyclerViewAdapterForContents(child, ContentsActivity.this);
                    contentsRecyclerView.setAdapter(adapter);
                }catch(Exception e){
                    Log.e(TAG, e.getMessage());
                }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_TO_HISTORYACTIVITY){
            if(resultCode == RESULT_OK){
                String searchTarget = data.getStringExtra("searchTarget");
                searchEditText.setText(searchTarget);
                searchButton.performClick();
            }
        }
    }
}