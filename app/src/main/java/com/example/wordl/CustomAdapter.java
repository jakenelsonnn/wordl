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
import java.util.Arrays;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Square> {

    List<Square> squares = new ArrayList<>();
    int custom_layout_id;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Square> objects) {
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

        Square square = squares.get(position);
        TextView textView = v.findViewById(R.id.textView);
        textView.setText(String.valueOf(square.getLetter()));
        ConstraintLayout background = v.findViewById(R.id.square);

        switch (square.getColor()){
            case "gray":
            {
                background.setBackgroundColor(Color.GRAY);
                break;
            }
            case "green":
            {
                background.setBackgroundColor(Color.GREEN);
                break;
            }
            case "orange":
            {
                background.setBackgroundColor(Color.rgb(255, 165, 0));
                break;
            }
            default:
            {
                background.setBackgroundColor(Color.GRAY);
            }
        }

        return v;
    }
}
