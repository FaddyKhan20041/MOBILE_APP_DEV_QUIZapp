package com.example.quizes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class QuestionFragment extends Fragment {
    private static final String ARG_QUESTION_TEXT = "arg_question_text";
    private static final String ARG_QUESTION_COLOR = "arg_question_color";
    private static final String ARG_CURRENT_QUESTION_INDEX = "arg_current_question_index";
    private static final String ARG_TOTAL_QUESTIONS = "arg_total_questions";
    private static final String ARG_CORRECT_QUESTION_COUNT = "args_current_correct_question" ;
    private static final String ARG_CURRENT_CORECTQUESTION = "arg_current_corectquestion";

    private String questionText;
    private int questionColor;
    private int currentQuestionIndex;
    private int totalQuestions;
    private QuestionBank questionBank;
    private int correctQuestionCount = 0;


    public static QuestionFragment newInstance(String questionText, int questionColor, int currentQuestionIndex, int totalQuestions, QuestionBank questionBank, int correctQuestionCount) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION_TEXT, questionText);
        args.putInt(ARG_QUESTION_COLOR, questionColor);
        args.putInt(ARG_CURRENT_QUESTION_INDEX, currentQuestionIndex);
        args.putInt(ARG_TOTAL_QUESTIONS, totalQuestions);
        args.putInt(ARG_CORRECT_QUESTION_COUNT, correctQuestionCount);
        fragment.setArguments(args);
        fragment.questionBank = questionBank; // Store the QuestionBank instance
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionText = getArguments().getString(ARG_QUESTION_TEXT);
            questionColor = getArguments().getInt(ARG_QUESTION_COLOR);
            currentQuestionIndex = getArguments().getInt(ARG_CURRENT_QUESTION_INDEX);
            totalQuestions = getArguments().getInt(ARG_TOTAL_QUESTIONS);
            correctQuestionCount = getArguments().getInt(ARG_CORRECT_QUESTION_COUNT); // Corrected argument name
        }
    }


    private void setQuestionBank(QuestionBank questionBank) {
        this.questionBank = questionBank;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);

        TextView questionTextView = rootView.findViewById(R.id.question_text_view);
        questionTextView.setText(questionText);
//        rootView.setBackgroundColor(questionColor);

        LinearLayout textContainer = rootView.findViewById(R.id.text_container);
        textContainer.setBackgroundColor(questionColor);

        Button trueButton = rootView.findViewById(R.id.button_true);
        Button falseButton = rootView.findViewById(R.id.button_false);

        Locale locale = Locale.getDefault();
        String trueButtonText = getResources().getString(R.string.true_button);
        String falseButtonText = getResources().getString(R.string.false_button);

        trueButton.setText(trueButtonText);
        falseButton.setText(falseButtonText);


        // Set click listeners for true and false buttons
        rootView.findViewById(R.id.button_true).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        rootView.findViewById(R.id.button_false).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        return rootView;
    }


    private void checkAnswer(boolean userAnswer) {
        if (questionBank != null) {
            if (currentQuestionIndex >= 0 && currentQuestionIndex < questionBank.getQuestions().size()) {
                boolean correctAnswer = questionBank.getQuestions().get(currentQuestionIndex).isAnswer();
                if (userAnswer == correctAnswer) {
                    Toast.makeText(getActivity(), R.string.toast_correct_answer, Toast.LENGTH_SHORT).show();
                    correctQuestionCount++; // Increment the count of correct questions
                } else {
                    Toast.makeText(getActivity(), R.string.toast_incorrect_answer, Toast.LENGTH_SHORT).show();
                }

                currentQuestionIndex++;

                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.updateProgressBar(currentQuestionIndex, totalQuestions, correctQuestionCount); // Update the progress and correct question count
                }
            }

            if (currentQuestionIndex < totalQuestions) {
                Question question = questionBank.getNextQuestion();
                if (question != null) {
                    Fragment questionFragment = QuestionFragment.newInstance(question.getText(), question.getColor(), currentQuestionIndex, totalQuestions, questionBank, correctQuestionCount);
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, questionFragment)
                            .commit();
                }
            } else {
                showQuizCompletionDialog(correctQuestionCount, totalQuestions);
                currentQuestionIndex = 0;
            }
        }
    }


    private void showQuizCompletionDialog(int correctQuestionCount, int totalQuestions) {
        CompletionDialogFragment dialogFragment = new CompletionDialogFragment();

        Bundle args = new Bundle();
        args.putInt("correctQuestionCount", correctQuestionCount);
        args.putInt("totalQuestions", totalQuestions);
        dialogFragment.setArguments(args);

        dialogFragment.show(requireActivity().getSupportFragmentManager(), "CompletionDialogFragment");
    }

}
