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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String word = generateWord();
    private ArrayList<Square> squares = new ArrayList<>();
    private int position = 0;
    private CustomAdapter customAdapter;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //populate the square array with empties
        for(int i = 0; i < 25; i++){
            squares.add(new Square('\0'));
        }

        //setup the adapter with the gridview and the array
        gridView = findViewById(R.id.grid);
        customAdapter = new CustomAdapter(this, R.layout.square, squares);
        gridView.setAdapter(customAdapter);

        //setup the text entry area
        EditText guessEditText = findViewById(R.id.guessEditText);

        Button guessButton = (Button) findViewById(R.id.guess);
        guessButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String guessText = String.valueOf(guessEditText.getText());
                guessWord(guessText, position);
                position = position + 5;
                refresh();
                Toast.makeText(MainActivity.this, String.valueOf(guessEditText.getText()) + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateWord(){
        //todo: get random word from words.txt
        return "alien";
    }

    private void guessWord(String guess, int position){
        if(!guess.equals(word)){
            for(int i = 0; i < 5; i++) {
                squares.get(position + i).setLetter(guess.charAt(i));
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
}