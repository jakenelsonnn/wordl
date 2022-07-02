package com.example.wordl;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    private ArrayList<ColorSquare> squares = new ArrayList<>();
    private ColorSquareAdapter colorSquareAdapter;
    private GridView gridView;
    private int red = 0, green = 0, blue = 0;
    private int color;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);

        //fluctuate up and down rgb spectrum one color at a time
        red = 255;
        for (int i = 1; i < 255; i++) {
            blue = i;
            ColorSquare colorSquare = new ColorSquare(Color.rgb(red, green, blue));
            squares.add(colorSquare);
        }

        //red down to 0
        for (int i = red; i > 0; i--) {
            red = i;
            ColorSquare colorSquare = new ColorSquare(Color.rgb(red, green, blue));
            squares.add(colorSquare);
        }

        //green up
        for (int i = 0; i < 255; i++) {
            green = i;
            ColorSquare colorSquare = new ColorSquare(Color.rgb(red, green, blue));
            squares.add(colorSquare);
        }

        //blue down to 0
        for (int i = blue; i > 0; i--) {
            blue = i;
            ColorSquare colorSquare = new ColorSquare(Color.rgb(red, green, blue));
            squares.add(colorSquare);
        }

        //red up
        for (int i = 0; i < 255; i++) {
            red = i;
            ColorSquare colorSquare = new ColorSquare(Color.rgb(red, green, blue));
            squares.add(colorSquare);
        }

        //green down to 0
        for (int i = green; i > 127; i--) {
            green = i;
            ColorSquare colorSquare = new ColorSquare(Color.rgb(red, green, blue));
            squares.add(colorSquare);
        }

        //set up the adapter and the gridview
        gridView = findViewById(R.id.colorgrid);
        colorSquareAdapter = new ColorSquareAdapter(this, R.layout.color_square, squares);
        gridView.setAdapter(colorSquareAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                color = squares.get(i).getColor();
                Intent intent = new Intent();
                intent.putExtra("color", color);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}