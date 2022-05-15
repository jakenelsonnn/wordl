package com.example.wordl;

public class Square {

    private char letter;
    private String color;

    Square(char letter, String color) {
        this.letter = letter;
        this.color = color;
    }

    public char getLetter() {
        return this.letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }
}

