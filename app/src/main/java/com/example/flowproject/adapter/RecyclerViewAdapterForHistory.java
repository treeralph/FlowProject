package com.example.flowproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flowproject.R;

import java.util.ArrayList;

public class RecyclerViewAdapterForHistory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> dataList;
    Context context;

    public RecyclerViewAdapterForHistory(ArrayList<String> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new RecyclerViewAdapterForHistory.HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        HistoryViewHolder viewHolder = (HistoryViewHolder) holder;

        String data = dataList.get(dataList.size() - position - 1);
        viewHolder.historyItemTextView.setText(data);
        viewHolder.historyItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("searchTarget", data);
                ((Activity) context).setResult(Activity.RESULT_OK, intent);
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder{

        CardView historyItemCardView;
        TextView historyItemTextView;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyItemCardView = itemView.findViewById(R.id.historyItemCardView);
            historyItemTextView = itemView.findViewById(R.id.historyItemTextView);
        }
    }
}
