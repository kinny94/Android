package com.example.user.tic_tac_toe;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //0 = circle 1 = cross
    int activePlayer = 0;

    //to check if game is active or not
    boolean active = true;

    //2 = not playing
    int[] currentState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    int[][] win = { {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    public void dropIn(View view){

        ImageView counter = (ImageView) view;
        counter.getTag().toString();
        int tappedCounter = Integer.parseInt(counter.getTag().toString());
        System.out.println("Tag: " + tappedCounter);

        if(currentState[tappedCounter] == 2 && active){

            currentState[tappedCounter] = activePlayer;

            //taking it to the top of the screen
            counter.setTranslationY(-1000f);

            if(activePlayer == 0){
                //setting the image
                counter.setImageResource(R.drawable.circle);
                activePlayer = 1;
            }else {
                //setting the image
                counter.setImageResource(R.drawable.cross);
                activePlayer = 0;
            }

            //animation
            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for(int[] positions : win){
                //checking if someone has won
                if(currentState[positions[0]] == currentState[positions[1]] && currentState[positions[1]] == currentState[positions[2]] && currentState[positions[0]] != 2){
                    //System.out.println("Winner: " + currentState[positions[0 ]]);

                    active = false;
                    String winner = "Cross";


                    if(currentState[positions[0]] == 0){
                        winner = "Circle";
                    }

                    TextView winnerMsg = (TextView)findViewById(R.id.winnerMessage);
                    winnerMsg.setText(winner + " has won!");
                    //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
                    //layout.setVisibility(View.VISIBLE);

                    Button button = (Button)findViewById(R.id.playAgainButton);
                    button.setVisibility(View.VISIBLE);

                }else{

                    boolean gameOver = true;

                    for(int counterState : currentState){
                        if(counterState == 2){
                            gameOver = false;
                        }
                    }

                    if(gameOver){

                        TextView winnerMsg = (TextView)findViewById(R.id.winnerMessage);
                        winnerMsg.setText("It's a draw!!");
                        Button button = (Button)findViewById(R.id.playAgainButton);
                        button.setVisibility(View.VISIBLE);

                    }

                }

            }

        }

    }

    public void playAgain(View view){

        active = true;

        Button button = (Button)findViewById(R.id.playAgainButton);
        button.setVisibility(View.INVISIBLE);
        TextView text = (TextView)findViewById(R.id.winnerMessage);
        text.setText("TIC-TAC-TOE");
        activePlayer = 0;
        for(int i=0; i<currentState.length; i++){
            currentState[i] = 2;
        }
        //changing the image of imageviews in grid layout
        GridLayout grid = (GridLayout)findViewById(R.id.grid);
        for(int i=0; i<grid.getChildCount(); i++){
            ((ImageView) grid.getChildAt(i)).setImageResource(0);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
