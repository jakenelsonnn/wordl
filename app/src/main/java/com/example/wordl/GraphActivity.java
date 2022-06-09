package com.example.wordl;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    ArrayList dataList = new ArrayList();
    TextView totalTextView;
    TextView highScoreTextView;
    private int highScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        int totalGamesPlayed = 0;

        //set up the stats file, build array from file contents, total up the numbers in the file.
        String statsFilePath = getApplicationContext().getFilesDir() + "/" + "stats.txt";
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(statsFilePath));
            for(int i = 0; i < 7; i++){
                int n = Integer.parseInt(reader.readLine());
                dataList.add(new BarEntry(i, n));
                totalGamesPlayed += n;
            }
            //skip over current score line
            reader.readLine();

            //get high score
            highScore = Integer.parseInt(reader.readLine());

        }catch (Exception e){

        }

        //set up the bar chart
        BarChart barChart = findViewById(R.id.barchart);
        BarDataSet barDataSet = new BarDataSet(dataList, "Stats");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        //colors for the bars
        int color1 = Color.rgb(27, 152, 224); //blue
        int color2 = Color.rgb(27, 224, 129); //green
        int color3 = Color.rgb(194, 224, 27); //yellowish
        int color4 = Color.rgb(224, 162, 27); //orange-ish
        int color5 = Color.rgb(224, 86, 27); //orange
        int color6 = Color.rgb(224, 27, 27); //red
        int colorLosses = Color.rgb(135, 135, 135); //grey

        int[] barColors = {color1, color2, color3, color4, color5, color6, colorLosses};

        barDataSet.setColors(barColors);

        //remove ugly grid lines
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);

        //text stuff
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setValueTextSize(16f);

        //tidy up
        barChart.setDragEnabled(false);
        barChart.getDescription().setEnabled(false);

        //bottom labels
        final ArrayList<String> labels = new ArrayList<>();
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("6");
        labels.add("losses");

        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setGranularityEnabled(true);

        //display the total number of games played
        totalTextView = findViewById(R.id.totalgamesplayed);
        totalTextView.setText(Integer.toString(totalGamesPlayed));

        //display high score
        highScoreTextView = findViewById(R.id.highscore);
        highScoreTextView.setText(Integer.toString(highScore));
    }
}