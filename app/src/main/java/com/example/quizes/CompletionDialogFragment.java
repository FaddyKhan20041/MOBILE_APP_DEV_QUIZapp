package com.example.quizes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CompletionDialogFragment extends DialogFragment {
    private  Result result;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        result = new Result(getContext());
        result.initialize();

        int totalQuestions = 0;
        int correctQuestionCount = 0;
        if (args != null) {
            correctQuestionCount = args.getInt("correctQuestionCount", 0);
            totalQuestions = args.getInt("totalQuestions", 0);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_quiz_completed_title);
//        builder.setMessage(R.string.dialog_quiz_completed_message);
        String message = getString(R.string.dialog_quiz_completed_message) +
                "\n\nYour score is: " + correctQuestionCount + " out of " + totalQuestions;
        builder.setMessage(message);

        int finalCorrectQuestionCount = correctQuestionCount;
        int finalTotalQuestions = totalQuestions;
        builder.setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Save the results to the file system
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.saveResults();
                Toast.makeText(getActivity(), R.string.toast_results_saved, Toast.LENGTH_SHORT).show();

                result.update(finalCorrectQuestionCount, finalTotalQuestions);

            }
        });

        builder.setNegativeButton(R.string.ignore_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ignore the results and reset the quiz
                Toast.makeText(getActivity(), R.string.toast_results_ignored, Toast.LENGTH_SHORT).show();
                // Reset the quiz using the MainActivity instance
            }
        });

        return builder.create();
    }


}
