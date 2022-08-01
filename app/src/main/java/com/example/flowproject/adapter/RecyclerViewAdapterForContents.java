package com.example.flowproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flowproject.ContentActivity;
import com.example.flowproject.ContentsActivity;
import com.example.flowproject.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class RecyclerViewAdapterForContents extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "RecyclerViewAdapterForContents";

    JSONArray dataList;
    Context context;

    public RecyclerViewAdapterForContents(JSONArray dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contents_item, parent, false);
        return new ContentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ContentsViewHolder viewHolder = (ContentsViewHolder) holder;
        try{
             JSONObject jsonObject = (JSONObject) dataList.get(position);
             String title = jsonObject.getString("title");
             String imageUri = jsonObject.getString("image");
             String release = jsonObject.getString("pubDate");
             String userRating = jsonObject.getString("userRating");
             String link = jsonObject.getString("link");

             String refinedTitle = Jsoup.parse(title).text();

             Uri uri = Uri.parse(imageUri);
             viewHolder.contentsItemTitleTextView.setText(refinedTitle);
             viewHolder.contentsItemRateTextView.setText(userRating);
             viewHolder.contentsItemReleaseTextView.setText(release);
             Glide.with(context).load(uri).into(viewHolder.contentsItemImageView);

             viewHolder.contentsItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                    Intent intent = new Intent(context, ContentActivity.class);
                    intent.putExtra("link", link);
                    context.startActivity(intent);
                 }
             });

        }catch(Exception e){
            Log.e(TAG, e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return dataList.length();
    }

    public static class ContentsViewHolder extends RecyclerView.ViewHolder{

        LinearLayout contentsItemLinearLayout;
        ImageView contentsItemImageView;
        TextView contentsItemTitleTextView;
        TextView contentsItemReleaseTextView;
        TextView contentsItemRateTextView;

        public ContentsViewHolder(@NonNull View itemView) {
            super(itemView);
            contentsItemLinearLayout = itemView.findViewById(R.id.contentsItemLinearLayout);
            contentsItemImageView = itemView.findViewById(R.id.contentsItemImageView);
            contentsItemTitleTextView = itemView.findViewById(R.id.contentsItemTitleTextView);
            contentsItemReleaseTextView = itemView.findViewById(R.id.contentsItemReleaseTextView);
            contentsItemRateTextView = itemView.findViewById(R.id.contentsItemRateTextView);
        }
    }
}
