package com.example.wordl;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;

public class HistorySquareAdapter extends ArrayAdapter<HistorySquare> {

    List<HistorySquare> squares = new ArrayList<>();
    int custom_layout_id;

    public HistorySquareAdapter(@NonNull Context context, int resource, @NonNull List<HistorySquare> objects) {
        super(context, resource, objects);
        squares = objects;
        custom_layout_id = resource;
    }

    @Override
    public int getCount() {
        return squares.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(custom_layout_id, null);
        }

        HistorySquare square = squares.get(position);
        ConstraintLayout background = v.findViewById(R.id.historysquare);
        TextView number = v.findViewById((R.id.historysquaretextview));

        switch (square.getColor()){
            case "OneGuessColor":
            {
                background.setBackgroundColor(Color.rgb(163, 163, 255));
                number.setText("1");
                break;
            }
            case "TwoGuessColor":
            {
                background.setBackgroundColor(Color.rgb(16, 52, 166));
                number.setText("2");
                break;
            }
            case "ThreeGuessColor":
            {
                background.setBackgroundColor(Color.rgb(65, 47, 136));
                number.setText("3");
                break;
            }
            case "FourGuessColor":
            {
                background.setBackgroundColor(Color.rgb(114, 43, 106));
                number.setText("4");
                break;
            }
            case "FiveGuessColor":
            {
                background.setBackgroundColor(Color.rgb(162, 38, 75));
                number.setText("5");
                break;
            }
            case "SixGuessColor":
            {
                background.setBackgroundColor( Color.rgb(246, 45, 45));
                number.setText("6");
                break;
            }
            case "LossColor":
            {
                background.setBackgroundColor( Color.rgb(60, 84, 88));
                number.setText("x");
            }
        }

        return v;
    }
}
