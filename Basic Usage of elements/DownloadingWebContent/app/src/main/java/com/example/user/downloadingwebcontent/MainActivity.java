package com.example.user.downloadingwebcontent;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String>{ // we use async task to run this this task on a different thread then the mani one. Advised by google.

        @Override
        protected String doInBackground(String... urls) {   //String... params not an array, its varargs. can contain multiple urls

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try{

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in  = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1){  //read data till it can no more

                    char current = (char) data;
                    result += current;
                    data = reader.read();   // keep on reading

                }

                return result;

            }catch(Exception e){

                e.printStackTrace();
                return "Failed!";

            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        String result  = null;
        try {
            result = task.execute("https://kiiiinnnnnnnnnyyyy.github.io/portfolio").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.i("Contents of URL", result);
    }
}
