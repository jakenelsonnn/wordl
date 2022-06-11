package com.example.wordl;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class FiveLetterLineChartActivity extends AppCompatActivity {

    ArrayList dataList = new ArrayList();
    TextView totalTextView;
    TextView highScoreTextView;
    private int highScore = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.five_letter_linegraph_activity);
        int totalGamesPlayed = 0;
        //set up the stats file, build array from file contents, total up the numbers in the file.
        String historyFilePath = getApplicationContext().getFilesDir() + "/" + "five-letter-history.txt";
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(historyFilePath));
            for(int i = 0; reader.readLine() != null; i++){
                int n = Integer.parseInt(reader.readLine());
                dataList.add(new Entry(i, n));
            }

        }catch (Exception e){

        }

        //set up the bar chart
        LineChart lineChart = findViewById(R.id.linechart);
        LineDataSet lineDataSet = new LineDataSet(dataList, "History");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        /*

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

         */

        //remove ugly grid lines
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);

        //text stuff
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setValueTextSize(16f);
        lineDataSet.setColor(Color.WHITE);

        //tidy up
        lineChart.setDragEnabled(true);
        lineChart.getDescription().setEnabled(false);

        /*

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
        xAxis.set

         */

        //display the total number of games played
        totalTextView = findViewById(R.id.totalgamesplayed4);
        totalTextView.setText(Integer.toString(totalGamesPlayed));

        //display high score
        highScoreTextView = findViewById(R.id.highscore4);
        highScoreTextView.setText(Integer.toString(highScore));
    }
}