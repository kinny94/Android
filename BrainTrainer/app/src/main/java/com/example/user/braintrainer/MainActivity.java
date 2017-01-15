package com.example.user.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button start, button0, button1, button2, button3, playAgain;
    TextView qtion, timer;
    TextView result;
    TextView pointsText;
    RelativeLayout layout;
    ArrayList<Integer> answers = new ArrayList<Integer>();
    int correctAnswerLoc,
        score = 0,
        numberOfQuestions = 0;

    public void playAgain(View view){
        score = 0;
        numberOfQuestions = 0;
        timer.setText("30s");
        pointsText.setText("0/0");
        result.setText("");
        playAgain.setVisibility(View.INVISIBLE);

        generate();

        new CountDownTimer(30100, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                playAgain.setVisibility(View.VISIBLE);
                timer.setText("0s");
                result.setText("Your Score: " + Integer.toString(score) + "/" + Integer.toString(numberOfQuestions));
            }
        }.start();
    }

    public void generate(){
        Random rand = new Random();
        int a = rand.nextInt(21);
        int b = rand.nextInt(21);

        qtion.setText(Integer.toString(a) + " + " + Integer.toString(b));

        correctAnswerLoc = rand.nextInt(4);
        answers.clear();

        int wrongAnswer;

        for(int i=0; i<4; i++){
            if(i == correctAnswerLoc){
                answers.add(a + b);
            }else{
                wrongAnswer = rand.nextInt(41);
                while(wrongAnswer == a + b){
                    wrongAnswer = rand.nextInt(41);
                }
                answers.add(wrongAnswer);
            }
        }

        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
    }

    public void chooseAnswer(View view){

        if(view.getTag().toString().equals(Integer.toString(correctAnswerLoc))){
            score++;
            result.setText("Correct!!");
        }else{
            result.setText("Incorrect!!");
        }

        numberOfQuestions++;
        pointsText.setText(Integer.toString(score) + "/" + Integer.toString(numberOfQuestions));
        generate();

    }

    public void start(View view){
        start.setVisibility(View.INVISIBLE);
        layout.setVisibility(View.VISIBLE);
        playAgain(playAgain);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button)findViewById(R.id.start);
        qtion = (TextView)findViewById(R.id.qtion);
        button0 = (Button)findViewById(R.id.button0);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        result = (TextView)findViewById(R.id.result);
        pointsText = (TextView)findViewById(R.id.points);
        timer = (TextView)findViewById(R.id.timer);
        playAgain = (Button)findViewById(R.id.playAgain);
        layout = (RelativeLayout)findViewById(R.id.rl);
        playAgain(playAgain);
    }
}
