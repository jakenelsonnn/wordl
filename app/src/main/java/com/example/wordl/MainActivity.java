package com.example.wordl;

import androidx.appcompat.app.AppCompatActivity;

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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Locale;
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

    private String statsFilePath, historyFilePath;

    private final int WORDLIMIT = 3000;

    private boolean gameWon = false;

    //stats
    private int oneWins = 0, twoWins = 0, threeWins = 0, fourWins = 0, fiveWins = 0, sixWins = 0, losses = 0;

    //in case i need to restart the save data
    private boolean restart = false;

    //points for scoring
    private int points = 0, currentWordPoints = 0, pointsSubtractionValue = 0, highScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up the file path string for stats.txt
        statsFilePath = getApplicationContext().getFilesDir() + "/" + "stats.txt";
        historyFilePath = getApplicationContext().getFilesDir() + "/" + "five-letter-history.txt";
        File file = new File(statsFilePath);

        //stats file has not been written to yet (or i need to reset save data), so write all 0s to it
        if(file.length() == 0 || restart){
            FileWriter writer = null;
            try {
                writer = new FileWriter(statsFilePath);
                for(int i = 0; i < 9; i++) {
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
                if(guessText.length() != 5 || !isOnlyChars(guessText) || !isWordInList(guessText)){
                    Toast.makeText(getApplicationContext(), "Not in word list.", Toast.LENGTH_LONG).show();
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
                if(numGuesses != 0) restart();
                else{
                    Toast.makeText(getApplicationContext(), "Grid is empty.", Toast.LENGTH_SHORT).show();
                }
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

        //set up history button
        Button historyButton = (Button) findViewById(R.id.historybutton);
        historyButton.setBackgroundColor(Color.rgb(42, 155, 247));
        historyButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, FiveLetterLineChartActivity.class));
            }
        });

        //set up Four Letter Game button
        Button fourLetterGameButton = (Button) findViewById(R.id.fourlettergamebutton);
        fourLetterGameButton.setBackgroundColor(Color.rgb(42, 155, 247));
        fourLetterGameButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, FourLetterActivity.class));
            }
        });

        //set up Six Letter Game button
        Button sixLetterGameButton = (Button) findViewById(R.id.sixlettergamebutton);
        sixLetterGameButton.setBackgroundColor(Color.rgb(42, 155, 247));
        sixLetterGameButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, SixLetterActivity.class));
            }
        });

    }

    //generates the word to be guessed. also handles getting the words's point value, and the gameWon bool
    private String generateWord() throws FileNotFoundException {
        gameWon = false;
        String word = "";

        //words.txt has 5754 words, arranged by commonness or something,
        //but i'm restricting the words to 2800 for a more reasonable word list.
        //word list from https://github.com/charlesreid1/five-letter-words/blob/master/sgb-words.txt (i removed a few)
        Random rand = new Random();
        int randIndex = rand.nextInt(WORDLIMIT);

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

    //guess word from string in text input
    private void guessWord(String guess, int position){
        numGuesses++;

        //force lowercase
        guess = guess.toLowerCase();

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

                //increment score and stats
                points += currentWordPoints;

                //single guess bonus
                if(numGuesses == 1) points += 3000;

                //two guess bonus
                if(numGuesses == 2) points += 1000;

                //congrats
                Toast.makeText(getApplicationContext(), "Well done! +" + Integer.toString(currentWordPoints) + " points earned.", Toast.LENGTH_LONG).show();

                //write score to screen
                scoreTextView.setText(new StringBuilder().append(points).toString());

                updateStats(numGuesses);

                //set high score if necessary
                if(points > highScore)  highScore = points;
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

            //write score to screen
            scoreTextView.setText(new StringBuilder().append(points).toString());

            updateStats(numGuesses);

            //set high score if necessary
            if(points > highScore)  highScore = points;

            //save stats
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

    //restarts the game, regenerates word, reset score if word hasn't been guessed
    private void restart(){
        //reset index and number of guesses
        position = 0;
        numGuesses = 0;

        //if restart mid-game, player loses score
        if(!gameWon){
            points = 0;
        }
        updateStatsFile();
        //write score to screen
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
            highScore = Integer.parseInt(reader.readLine());

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
            writer.println(highScore);

        }catch(Exception e){
            Log.d("MYTAG: ", "ERROR IN UPDATESTATSFILE");
        }
        finally{
            if (writer != null) {
                writer.close();
            }
        }
    }

    //update the stats variables and the history file
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
            case 7: {
                losses++;
                break;
            }
        }
        if(n != 7){
            updateHistoryFile(n);
        }else{
            updateHistoryFile(0);
        }
    }

    private void updateHistoryFile(int n){
        try {
            Writer output = new BufferedWriter(new FileWriter(historyFilePath, true));
            output.append(n + "\n");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //to ensure input string is only chars
    private boolean isOnlyChars(String word) {
        char[] chars = word.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) return false;
        }
        return true;
    }

    //checks if string is in the word file
    private boolean isWordInList(String word) {
        word = word.toLowerCase();
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(getAssets().open("words.txt")));
            for(int i = 0; i != WORDLIMIT; i++){
                if(word.equals(reader.readLine())) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}