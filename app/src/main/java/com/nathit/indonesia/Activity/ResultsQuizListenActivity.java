package com.nathit.indonesia.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nathit.indonesia.R;

public class ResultsQuizListenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_quiz_listen);

        final AppCompatButton startNewBtn = findViewById(R.id.startNewQuizBtn);
        final TextView correctAnswer = findViewById(R.id.correctAnswers);
        final TextView incorrectAnswers = findViewById(R.id.incorrectAnswers);
        final TextView AnswersAll = findViewById(R.id.AnswersAll);
        final TextView successTextView = findViewById(R.id.successTextView);

        final int getCorrectAnswers = getIntent().getIntExtra("correct", 0);
        final int getIncorrectAnswers = getIntent().getIntExtra("incorrect", 0);


        correctAnswer.setText(String.valueOf("คำตอบที่ถูกต้อง : " + getCorrectAnswers));
        incorrectAnswers.setText(String.valueOf("คำตอบผิด : " + getIncorrectAnswers));
        AnswersAll.setText("ทำคะแนนได้ทั้งหมด : " + getCorrectAnswers + " คะแนน");
        startNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultsQuizListenActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResultsQuizListenActivity.this, HomeActivity.class));
        finish();
    }
}