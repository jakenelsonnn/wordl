package com.example.wordl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.example.wordl.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private String word = "";


    private ArrayList<Square> squares = new ArrayList<>();
    private int position = 0;
    private CustomAdapter customAdapter;
    private GridView gridView;
    private EditText guessEditText;
    private int numGuesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //populate the square array with empties
        for(int i = 0; i < 25; i++){
            squares.add(new Square('\0', "grey"));
        }

        numGuesses = 0;

        //setup the adapter with the gridview and the array
        gridView = findViewById(R.id.grid);
        customAdapter = new CustomAdapter(this, R.layout.square, squares);
        gridView.setAdapter(customAdapter);

        try {
            this.word = generateWord();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //setup the text entry area
        guessEditText = findViewById(R.id.guessEditText);

        //set up the guess button
        Button guessButton = (Button) findViewById(R.id.guess);
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
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart();
            }
        });
    }

    private String generateWord() throws FileNotFoundException {
        String word = "";

        int wordCount = 8548;
        Random rand = new Random();
        int randIndex = rand.nextInt(wordCount);
        BufferedReader reader = null;

        //read the file and get the word
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

        //Toast.makeText(getApplicationContext(), word, Toast.LENGTH_LONG).show();
        return word;
    }

    private void guessWord(String guess, int position){
        numGuesses++;
        if(numGuesses < 5){
            if(!guess.equals(word)){
                for(int i = 0; i < 5; i++) {
                    squares.get(position + i).setLetter(guess.charAt(i));

                    if(word.charAt(i) == guess.charAt(i)) {
                        squares.get(position + i).setColor("green");
                    }
                    else if(word.contains(String.valueOf(guess.charAt(i)))){
                        squares.get(position + i).setColor("orange");
                    }
                }
            }
            else{ //correct guess
                for(int i = 0; i < 5; i++){
                    squares.get(position + i).setLetter(guess.charAt(i));
                    squares.get(position + i).setColor("green");
                }
                Toast.makeText(getApplicationContext(), "you did it!!", Toast.LENGTH_LONG).show();
            }
        }else if(numGuesses == 5){
            Toast.makeText(getApplicationContext(), "out of guesses. The word was: " + word, Toast.LENGTH_LONG).show();
            for(int i = 0; i < 5; i++) {
                squares.get(position + i).setLetter(guess.charAt(i));

                if(word.charAt(i) == guess.charAt(i)) {
                    squares.get(position + i).setColor("green");
                }
                else if(word.contains(String.valueOf(guess.charAt(i)))){
                    squares.get(position + i).setColor("orange");
                }
            }
        }
    }

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
        //reset index
        position = 0;
        numGuesses = 0;

        //re-generate the word
        try {
            word = generateWord();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 25; i++){
            squares.get(i).setLetter('\0');
            squares.get(i).setColor("gray");
        }

        //refresh the grid, clear the text box
        refresh();
        guessEditText.setText("");
    }
}