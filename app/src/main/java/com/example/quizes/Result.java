package com.example.quizes;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Result {
    private static final String FILE_NAME = "result.txt";
    private Context context;

    public Result(Context context) {
        this.context = context;
        initialize();
    }

    public void initialize() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String read() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        int sumCorrectAnswers = 0;
        int sumTotalQuestions = 0;
        int count = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(" \\| ");
                    int correctAnswers = Integer.parseInt(parts[0]);
                    int totalQuestions = Integer.parseInt(parts[1]);

                    sumCorrectAnswers += correctAnswers;
                    sumTotalQuestions += totalQuestions;
                    count++;
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (count > 0) {
            int averageCorrectAnswers = sumCorrectAnswers / count;
            int averageTotalQuestions = sumTotalQuestions / count;
            return averageCorrectAnswers + " | " + averageTotalQuestions;
        } else {
            return "0|0"; // Return a message indicating no valid data was found in the file
        }
    }


    public void update(int correctAnswers, int totalQuestions) {
        File file = new File(context.getFilesDir(), FILE_NAME);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            String result = correctAnswers + " | " + totalQuestions;
            writer.write(result);
            writer.newLine(); // Add a new line after the content
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getAverage() {
        String result = read();

        if (result.isEmpty()) {
            return "0/0";
        } else {
            String[] parts = result.split("\\|");
            if (parts.length >= 2) {
                int correctAnswers = Integer.parseInt(parts[0].trim());
                int totalQuestions = Integer.parseInt(parts[1].trim());
                if (totalQuestions > 0) {
                    return correctAnswers + "/" + totalQuestions;
                }
            }
        }

        return "0/0";
    }

    public void reset() {
        File file = new File(context.getFilesDir(), FILE_NAME);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
