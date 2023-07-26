package com.example.quizes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private QuestionBank questionBank;
    private int totalQuestions;
    private int currentQuestionIndex;
    private ProgressBar progressBar;
    private int correctQuestionCount;
    private int correctAnswersPerGame;
    private  Result result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = new Result(this);
        result.initialize();

        // Initialize the question bank and set the total number of questions
//        questionBank = new QuestionBank();
        questionBank = new QuestionBank(this);
        totalQuestions = questionBank.getQuestions().size();
        progressBar = findViewById(R.id.progress_bar);

        correctQuestionCount = 0;
        correctAnswersPerGame = 0;

        showQuestionFragment();
        Locale locale = Locale.getDefault();


        // Get the localized app name.
        String appName = getResources().getString(R.string.app_name);
        String formattedAppName = String.format("%s", appName);
        // Set the app's name to the localized app name.
        getSupportActionBar().setTitle(appName);

//        if (savedInstanceState == null) {
//            // Load the first question fragment
//            showQuestionFragment();
//        } else {
//            // Restore the state of the activity
//            currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex");
//        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentQuestionIndex", currentQuestionIndex);
    }

    private void showQuestionFragment() {
        Question question = questionBank.getNextQuestion();
        if (question != null) {
            Fragment questionFragment = QuestionFragment.newInstance(question.getText(), question.getColor(), currentQuestionIndex, totalQuestions, questionBank, correctQuestionCount);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, questionFragment);
            transaction.commit();
            currentQuestionIndex++;

//            int color = question.getColor();
//            View rootView = getWindow().getDecorView().getRootView();
//            rootView.setBackgroundColor(color);
        } else {
            // Quiz finished, show completion dialog
            showQuizCompletionDialog();
        }
    }

    private void showQuizCompletionDialog() {
        correctAnswersPerGame += correctQuestionCount;
        // Implement the completion dialog logic here
    }

    public void saveResults() {
        correctAnswersPerGame += correctQuestionCount;
    }

public void updateProgressBar(int currentQuestionIndex, int totalQuestions, int cqc) {
    int progress = (int) (((float) currentQuestionIndex / totalQuestions) * 100);
    progressBar.setProgress(progress);
//    int incorrectQuestionCount = currentQuestionIndex - correctQuestionCount;
    correctQuestionCount = cqc;
        progressBar.setProgress(progress);
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_average) {
            // Handle average menu item click
            showAverageDialog();
            return true;
        } else if (itemId == R.id.action_select_number_of_questions) {
            showNumberInputDialog();
            return true;
        } else if (itemId == R.id.action_reset_results) {
            // Handle reset results menu item click
            result.reset();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showNumberInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.menu_select_number_of_questions);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedNumber = input.getText().toString();
                int selectedCount = Integer.parseInt(selectedNumber);

                if (selectedCount > totalQuestions) {
                    // If selected count is greater than available questions, use the available count
                    selectedCount = totalQuestions;
                }
                Toast.makeText(MainActivity.this, "Selected Number: " + selectedCount, Toast.LENGTH_SHORT).show();
                totalQuestions = selectedCount;
                currentQuestionIndex = 0;
                reset();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean reset() {
        correctQuestionCount = 0;
        currentQuestionIndex = 0;
        correctAnswersPerGame = 0;
        updateProgressBar(currentQuestionIndex, totalQuestions, correctAnswersPerGame);
        showQuestionFragment(); // Reset the UI
        return true;
    }

    private void showAverageDialog() {
        String average = result.getAverage();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Your Correct answers / Total number of questions = " + average);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
