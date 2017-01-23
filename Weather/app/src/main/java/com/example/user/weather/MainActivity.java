package com.example.user.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText city;
    TextView showWeather, minMax, pressureText, humidityText, temperature;

    public void  weather(View view){

        String cityName = null;
        String apiKey = "13e13468908840600e6ee16d876dfe2a";
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(city.getWindowToken(), 0);

        try {

            cityName = URLEncoder.encode(city.getText().toString(), "UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" +  cityName + "&appid=" + apiKey);

        }catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        Log.i("city", cityName);

    }

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {

            String result  = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data  = reader.read();

                while(data != -1){

                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }

                return result;

            }catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Couldn't find weather!!", Toast.LENGTH_LONG).show();

            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {

            try{

                String message = "";

                JSONObject jsonObject = new JSONObject(s);
                String weather = jsonObject.getString("weather");

                String temp = jsonObject.getJSONObject("main").getString("temp");
                double currTemp = Double.parseDouble(temp);
                currTemp = currTemp * 9/5 - 459.67;
                currTemp = Math.round(currTemp * 100)/100D;

                String minTemp = jsonObject.getJSONObject("main").getString("temp_min");
                double min = Double.parseDouble(minTemp);
                min = min * 9/5 - 459.67;
                min = Math.round(min*100)/100D;

                String maxTemp = jsonObject.getJSONObject("main").getString("temp_max");
                double max = Double.parseDouble(maxTemp);
                max = max * 9/5 - 459.67;
                max = Math.round(max *100)/100D;

                String speed = jsonObject.getJSONObject("wind").getString("speed");
                String humidity = jsonObject.getJSONObject("main").getString("humidity");
                JSONArray arr = new JSONArray(weather);

                for(int i=0; i<1; i++){

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = "";
                    String description = "";

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if(main != "" && description != ""){

                        message += main + ": " + description + "\n";

                    }

                }

                if(message != ""){

                    showWeather.setText(message);
                    temperature.setText("Current Temperature: " + currTemp + "F");
                    minMax.setText("Min: " + min + "F           " + "Max: " + max + "F");
                    humidityText.setText("Humidity: " + humidity + "%");
                    pressureText.setText("Wind Speed: " + speed + "mph");

                }else{

                    Toast.makeText(getApplicationContext(), "Couldn't find weather!!", Toast.LENGTH_LONG).show();

                }

            }catch(Exception e){

                Toast.makeText(getApplicationContext(), "Couldn't find weather!!", Toast.LENGTH_LONG).show();

            }

            super.onPostExecute(s);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (EditText)findViewById(R.id.city);
        showWeather = (TextView)findViewById(R.id.showWeather);
        temperature = (TextView)findViewById(R.id.temp);
        minMax = (TextView)findViewById(R.id.minMax);
        humidityText = (TextView)findViewById(R.id.humidity);
        pressureText = (TextView)findViewById(R.id.pressure);

    }
}
