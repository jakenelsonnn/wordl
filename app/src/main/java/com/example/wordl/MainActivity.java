package com.example.wordl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String word = "";


    private ArrayList<Square> squares = new ArrayList<>();
    private int position = 0;
    private int numGuesses;

    private CustomAdapter customAdapter;
    private GridView gridView;
    private EditText guessEditText;
    private TextView scoreTextView;

    private String statsFilePath;

    private boolean gameWon = false;

    //stats
    private int oneWins = 0, twoWins = 0, threeWins = 0, fourWins = 0, fiveWins = 0, sixWins = 0, losses = 0;
    private boolean restart = false;

    //points for scoring
    private int points = 0, currentWordPoints = 0, pointsSubtractionValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up the file path string for stats.txt
        statsFilePath = getApplicationContext().getFilesDir() + "/" + "stats.txt";
        File file = new File(statsFilePath);

        //stats file has not been written to yet, so write all 0s to it
        if(file.length() == 0 || restart){
            FileWriter writer = null;
            try {
                writer = new FileWriter(statsFilePath);
                for(int i = 0; i < 8; i++) {
                    writer.append("0\n");
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //populate the square array with empties
        for(int i = 0; i < 30; i++){
            squares.add(new Square('\0', "grey"));
        }

        //keep track of the number of guesses player has made
        numGuesses = 0;

        //set up the score text view
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);

        //get the stats and score from file
        updateStatsFromFile();
        scoreTextView.setText(new StringBuilder().append(points).toString());

        //setup the adapter with the gridview and the array
        gridView = findViewById(R.id.grid);
        customAdapter = new CustomAdapter(this, R.layout.square, squares);
        gridView.setAdapter(customAdapter);


        //pick a random word from words.txt
        try {
            this.word = generateWord();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //setup the text entry area
        guessEditText = findViewById(R.id.guessEditText);

        //set up the guess button
        Button guessButton = (Button) findViewById(R.id.guess);
        guessButton.setBackgroundColor(Color.rgb(42, 155, 247));
        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guessText = String.valueOf(guessEditText.getText());
                if(guessText.length() != 5){
                    Toast.makeText(getApplicationContext(), "please enter a five letter word!", Toast.LENGTH_LONG).show();
                }else{
                    guessWord(guessText, position);
                    position = position + 5;
                    refresh();
                    guessEditText.setText("");
                }
            }
        });

        //set up the clear button
        Button clearButton = (Button) findViewById(R.id.clear);
        clearButton.setBackgroundColor(Color.rgb(255, 165, 0));
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart();
            }
        });

        //set up stats button
        Button statsButton = (Button) findViewById(R.id.statsbutton);
        statsButton.setBackgroundColor(Color.rgb(42, 155, 247));
        statsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, GraphActivity.class));
            }
        });
    }

    //generates the word to be guessed. also handles getting the words's point value, and the gameWon bool
    private String generateWord() throws FileNotFoundException {
        gameWon = false;
        String word = "";

        //words.txt has 5754 words, arranged by commonness or something,
        //but i'm restraining the words to 2800 for a more reasonable word list.
        //word list from https://github.com/charlesreid1/five-letter-words/blob/master/sgb-words.txt (i removed a few)
        int wordCount = 2800;
        Random rand = new Random();
        int randIndex = rand.nextInt(wordCount);

        //points value of word is its index in the file
        currentWordPoints = randIndex;
        pointsSubtractionValue = (int) (currentWordPoints * .15);

        //read the file and get the word
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(getAssets().open("words.txt")));
            for(int i = 0; i != randIndex; i++){
                word = reader.readLine();
            }

        }catch(Exception e){
            //log the exception
        }
        finally{
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return word;
    }

    private void guessWord(String guess, int position){
        numGuesses++;

        if(numGuesses < 6){
            if(!guess.equals(word)){ //incorrect guess
                for(int i = 0; i < 5; i++) {
                    //put the letters on the squares
                    squares.get(position + i).setLetter(guess.charAt(i));
                    //set colors of squares
                    if(word.charAt(i) == guess.charAt(i)) {
                        squares.get(position + i).setColor("blue");
                    }
                    else if(word.contains(String.valueOf(guess.charAt(i)))){
                        squares.get(position + i).setColor("orange");
                    }
                }
                //decrement the number of points earned
                currentWordPoints -= pointsSubtractionValue;
            }
            else{ //correct guess
                for(int i = 0; i < 5; i++){
                    squares.get(position + i).setLetter(guess.charAt(i));
                    squares.get(position + i).setColor("blue");
                }

                //congrats
                Toast.makeText(getApplicationContext(), "Well done! +" + Integer.toString(currentWordPoints) + " points earned.", Toast.LENGTH_LONG).show();

                //increment score and stats
                points += currentWordPoints;
                scoreTextView.setText(new StringBuilder().append(points).toString());
                updateStats(numGuesses);
                updateStatsFile();
                gameWon = true;
            }
        }else if(numGuesses == 6 && !guess.equals(word)){ //incorrect guess and out of guesses
            Toast.makeText(getApplicationContext(), "out of guesses. The word was: " + word, Toast.LENGTH_LONG).show();

            //set score to 0 and update stats
            points = 0;
            updateStats(7);
            updateStatsFile();

            for(int i = 0; i < 5; i++) {
                squares.get(position + i).setLetter(guess.charAt(i));
                if(word.charAt(i) == guess.charAt(i)) {
                    squares.get(position + i).setColor("blue");
                }
                else if(word.contains(String.valueOf(guess.charAt(i)))){
                    squares.get(position + i).setColor("orange");
                }
            }
        }else if(numGuesses == 6 && guess.equals(word)){ //correct guess on the last guess
            for(int i = 0; i < 5; i++){
                squares.get(position + i).setLetter(guess.charAt(i));
                squares.get(position + i).setColor("blue");
            }

            //congrats
            Toast.makeText(getApplicationContext(), "Well done! +" + Integer.toString(currentWordPoints) + " points earned.", Toast.LENGTH_LONG).show();

            //update score and stats
            points += currentWordPoints;
            scoreTextView.setText(new StringBuilder().append(points).toString());
            updateStats(numGuesses);
            updateStatsFile();
            gameWon = true;
        }
    }

    //refresh the grid
    private void refresh(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                customAdapter.notifyDataSetChanged();
                gridView.invalidate();
            }
        });
    }

    private void restart(){
        //reset index and number of guesses
        position = 0;
        numGuesses = 0;

        //if restart mid-game, player loses score
        if(!gameWon) points = 0;
        scoreTextView.setText(new StringBuilder().append(points).toString());

        //re-generate the word
        try {
            word = generateWord();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //reset the grid
        for(int i = 0; i < 30; i++){
            squares.get(i).setLetter('\0');
            squares.get(i).setColor("gray");
        }

        //refresh the grid, clear the text box
        refresh();
        guessEditText.setText("");
    }

    //read the file and update the score text view
    private void updateStatsFromFile(){

        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(statsFilePath));
            oneWins = Integer.parseInt(reader.readLine());
            twoWins = Integer.parseInt(reader.readLine());
            threeWins = Integer.parseInt(reader.readLine());
            fourWins = Integer.parseInt(reader.readLine());
            fiveWins = Integer.parseInt(reader.readLine());
            sixWins = Integer.parseInt(reader.readLine());
            losses = Integer.parseInt(reader.readLine());
            points = Integer.parseInt(reader.readLine());

            //set score text view to score
            scoreTextView.setText(points);

        }catch(Exception e){
            //log the exception
        }
        finally{
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d("MYTAG: ", "ERROR IN UPDATESTATSONSCREEN");
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //save stats on a text file
    private void updateStatsFile(){
        PrintWriter writer = null;
        try{
            writer = new PrintWriter(statsFilePath, "UTF-8");
            writer.println(oneWins);
            writer.println(twoWins);
            writer.println(threeWins);
            writer.println(fourWins);
            writer.println(fiveWins);
            writer.println(sixWins);
            writer.println(losses);
            writer.println(points);
        }catch(Exception e){
            Log.d("MYTAG: ", "ERROR IN UPDATESTATSFILE");
        }
        finally{
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void updateStats(int n){
        switch (n) {
            case 1:{
                oneWins++;
                break;
            }
            case 2:{
                twoWins++;
                break;
            }
            case 3:{
                threeWins++;
                break;
            }
            case 4:{
                fourWins++;
                break;
            }
            case 5:{
                fiveWins++;
                break;
            }
            case 6:{
                sixWins++;
                break;
            }
            case 7:{
                losses++;
                break;
            }
        }
    }
}