package com.example.user.hidingandshowing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tf;

    public void show(View view){

        tf.setVisibility(View.VISIBLE);

    }

    public void hide(View view){
        tf.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tf = (TextView)findViewById(R.id.textView);

    }
}
