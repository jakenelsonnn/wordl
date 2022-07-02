package com.example.wordl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class ThemesActivity extends AppCompatActivity {

    private int color, correctPositionColor = 0, wrongPositionColor = 0;
    private boolean changeWrongPositionButton = false;
    private MaterialCardView correctPositionButton;
    private MaterialCardView wrongPositionButton;
    private String colorsFilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);

        //load the saved colors
        colorsFilePath = getApplicationContext().getFilesDir() + "/" + "colors.txt";

        File file = new File(colorsFilePath);
        if(file.length() == 0){
            correctPositionColor = Color.rgb(42, 155, 247);
            wrongPositionColor = Color.rgb(255, 165, 0);
        }else {
            //get the saved colors
            BufferedReader reader = null;
            try{
                reader = new BufferedReader(new FileReader(colorsFilePath));
                correctPositionColor = Integer.parseInt(reader.readLine());
                wrongPositionColor = Integer.parseInt(reader.readLine());
            }catch (Exception e){ }
        }

        //set up correct position Color button
        correctPositionButton = (MaterialCardView) findViewById(R.id.changecorrectletterbutton);
        correctPositionButton.setBackgroundColor(correctPositionColor);
        correctPositionButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivityForResult(new Intent(ThemesActivity.this, ColorsActivity.class), 0);
            }
        });

        //set up wrong position Color button
        wrongPositionButton = (MaterialCardView) findViewById(R.id.changewrongpositionbutton);
        wrongPositionButton.setBackgroundColor(wrongPositionColor);
        wrongPositionButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                changeWrongPositionButton = true;
                startActivityForResult(new Intent(ThemesActivity.this, ColorsActivity.class), 0);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                color = data.getExtras().getInt("color");
                if(changeWrongPositionButton) {
                    wrongPositionButton.setBackgroundColor(color);
                    wrongPositionColor = color;
                    PrintWriter writer = null;
                    try{
                        writer = new PrintWriter(colorsFilePath, "UTF-8");
                        writer.println(correctPositionColor);
                        writer.println(wrongPositionColor);
                    }catch(Exception e){
                        Log.d("MYTAG: ", "ERROR IN UPDATECOLORFILE");
                    }
                    finally {
                        if (writer != null) {
                            writer.close();
                        }
                    }
                    changeWrongPositionButton = false;
                }else{
                    correctPositionButton.setBackgroundColor(color);
                    correctPositionColor = color;
                    PrintWriter writer = null;
                    try{
                        writer = new PrintWriter(colorsFilePath, "UTF-8");
                        writer.println(correctPositionColor);
                        writer.println(wrongPositionColor);
                    }catch(Exception e){
                        Log.d("MYTAG: ", "ERROR IN UPDATECOLORFILE");
                    }
                    finally {
                        if (writer != null) {
                            writer.close();
                        }
                    }
                }
            }
        }
    }

}