package com.example.flowproject.tool;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NaverMovieAPI {

    private String TAG = "NaverMovieAPI";

    public NaverMovieAPI() {
    }

    public void request(String address, Callback callback) {
        try {
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();

            String query = "https://openapi.naver.com/v1/search/movie.json?query=" + URLEncoder.encode(address, "UTF-8");

            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {

                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.setRequestMethod("GET");

                conn.setRequestProperty("X-Naver-Client-Id", "GfbVpyB2ToX8Gb5glNUT");
                conn.setRequestProperty("X-Naver-Client-Secret", "CgfNOhRVlv");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                Log.w(TAG, "responseCode: " +responseCode);
                if (responseCode == 200) {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    Log.w(TAG, line + "\n");
                    stringBuilder.append(line + "\n");
                }

                JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                Log.w(TAG, "JSON_Test: " + jsonObject.toString());

                bufferedReader.close();
                conn.disconnect();

                callback.OnCallback(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
