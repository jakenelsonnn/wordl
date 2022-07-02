package com.example.wordl;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;

public class ColorSquareAdapter extends ArrayAdapter<ColorSquare> {

    List<ColorSquare> squares = new ArrayList<>();
    int custom_layout_id;

    public ColorSquareAdapter(@NonNull Context context, int resource, @NonNull List<ColorSquare> objects) {
        super(context, resource, objects);
        squares = objects;
        custom_layout_id = resource;
    }

    @Override
    public int getCount() {
        return squares.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(custom_layout_id, null);
        }

        ColorSquare square = squares.get(position);
        ConstraintLayout background = v.findViewById(R.id.colorsquare);
        background.setBackgroundColor(square.getColor());

        return v;
    }
}
