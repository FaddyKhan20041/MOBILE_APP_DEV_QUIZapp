package com.example.quizes;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionBank {
    private List<Question> questions;
    private int currentIndex;
    private int totalQuestions;

    public QuestionBank(Context context) {
        questions = new ArrayList<>();
        currentIndex = 0;
        readQuestionsFromTextFile(context);
        totalQuestions = questions.size();
        shuffleQuestions();
    }

    private void readQuestionsFromTextFile(Context context) {
        String languageCode = getLanguageCode(context);
        String fileName = "questions_" + languageCode + ".txt";

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String questionText = parts[0].trim();
                    boolean answer = Boolean.parseBoolean(parts[1].trim());
                    int color = generateRandomColor();
                    questions.add(new Question(questionText, answer, color));
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void shuffleQuestions() {
        Collections.shuffle(questions);
    }
    private int generateRandomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
    private String getLanguageCode(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return config.getLocales().get(0).getLanguage();
    }

    public Question getNextQuestion() {
        if (currentIndex < questions.size()) {
            Question question = questions.get(currentIndex);
            currentIndex++;
            return question;
        }
        return null;
    }

    public int getCurrentQuestionIndex() {
        return currentIndex;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
