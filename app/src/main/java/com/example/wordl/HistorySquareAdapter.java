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

        switch (square.getColor()){
            case "blue":
            {
                background.setBackgroundColor(Color.rgb(27, 152, 224));
                break;
            }
            case "green":
            {
                background.setBackgroundColor(Color.rgb(31, 222, 85));
                break;
            }
            case "yellow":
            {
                background.setBackgroundColor(Color.rgb(225, 225, 0));
                break;
            }
            case "orangeish":
            {
                background.setBackgroundColor(Color.rgb(227, 177, 61));
                break;
            }
            case "orange":
            {
                background.setBackgroundColor(Color.rgb(227, 127, 27));
                break;
            }
            case "red":
            {
                background.setBackgroundColor( Color.rgb(255, 0, 0));
                break;
            }
            case "grey":
            {
                background.setBackgroundColor( Color.rgb(135, 135, 135));
            }
        }

        return v;
    }
}
