package com.example.wordl;

public class Square {

    private char letter;
    private int color;

    Square(char letter, int color) {
        this.letter = letter;
        this.color = color;
    }

    public char getLetter() {
        return this.letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }
}

