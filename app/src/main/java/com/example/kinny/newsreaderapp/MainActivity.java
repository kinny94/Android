package com.example.kinny.newsreaderapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Map<Integer, String> articleUrls = new HashMap<Integer, String>();
    Map<Integer, String> articleTitles = new HashMap<Integer, String>();
    ArrayList<Integer> articleIds = new ArrayList<Integer>();

    SQLiteDatabase articlesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articlesDB = this.openOrCreateDatabase("Artcles", MODE_PRIVATE, null);

        articlesDB.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleID INTEGER, url VARCHAR, title VARCHAR, content VARCHAR)");


        DownloadTasks tasks = new DownloadTasks();
        try {
            String result = tasks.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();

            JSONObject jsonArray = new JSONObject(result);

            articlesDB.execSQL("DELETE FROM articles");

            for(int i=0; i<30; i++){

                String articleId = jsonArray.getString(String.valueOf(i));
                DownloadTasks getArticle = new DownloadTasks();
                //Log.i("Article ID", jsonArray.getString(String.valueOf(i)));

                String articleInfo = getArticle.execute("https://hacker-news.firebaseio.com/v0/item/" + articleId + ".json?print=pretty").get();

                JSONObject jsonObject= new JSONObject(articleInfo);

                String articleTitle = jsonObject.getString("title");
                String articleUrl = jsonObject.getString("url");

                articleIds.add(Integer.valueOf(articleId));
                articleTitles.put(Integer.valueOf(articleId), articleTitle);
                articleUrls.put(Integer.valueOf(articleId), articleUrl);

                String sql = "INSERT INTO articles (articleId, url, title) VALUES (? , ? , ?)";
                SQLiteStatement statement = articlesDB.compileStatement(sql);


                statement.bindString(1, articleId);
                statement.bindString(2, articleUrl);
                statement.bindString(3, articleTitle);

                statement.execute();

            }

            Cursor c = articlesDB.rawQuery("SELECT * FROM aarticles", null);
            int articleIdIndex = c.getColumnIndex("articleId");
            int urlIndex = c.getColumnIndex("url");
            int titleIndex = c.getColumnIndex("title");

            c.moveToFirst();

            while(c != null){
                c.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DownloadTasks extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
            return result;
        }
    }
}
