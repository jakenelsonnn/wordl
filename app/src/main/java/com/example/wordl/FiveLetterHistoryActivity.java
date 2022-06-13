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

        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(historyFilePath));
            String line = null;
            while((line = reader.readLine()) != null){
                int n = Integer.parseInt(line);
                String color = getColorFromInt(n);
                squares.add(new HistorySquare(color));
            }

        }catch (Exception e){ }

        //set up the adapter and the gridview
        gridView = findViewById(R.id.historygrid);
        historySquareAdapter = new HistorySquareAdapter(this, R.layout.history_square, squares);
        gridView.setAdapter(historySquareAdapter);
    }

    //I know. I don't like this either.
    private String getColorFromInt(int n)
    {
        switch (n)
        {
            case 1:{
                return "OneGuessColor";
            }
            case 2:{
                return "TwoGuessColor";
            }
            case 3:{
                return "ThreeGuessColor";
            }
            case 4:{
                return "FourGuessColor";
            }
            case 5:{
                return "FiveGuessColor";
            }
            case 6:{
                return "SixGuessColor";
            }
            default:{
                return "LossColor";
            }
        }
    }
}