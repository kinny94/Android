package com.example.user.timers;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //countdown time is useful for temporary usage

        new CountDownTimer(10000, 1000){
            public void onTick(long millisecondsUntilDone){
                Log.i("seconds left, ", String.valueOf(millisecondsUntilDone / 1000));
                //countdown is counting down.
            }
            public void onFinish(){
                Log.i("Finish", "Countdown timer Finished");
            }
        }.start();

        /*handler allows chunks of code to be run in a delayed way. These chunks of code are called runnable
        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //Insert code to be run every second
                Log.i("Runnable has run", "a second must have passed");
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(run);
        */
    }
}
