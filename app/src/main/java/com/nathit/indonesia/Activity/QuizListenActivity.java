package com.nathit.indonesia.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nathit.indonesia.Fragment.ProfileFragment;
import com.nathit.indonesia.Fragment.QuizFragment;
import com.nathit.indonesia.Model.QuizListenModel;
import com.nathit.indonesia.QuestionListen;
import com.nathit.indonesia.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuizListenActivity extends AppCompatActivity {

    private TextView selectName;
    private TextView questions;
    private TextView question;

    private AppCompatButton option1, option2, option3, option4, nextBtn;

    private Timer quizTimer;

    private int totalTimeInMin = 1;

    private int seconds = 0;

    private List<QuizListenModel> quizListenModelList;

    private int currentQuestionPosition = 0;

    private String selectOptionByUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_listen);

        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView timer = findViewById(R.id.timer);

        questions = findViewById(R.id.questions);
        question = findViewById(R.id.question);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        nextBtn = findViewById(R.id.nextBtn);
        selectName = findViewById(R.id.selectName);

        final String getSelectedName = getIntent().getStringExtra("selectedTopic");

        selectName.setText(getSelectedName);

        quizListenModelList = QuestionListen.getQuestions(getSelectedName);

        startTimer(timer);

        questions.setText((currentQuestionPosition + 1) + "/" + quizListenModelList.size());
        question.setText(quizListenModelList.get(0).getQuestion());
        option1.setText(quizListenModelList.get(0).getOption1());
        option2.setText(quizListenModelList.get(0).getOption2());
        option3.setText(quizListenModelList.get(0).getOption3());
        option4.setText(quizListenModelList.get(0).getOption4());

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectOptionByUser.isEmpty()){
                    selectOptionByUser = option1.getText().toString();

                    option1.setBackgroundResource(R.drawable.round_quiz_red);
                    option1.setTextColor(Color.WHITE);

                    revealAnswer();

                    quizListenModelList.get(currentQuestionPosition).setUserSelectedAnswer(selectOptionByUser);
                }
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectOptionByUser.isEmpty()){
                    selectOptionByUser = option2.getText().toString();

                    option2.setBackgroundResource(R.drawable.round_quiz_red);
                    option2.setTextColor(Color.WHITE);

                    revealAnswer();

                    quizListenModelList.get(currentQuestionPosition).setUserSelectedAnswer(selectOptionByUser);
                }

            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectOptionByUser.isEmpty()){
                    selectOptionByUser = option3.getText().toString();

                    option3.setBackgroundResource(R.drawable.round_quiz_red);
                    option3.setTextColor(Color.WHITE);

                    revealAnswer();

                    quizListenModelList.get(currentQuestionPosition).setUserSelectedAnswer(selectOptionByUser);
                }
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectOptionByUser.isEmpty()){
                    selectOptionByUser = option4.getText().toString();

                    option4.setBackgroundResource(R.drawable.round_quiz_red);
                    option4.setTextColor(Color.WHITE);

                    revealAnswer();

                    quizListenModelList.get(currentQuestionPosition).setUserSelectedAnswer(selectOptionByUser);
                }

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectOptionByUser.isEmpty()) {
                    Toast.makeText(QuizListenActivity.this, "กรุณาเลือกคำตอบ!", Toast.LENGTH_SHORT).show();
                } else {
                    ChangeNextQuestion();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizTimer.purge();
                quizTimer.cancel();

                startActivity(new Intent(QuizListenActivity.this, HomeActivity.class));
                finish();
            }
        });

    }

    @SuppressLint("ResourceType")
    private void ChangeNextQuestion() {
        currentQuestionPosition++;

        if ((currentQuestionPosition + 1) == quizListenModelList.size()){
            nextBtn.setText("ส่งคำตอบ");
        }

        if (currentQuestionPosition < quizListenModelList.size()) {
            selectOptionByUser = "";
            option1.setBackgroundResource(R.drawable.round_quiz);
            option1.setTextColor(Color.parseColor("#1F6BB8"));

            option2.setBackgroundResource(R.drawable.round_quiz);
            option2.setTextColor(Color.parseColor("#1F6BB8"));

            option3.setBackgroundResource(R.drawable.round_quiz);
            option3.setTextColor(Color.parseColor("#1F6BB8"));

            option4.setBackgroundResource(R.drawable.round_quiz);
            option4.setTextColor(Color.parseColor("#1F6BB8"));

            questions.setText((currentQuestionPosition + 1) + "/" + quizListenModelList.size());
            question.setText(quizListenModelList.get(currentQuestionPosition).getQuestion());
            option1.setText(quizListenModelList.get(currentQuestionPosition).getOption1());
            option2.setText(quizListenModelList.get(currentQuestionPosition).getOption2());
            option3.setText(quizListenModelList.get(currentQuestionPosition).getOption3());
            option4.setText(quizListenModelList.get(currentQuestionPosition).getOption4());

        } else {

            Intent intent = new Intent(QuizListenActivity.this, ResultsQuizListenActivity.class);
            intent.putExtra("correct", getCorrectAnswers());
            intent.putExtra("incorrect", getInCorrectAnswers());
            startActivity(intent);

            finish();
        }
    }

    private void startTimer(TextView timerTv) {

        quizTimer = new Timer();

        quizTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if (seconds == 0) {
                    totalTimeInMin--;
                    seconds = 59;
                } else if (seconds == 0 && totalTimeInMin == 0) {

                    quizTimer.purge();
                    quizTimer.cancel();

                    Toast.makeText(QuizListenActivity.this, "หมดเวลา", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(QuizListenActivity.this, ResultsQuizListenActivity.class);
                    intent.putExtra("correct", getCorrectAnswers());
                    intent.putExtra("incorrect", getInCorrectAnswers());
                    startActivity(intent);

                    finish();
                } else {
                    seconds--;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String finalMinutes = String.valueOf(totalTimeInMin);
                        String finalSeconds = String.valueOf(seconds);

                        if (finalMinutes.length() == 1) {
                            finalMinutes = "0"+finalMinutes;

                        }

                        if (finalSeconds.length() == 1) {
                            finalSeconds = "0"+finalSeconds;
                        }

                        timerTv.setText(finalMinutes + ":" + finalSeconds);
                    }
                });

            }
        }, 1000, 1000);

    }

    private int getCorrectAnswers() {

        int correctAnswers = 0;

        for (int i = 0; i < quizListenModelList.size(); i++) {
            final String getUserSelectedAnswer = quizListenModelList.get(i).getUserSelectedAnswer();
            final String getAnswer = quizListenModelList.get(i).getAnswer();

            if (getUserSelectedAnswer.equals(getAnswer)) {
                correctAnswers++;
            }
        }
        return  correctAnswers;
    }

    private int getInCorrectAnswers() {

        int correctAnswers = 0;

        for (int i = 0; i < quizListenModelList.size(); i++) {
            final String getUserSelectedAnswer = quizListenModelList.get(i).getUserSelectedAnswer();
            final String getAnswer = quizListenModelList.get(i).getAnswer();

            if (!getUserSelectedAnswer.equals(getAnswer)) {
                correctAnswers++;
            }
        }
        return  correctAnswers;
    }

    @Override
    public void onBackPressed() {

        quizTimer.purge();
        quizTimer.cancel();

        startActivity(new Intent(QuizListenActivity.this, HomeActivity.class));
        finish();
    }

    private void revealAnswer(){

        final String getAnswer = quizListenModelList.get(currentQuestionPosition).getAnswer();

        if (option1.getText().toString().equals(getAnswer)) {
            option1.setBackgroundResource(R.drawable.round_quiz_green);
            option1.setTextColor(Color.WHITE);
        }
        else if (option2.getText().toString().equals(getAnswer)) {
            option2.setBackgroundResource(R.drawable.round_quiz_green);
            option2.setTextColor(Color.WHITE);
        }
        else if (option3.getText().toString().equals(getAnswer)) {
            option3.setBackgroundResource(R.drawable.round_quiz_green);
            option3.setTextColor(Color.WHITE);
        }
        else if (option4.getText().toString().equals(getAnswer)) {
            option4.setBackgroundResource(R.drawable.round_quiz_green);
            option4.setTextColor(Color.WHITE);
        }
    }
}