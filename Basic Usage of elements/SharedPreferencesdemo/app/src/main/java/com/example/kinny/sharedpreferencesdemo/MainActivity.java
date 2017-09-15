package com.example.kinny.sharedpreferencesdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.add){

            Toast.makeText(getApplicationContext(), "Action button tapped", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
