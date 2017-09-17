package com.example.kinny.databasedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{

            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);

            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR, age INT(3))");
            myDatabase.execSQL("INSERT INTO users (name, age) VALUES (Arjun, 23)");
            myDatabase.execSQL("INSERT INTO users (name, age) VALUES (Ron, 53)");

            /   / To get data out of the database
            Cursor c = myDatabase.rawQuery("SELECT * FROM users", null);

            // To get indexes of columns, necessary in android
            int nameIndex = c.getColumnIndex("name");
            int ageIndex = c.getColumnIndex("age");

            c.moveToFirst();
            while(c!= null){
                Log.i("name", c.getString(nameIndex));
                Log.i("age", Integer.toString(c.getInt(ageIndex)));

                Toast.makeText(getApplicationContext(), c.getString(nameIndex), Toast.LENGTH_LONG).show();

                c.moveToNext();
            }


        }catch(Exception e){

        }
    }
}
