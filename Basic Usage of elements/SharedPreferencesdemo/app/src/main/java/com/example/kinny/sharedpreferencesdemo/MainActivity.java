package com.example.kinny.sharedpreferencesdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sharing data with only your app - private
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.kinny.sharedpreferencesdemo", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("username", "Arjun").apply();

        String username = sharedPreferences.getString("username", "");
        Log.i("username", username);

    }
}
