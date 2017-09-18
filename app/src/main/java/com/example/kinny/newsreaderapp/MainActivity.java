     package com.example.kinny.newsreaderapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
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
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    ArrayList<String> urls = new ArrayList<String>();
    ArrayList<String> content = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listview);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,titles);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                i.putExtra("articleURL", urls.get(position));
                i.putExtra("content", content.get(position));
                startActivity(i);
            }
        });

        articlesDB = this.openOrCreateDatabase("articles", MODE_PRIVATE, null);
        articlesDB.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleId INTEGER, url VARCHAR, title VARCHAR, content VARCHAR)");

        updateListView();

        DownloadTasks tasks = new DownloadTasks();
        try {

            // try to do as less api calls in onCreate function, this will slow down the appz
            tasks.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void updateListView(){

        try{
            Cursor c = articlesDB.rawQuery("SELECT * FROM articles ORDER BY articleId DESC", null);
            int contentIndex = c.getColumnIndex("content");
            int urlIndex = c.getColumnIndex("url");
            int titleIndex = c.getColumnIndex("title");

            c.moveToFirst();
            titles.clear();

            while(c != null){

                titles.add(c.getString(titleIndex));
                urls.add(c.getString(urlIndex));
                content.add(c.getString(contentIndex));
                c.moveToNext();
            }

            arrayAdapter.notifyDataSetChanged();
        }catch (Exception e) {
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


                JSONArray jsonArray = new JSONArray(result);

                articlesDB.execSQL("DELETE FROM articles");

                for(int i=0; i<30; i++){

                    String articleId = jsonArray.getString(i);
                    url = new URL("https://hacker-news.firebaseio.com/v0/item/" + articleId + ".json?print=pretty");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    in = urlConnection.getInputStream();
                    reader = new InputStreamReader(in);
                    data = reader.read();
                    String articleInfo = "";

                    while(data != -1){
                        char current = (char) data;
                        articleInfo += current;
                        data = reader.read();
                    }

                    JSONObject jsonObject= new JSONObject(articleInfo);

                    String articleTitle = jsonObject.getString("title");
                    String articleUrl = jsonObject.getString("url");

                    url = new URL(articleUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    in = urlConnection.getInputStream();
                    reader = new InputStreamReader(in);
                    data = reader.read();

                    String articleContent = "";

                    while(data != -1){
                        char current  = (char) data;
                        articleInfo += current;
                        data = reader.read();
                    }

                    articleIds.add(Integer.valueOf(articleId));
                    articleTitles.put(Integer.valueOf(articleId), articleTitle);
                    articleUrls.put(Integer.valueOf(articleId), articleUrl);

                    String sql = "INSERT INTO articles (articleId, url, title, content) VALUES (? , ? , ?, ?)";
                    SQLiteStatement statement = articlesDB.compileStatement(sql);


                    statement.bindString(1, articleId);
                    statement.bindString(2, articleUrl);
                    statement.bindString(3, articleTitle);
                    statement.bindString(4, articleContent);

                    statement.execute();

                }

            }catch(Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            updateListView();
        }
    }
}
