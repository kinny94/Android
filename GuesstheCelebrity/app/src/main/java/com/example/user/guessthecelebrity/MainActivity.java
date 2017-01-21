package com.example.user.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> celebImages= new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    int ChosenCeleb = 0;
    ImageView imageView;
    int correctLocation = 0;
    String [] answers = new String[4];
    Button button0, button1, button2, button3;
    int score = 0, total = 0;
    TextView scoreView;

    public void answer(View view){

        if(view.getTag().toString().equals(Integer.toString(correctLocation))){

            Toast.makeText(getApplicationContext(), "Correct!!", Toast.LENGTH_LONG).show();
            score++;
            total++;
            Log.i("Value", String.valueOf(score));
            Log.i("Value", String.valueOf(total));

        }else{

            Toast.makeText(getApplicationContext(), "Incorrect!!, It was " + celebNames.get(ChosenCeleb), Toast.LENGTH_SHORT).show();
            total++;

        }

        createNewQuestion();

    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return null;
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String>{

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
                    result +=  current;
                    data = reader.read();

                }

                return result;

            }catch(Exception e){

                e.printStackTrace();

            }

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        scoreView = (TextView)findViewById(R.id.score);
        button0 = (Button)findViewById(R.id.button);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);

        DownloadTask task = new DownloadTask();
        String result = null;

        try {

            result = task.execute("http://www.posh24.com/celebrities").get();
            String[] splitResult = result.split("<div class=\"sidebarContainer\">");
            Pattern p = Pattern.compile("<img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);

            while(m.find()){

                celebImages.add(m.group(1));

            }

            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher((splitResult[0]));

            while (m.find()) {

                celebNames.add(m.group(1));

            }



        }catch (InterruptedException e) {

            e.printStackTrace();

        }catch (ExecutionException e) {

            e.printStackTrace();

        }

        createNewQuestion();

    }

    public void createNewQuestion(){

        scoreView.setText("Your Score: " + score + "/" + total);

        Random rand = new Random();
        ChosenCeleb = rand.nextInt(celebImages.size());

        ImageDownloader imageTask = new ImageDownloader();
        Bitmap celebImage;
        try {

            celebImage = imageTask.execute(celebImages.get(ChosenCeleb)).get();

            imageView.setImageBitmap(celebImage);
            correctLocation = rand.nextInt(4);

            int incorrectLocation;

            for(int i=0; i<4; i++){

                if(i == correctLocation){

                    answers[i] = celebNames.get(ChosenCeleb);

                }else{

                    incorrectLocation = rand.nextInt(celebImages.size());

                    while(incorrectLocation == ChosenCeleb){

                        incorrectLocation = rand.nextInt(celebImages.size());

                    }

                    answers[i] = celebNames.get(incorrectLocation);

                }

            }

            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);

        }catch (InterruptedException e){

            e.printStackTrace();

        }catch (ExecutionException e) {

            e.printStackTrace();

        }
    }
}
