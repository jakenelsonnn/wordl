package com.example.wordl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.github.mikephil.charting.data.Entry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class FiveLetterHistoryActivity extends AppCompatActivity {

    private ArrayList<HistorySquare> squares = new ArrayList<>();
    private HistorySquareAdapter historySquareAdapter;
    private GridView gridView;
    private String historyFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_letter_history);
        //set up the file
        historyFilePath = getApplicationContext().getFilesDir() + "/" + "five-letter-history.txt";

        /*
        //populate the square array with empties
        for(int i = 0; i < 150; i++){
            squares.add(new HistorySquare("blue"));
        }
        */

        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(historyFilePath));
            String line = null;
            while((line = reader.readLine()) != null){
                int n = Integer.parseInt(line);
                String color = getColorFromInt(n);
                squares.add(new HistorySquare(color));
            }

        }catch (Exception e){

        }

        //set up the adapter and the gridview
        gridView = findViewById(R.id.historygrid);
        historySquareAdapter = new HistorySquareAdapter(this, R.layout.history_square, squares);
        gridView.setAdapter(historySquareAdapter);
    }

    private String getColorFromInt(int n)
    {
        switch (n)
        {
            case 1:{
                return "blue";
            }
            case 2:{
                return "green";
            }
            case 3:{
                return "yellow";
            }
            case 4:{
                return "orangeish";
            }
            case 5:{
                return "orange";
            }
            case 6:{
                return "red";
            }
            default:{
                return "grey";
            }
        }
    }
}