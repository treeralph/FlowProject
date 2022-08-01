package com.example.flowproject.DatabaseManager;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.flowproject.tool.Callback;

import java.util.ArrayList;

public class SQLiteManager {

    private static String HISTORY_DATABASE = "historyDatabase";
    private static String HISTORY_DATABASE_TABLE = "historyTable";

    Context context;
    SQLiteDatabase database;

    public SQLiteManager(Context context) {

        this.context = context;

        database = database = this.context.openOrCreateDatabase(HISTORY_DATABASE, MODE_PRIVATE, null);
        database.execSQL("create table if not exists " + HISTORY_DATABASE_TABLE + " ("
                + "_id integer PRIMARY KEY autoincrement,"
                + "history text)");
    }

    public void write(String data, Callback callback){
        read(new Callback() {
            @Override
            public void OnCallback(Object object) {
                ArrayList<String> dataList = (ArrayList<String>) object;
                if(dataList.contains(data)){
                    dataList.remove(data);
                }
                if(dataList.size() == 10){
                    dataList.remove(0);
                }
                dataList.add(data);
                database.execSQL("delete from " + HISTORY_DATABASE_TABLE);
                for(int i=0; i<dataList.size(); i++){
                    database.execSQL("insert into " + HISTORY_DATABASE_TABLE
                            + "(history)"
                            + "values"
                            + "('" + dataList.get(i) + "')");
                }
            }
        });
    }

    public void read(Callback callback){

        ArrayList<String> dataList = new ArrayList<>();

        Cursor cursor = database.rawQuery("select _id, history from " + HISTORY_DATABASE_TABLE, null);
        int recordCount = cursor.getCount();
        for(int i=0; i<recordCount; i++){
            cursor.moveToNext();
            int id = cursor.getInt(0);
            String history = cursor.getString(1);
            dataList.add(history);
        }
        callback.OnCallback(dataList);
    }
}
