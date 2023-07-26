package com.example.quizes;

public class Question {
    private String text;
    private boolean answer;
    private int color;

    private String translatedQuestion;

    public Question(String text, boolean answer, int color) {
        this.text = text;
        this.answer = answer;
        this.color = color;
        this.translatedQuestion = null;
    }

    // Add getter methods for the fields
    public String getText() {
        return text;
    }

    public String getTranslatedQuestion() {
        return translatedQuestion;
    }

    public void setTranslatedQuestion(String translatedQuestion) {
        this.translatedQuestion = translatedQuestion;
    }

    public boolean isAnswer() {
        return answer;
    }

    public int getColor() {
        return color;
    }
}
